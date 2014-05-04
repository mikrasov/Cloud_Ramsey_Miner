package caveProgram.test.spring.testProgram;

/**
 * Hello world!
 *
 */

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient; 
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;

public class App 
{
	
	public static void main(String[] args) throws ClientProtocolException, IOException{
		 //sandbox key: 0Aju5YR6aSOXnr1CBoheRwMY0rga
		//OAuthBearerClientRequest kk;

		 String accessToken = "0Aju5YR6aSOXnr1CBoheRwMY0rga";
		 HttpClient httpclient = HttpClientBuilder.create().build();
		    HttpPost httppost = new HttpPost("http://richcoin.cs.ucsb.edu:8280/vault/1.0.0");
		    httppost.setHeader("Authorization", "Bearer "+accessToken);	
		    httppost.addHeader("Authorization:", " Bearer" + accessToken);
		    
		    //it might also be:
		    // httppost.addHeader("Authorization:", " Bearer" + accessToken);
		    
		    
		   // Log.d("DEBUG", "HEADERS: " + httppost.getFirstHeader("Authorization: Bearer"));

		    ResponseHandler<String> responseHandler = new BasicResponseHandler();
		    String responseBody = httpclient.execute(httppost, responseHandler);

		   // Log.d("DEBUG", "RESPONSE: " + responseBody);
    
    }
}
