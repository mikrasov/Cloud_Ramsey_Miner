package com.cloudmine.http;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerServer;
import org.simpleframework.transport.Server;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

public abstract class AppServer implements Container {
		
	public static final String CONTENT_HTML = "text/html";
	public static final String CONTENT_JSON = "text/json";
	
	
	protected final int port;
	private String contentType;
	private Server server;
	private Connection connection;
	private SocketAddress address;
	   
	public AppServer(int port, String contentType){
		this.port = port;
		this.contentType = contentType;
	}
	   
	public void start() throws IOException{
		server = new ContainerServer(this);
		connection = new SocketConnection(server);
		address = new InetSocketAddress(port);
		connection.connect(address);
	}

	public void handle(Request request, Response response) {
		try {
	         PrintStream body = response.getPrintStream();
	         long time = System.currentTimeMillis();
	   
	         body.println( process(request.getContent()) );
	         
	         response.setValue("Content-Type", contentType);
	         response.setValue("Server", "CloudMine/1.0 (Simple 4.0)");
	         response.setDate("Date", time);
	         response.setDate("Last-Modified", time);
	         
	         body.close();
	         
	      } catch(Exception e) {
	         e.printStackTrace();
	      }
	}

	public void close(){
		try {
			connection.close();
		} catch (IOException e) {}
	}
	
	public abstract String process(String request) throws IOException;
}
