import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.entity.ContentType;
import org.apache.http.HttpResponse;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import java.util.Map;
import java.sql.Timestamp;
import java.util.Date;

public class SimpleHTTPPOSTRequester {
    
    private String apiusername;
    private long Timestamp;
    private String apiURL;
    
    Graph graObj = new Graph(6);
    
    public SimpleHTTPPOSTRequester(String solution, long Timestamp, String apiURL) {

        this.solution= graObj.toString();
        this.Timestamp = Timestamp;
        this.apiURL = apiURL;
        //convert graph to String?
    }
    
    public void makeHTTPPOSTRequest() {
        try {
            HttpClient c = new DefaultHttpClient();
            HttpPost p = new HttpPost(this.apiURL);
            
            p.setEntity(new StringEntity("{\"solution\":\"" + this.solution + "\",\"timestamp\":\"" + this.Timestamp + "\"}",
                                         ContentType.create("application/json")));
            
            HttpResponse r = c.execute(p);
            
            BufferedReader rd = new BufferedReader(new InputStreamReader(r.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                JSONParser j = new JSONParser();
                JSONObject o = (JSONObject)j.parse(line);
                Map response = (Map)o.get("response");
                
                System.out.println(response.get("somevalue"));
            }
        }
        catch(ParseException e) {
            System.out.println(e);
        }
        catch(IOException e) {
            System.out.println(e);
        }                        
    }    
}