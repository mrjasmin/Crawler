
/**
 * @author Jasmin Krhan
 * @version 1.0
 */
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {

    private Stack<String> unvisitedURL;
    private List visitedURL;

    private List domain;
    private List keywords;

    private int size;

    //Start crawling from this URL
    private URL start;

    //PriorityBlockingQueue<Link> prQueue = new PriorityBlockingQueue<Link>();
    /**
     * Default constructor. Initializes the list used to keep track of the
     * crawling process.
     */
    public Crawler(String URLstart) {

        unvisitedURL = new Stack<String>();
        visitedURL = new ArrayList<String>();
        
        try {
            start = new URL(URLstart);
            unvisitedURL.add(URLstart);
        } catch (MalformedURLException e) {
            System.out.println("Not a valid Seed URL");
        }

    }

    /**
     * This is the main crawl function. Pops an URL form the stack and gets the
     * content from the website representing the URL.
     */
    public void crawl() throws URISyntaxException {

        while (true) {

            String URL = "";
            String content;
            String parsedContent = "";

            if (!unvisitedURL.empty()) {
                URL = unvisitedURL.pop();
            }
            
            System.out.println(URL); 

            if (!visitedURL.contains(URL)) {//Crawl

                content = getWebSiteContent(URL);

                if (content != "") {
                    parsedContent = parsePage(URL, content);

                }

                try {

                    Document doc = Jsoup.parse(content);
                    Elements links = doc.select("a[href]");

                    doc.setBaseUri(URL);

                    for (Element e : links) {

                        if (!unvisitedURL.contains(e.absUrl("href")) && !e.toString().contains("mailto")
                                && !visitedURL.contains(e.absUrl("href")) 
                                && !e.absUrl("href").toString().contains("twitter")
                                && !e.absUrl("href").toString().contains("facebook")) {

                            //.out.println(e.absUrl("href") + "After normalization: " + URLNormalizer.getCanonicalURL(e.absUrl("href")) );

                            //Normalize the URL and add it to unvisited sites 
                            unvisitedURL.add(URLNormalizer.getCanonicalURL(e.absUrl("href"))); 
                            
                        }

                    }
                } catch (Exception e) {
                    System.out.println("Jscoup exception");
                }

                visitedURL.add(URL);

            }

        }

    }

    /**
     * Parse a web page represented by a string. Extract the main text and
     * remove all HTML-tags. Uses the Boilerpipe library to accomplish this
     * task.
     *
     * @param string
     * @param content
     */
    private String parsePage(String url, String content) {
        String text = "";

        try {

            if (content != null) {
                text = ArticleExtractor.INSTANCE.getText(content);
            }

            if (text.length() > 200) {
                System.out.println(text);
            }
        } catch (BoilerpipeProcessingException e) {
            System.out.println("BoilerpipeProcessingException");
        }

        return text;

    }

    private void writeToFile(String content) {

    }

    private String getDomain(String URL) {

        String domain = "";

        try {
            URI uri = new URI(URL);
            domain = uri.getHost();

            // Use regular expression to extract last part of URL, .com/.se/.org
            // etc etc
            //Code goes here
        } catch (Exception e) {
            //error 
        }

        return domain;

    }

    private String getWebSiteContent(String URL) {

        try {

            URL url = new URL(URL);
           
            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setInstanceFollowRedirects(true);
            HttpURLConnection.setFollowRedirects(true);

            conn.setReadTimeout(5000); //Set the timeout to 5 seconds
            conn.addRequestProperty("User-Agent", "Mozilla");

            int status = conn.getResponseCode();

            boolean redirect = false;

            //If the HTTP code is 200, proceed, otherwise some error has occured. 
            //Also Check that the content fetched is no image.
            if (status == HttpURLConnection.HTTP_OK && !conn.getContentType().startsWith("image")) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));

                String buffer = "";
                String content;

                while ((content = in.readLine()) != null) {
                    buffer += content + "\n";

                }

                in.close();
                return buffer;

            } else if (status == HttpURLConnection.HTTP_MOVED_PERM
                    || status == HttpURLConnection.HTTP_MOVED_TEMP
                    || status == HttpURLConnection.HTTP_SEE_OTHER) {

                redirect = true;

            }

            if (redirect) {

                String newURL = conn.getHeaderField("Location");

                conn = (HttpURLConnection) new URL(newURL).openConnection();
                conn.setReadTimeout(5000); //Set the timeout to 5 seconds
                conn.addRequestProperty("User-Agent", "Mozilla");

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));

                String buffer = "";
                String content;

                while ((content = in.readLine()) != null) {
                    buffer += content + "\n";

                }

                in.close();
                return buffer;

            }

        } catch (MalformedURLException me) {
            //Handle error here
        } catch (IOException ex) {
            Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * Prints the crawled web pages
     */
    public void printVisitedURLs() {
        Iterator<String> it = visitedURL.iterator();

        System.out.println("Crawled Web pages: " + "\n");

        while (it.hasNext()) {
            System.out.println(it.next().toString());
        }

    }

}
