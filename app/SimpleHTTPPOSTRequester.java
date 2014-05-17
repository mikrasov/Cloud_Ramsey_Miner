package com.cloudmine.http;
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
    private long tStamp = Timestamp.getTime();
    private String apiURL = "http://richcoin.cs.ucsb.edu:8280/vault/1.0.0";
    URL url = new URL("http://richcoin.cs.ucsb.edu:8280/vault/1.0.0");
    
    Calendar calendar = Calendar.getInstance();
    java.sql.Date currentTimestamp = new java.sql.Timestamp(calendar.getTime().getTime());
    FinalTime = currentTimestamp.toString();
    
    
    Graph graObj = new Graph(6); //taking size as 6
    
    public SimpleHTTPPOSTRequester(String solution, long FinalTime, String apiURL) {

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