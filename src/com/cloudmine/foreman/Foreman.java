package com.cloudmine.foreman;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

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
	protected JsonParser jparse = new JsonParser();
	protected Gson gson = new Gson();

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
		
		JsonObject jRequest = jparse.parse(request).getAsJsonObject();
		
		System.out.println("REQUEST: "+request);
		
		JsonArray jSolutions = jRequest.get("solutionsQueue").getAsJsonArray();
		System.out.println("> Solutions:");
		for(JsonElement s: jSolutions){
			Graph solution = gson.fromJson(s, Solution.class).convertToGraph();
			bank.put(solution);
			System.out.println("\tAdding : "+solution.encodeAsJsonValue());
		}
		
		List<Task> taskList = new LinkedList<>();
		JsonArray jMiners = jRequest.get("miners").getAsJsonArray();
		System.out.println("> Miners:");
		for(JsonElement m: jMiners){
			JsonObject jminer = m.getAsJsonObject();
			UUID minerId = UUID.fromString(jminer.get("id").getAsString());
			boolean running = jminer.get("running").getAsBoolean();
			
			if(!running)
				taskList.add(new Task(minerId, Graph.generateRandom(8)));
			System.out.println("\t"+m);
		}
		
		bank.save();
		
		
		
		
		
		JsonElement responce = gson.toJsonTree(taskList);
		System.out.println("RESPONCE: "+responce);
		return responce;
	}

}
