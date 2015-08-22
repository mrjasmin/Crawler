import java.net.URL;

/**
 *
 * @author Jasmin Krhan
 */
public class Link implements Comparable<Link>{
    
    private URL url; 
    private double score;
    
    public Link(URL u, int s){
        url = u; 
        score = s; 
    }
    
    public double getScore(){
        return score;
    }
    
    public URL getURL(){
        return url; 
    }
    
    public void setScore(double s){
        this.score = s ;
    }
    
    public void setLink(URL u){
        this.url = u ; 
    }

    
    @Override
    public double compareTo(Link o) {
        return this.score - o.score; 
    }
   
}
