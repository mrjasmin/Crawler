
/**
 *
 * @author Jasmin Krhan
 */

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import com.google.gson.Gson; 
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GoogleSearch {

    private int numberOfSeeds;
    private List<String> querys;

    private final String charset = "UTF-8";
    private final String queryType;

    private String query = "programcreek";
    private List<String> result; 
    
    
    public GoogleSearch(List<String> l, String q, int n) {
        querys = l;
        numberOfSeeds = n;
        queryType = q;
        result = new ArrayList<String>(); 
    }

    public void doSearch() {

        for (int i = 0; i < numberOfSeeds; i = i + 4) {

            String address = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&start=" + i + "&q=";

            URL url = null;
            Reader reader = null; 
            
            try {
                url = new URL(address + URLEncoder.encode(query, charset));
            } catch (UnsupportedEncodingException ex) {

            } catch (MalformedURLException ex) {

            }
            try {
               reader = new InputStreamReader(url.openStream(), charset);
            } catch (UnsupportedEncodingException ex) {

            } catch (IOException ex) {

            }
             
            GoogleResults results = new Gson().fromJson(reader, GoogleResults.class);
            
        }

    }

    private String formatANDQuery(List<String> l) {
        return null;
    }

    private String formatORQuery(List<String> l) {
        return null;
    }

}
