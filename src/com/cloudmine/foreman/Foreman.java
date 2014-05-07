package com.cloudmine.foreman;

import java.io.IOException;

import com.cloudmine.Bank;
import com.cloudmine.Graph;
import com.cloudmine.http.AppServer;
import com.cloudmine.mine.Solution;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Foreman extends AppServer {

	protected Bank bank = new Bank();
	
	public Foreman(int port) throws IOException {
		super(8080);
		bank.put(Graph.generateRandom(8));
	}

   public static void main(String[] list) throws IOException {
	   Foreman master = new Foreman(8080);
	   
	   System.out.println("Starting Foreman");
	   master.load();
	   
	  
	   master.start();
	   System.out.println("Started");
	   
   }
   
   public void load(){
	   System.out.print("Trying to load from file '"+Bank.BANK_FILENAME+"' : ");
	   try {
		   bank.load();
		   System.out.println("File loaded");
	   } catch (ClassNotFoundException | IOException e) {
			System.out.println("Failed to Load file");
	   }
   }

	@Override
	public JsonElement process(String request) throws IOException {
		Gson gson = new Gson();
		JsonParser jparse = new JsonParser();
		JsonObject jStatus = jparse.parse(request).getAsJsonObject();
		
		System.out.println("Request: "+request);
		
		JsonArray jSolutions = jStatus.get("solutionsQueue").getAsJsonArray();
		System.out.println("Solutions");
		for(JsonElement s: jSolutions){
			Solution solution = gson.fromJson(s, Solution.class);
			System.out.println(solution);
		}
		
		JsonArray jMiners = jStatus.get("miners").getAsJsonArray();
		System.out.println("miners:");
		for(JsonElement m: jMiners){
			System.out.println(m);
		}
		
		
		return gson.toJsonTree(new Solution(3, "1010", 3, true));
	}

}
