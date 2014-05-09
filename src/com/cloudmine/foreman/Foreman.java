package com.cloudmine.foreman;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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

	public static final int DEAFAULT_PORT = 8080;
	
	protected final JsonParser jparse = new JsonParser();
	protected final Gson gson = new Gson();
	
	protected Bank bank = new Bank();
	protected Map<UUID, Task> map = new TreeMap<>();
	
	public Foreman(Bank bank, Map<UUID, Task> map) {
		super(DEAFAULT_PORT, AppServer.CONTENT_JSON);
		this.bank = bank;
		this.map = map;
	}

	@Override
	public String process(String request) throws IOException {	
		JsonObject jRequest = jparse.parse(request).getAsJsonObject();
		
		System.out.println("REQUEST: "+request);
		
		parseSolutions(jRequest.get("solutionsQueue").getAsJsonArray());

		List<Task> taskList = processMiners(jRequest.get("miners").getAsJsonArray() );
		
		bank.save();
		
		JsonElement responce = gson.toJsonTree(taskList);
		System.out.println("RESPONCE: "+responce);
		return responce.toString();
	}
	
	private void parseSolutions(JsonArray jSolutions ){
		System.out.println("> Solutions:");
		for(JsonElement s: jSolutions){
			Graph solution = gson.fromJson(s, Solution.class).convertToGraph();
			bank.put(solution);
			System.out.println("\tAdding : "+solution.encodeAsJsonValue());
		}
	}
	
	
	private List<Task> processMiners(JsonArray jMiners){
		List<Task> taskList = new LinkedList<>();
		System.out.println("> Miners:");
		for(JsonElement m: jMiners){
			JsonObject jminer = m.getAsJsonObject();
			UUID minerId = UUID.fromString(jminer.get("id").getAsString());
			boolean running = jminer.get("running").getAsBoolean();
			
			if(!running){
				Task task = new Task(minerId, Graph.generateRandom(8));
				taskList.add(task);
				map.put(task.getTaskId(), task);
			}
			System.out.println("\t"+m);
		}
		return taskList;
	}
}
