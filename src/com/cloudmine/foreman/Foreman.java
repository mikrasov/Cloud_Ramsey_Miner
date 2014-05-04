package com.cloudmine.foreman;

import java.io.IOException;

import com.cloudmine.http.AppServer;
import com.cloudmine.mine.Solution;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class Foreman extends AppServer {

	public Foreman(int port) throws IOException {
		super(8080);
	}

   public static void main(String[] list) throws Exception {
	   Foreman master = new Foreman(8080);
	   master.start();
	   System.out.println("Started Foreman ");
   }

	@Override
	public JsonElement process(String request) throws IOException {
		Gson gson = new Gson();
		System.out.println("Request: "+request);
		return gson.toJsonTree(new Solution(3, "1010", 3, true));
	}

}
