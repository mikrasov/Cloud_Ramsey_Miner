package com.mikrasov.cloudmine.http;

import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerServer;
import org.simpleframework.transport.Server;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

import com.google.gson.Gson;

public class AppServer implements Container {
	
	
	public static class Task implements Runnable {
		  
		private class CloudResponce{
			private int value1 = 1;
			  private String value2 = "abc";
			  private  int value3 = 3;
			  CloudResponce() {} 
		}
		
	      private final Response response;
	      private final Request request;
	 
	      public Task(Request request, Response response) {
	         this.response = response;
	         this.request = request;
	      }

	      public void run() {
		      try {
		         PrintStream body = response.getPrintStream();
		         long time = System.currentTimeMillis();
		   
		         System.out.println(request.getPath());
		         
		         Gson gson = new Gson();
		         String json;
		         
		         switch (request.getPath().toString().toLowerCase()) {
		            case "january":
		            	body.println(gson.toJson(new CloudResponce()));
		            break;
		            
		            case "february":
		            	body.println(gson.toJson(new CloudResponce()));
		   	        break;

		            default: 
		   	         	body.println(gson.toJson(new CloudResponce()));
		   	        break;
		        }
		         
		         response.setValue("Content-Type", "text/json");
		         response.setValue("Server", "CloudMine/1.0 (Simple 4.0)");
		         response.setDate("Date", time);
		         response.setDate("Last-Modified", time);
		   
		         
		         
		         body.close();
		         
		      } catch(Exception e) {
		         e.printStackTrace();
		      }
	      }
	   } 
	   
	public static final int MAX_NUM_THREADS = 50;
	
	   private final Executor executor;

	   public AppServer(int size) {
	      this.executor = Executors.newFixedThreadPool(size);
	   }

	   public void handle(Request request, Response response) {
	      Task task = new Task(request, response);
	      
	      executor.execute(task);
	   }

	
	   public static void main(String[] list) throws Exception {
	      Container container = new AppServer(MAX_NUM_THREADS);
	      Server server = new ContainerServer(container);
	      Connection connection = new SocketConnection(server);
	      SocketAddress address = new InetSocketAddress(8080);

	      connection.connect(address);
	   }
}
