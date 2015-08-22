
import java.net.URISyntaxException;
import java.net.URL;

/**
 *
 * @author Jasmin
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws URISyntaxException {
        // TODO code application logic here
        
        Crawler c = new Crawler("http://www.expressen.se"); 
        c.crawl();
        
   
        
    }
    
}
