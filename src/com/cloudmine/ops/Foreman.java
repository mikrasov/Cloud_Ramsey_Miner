package com.cloudmine.ops;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import com.cloudmine.Configuration;
import com.cloudmine.Graph;
import com.cloudmine.Task;
import com.cloudmine.http.AppServer;
import com.cloudmine.mine.Miner;
import com.cloudmine.mine.Solution;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Foreman extends AppServer {

	protected final JsonParser jparse = new JsonParser();
	protected final Gson gson = new Gson();
	
	protected Bank bank = new Bank();
	protected Map<UUID, Task> activeTasks = new TreeMap<>();
	
	public Foreman(Bank bank, Map<UUID, Task> map) {
		super(Configuration.FOREMAN_PORT, AppServer.CONTENT_JSON);
		this.bank = bank;
		this.activeTasks = map;
	}

	@Override
	public String process(String request) throws IOException {	
		JsonObject jRequest = jparse.parse(request).getAsJsonObject();
		
		System.out.println("REQUEST: "+request);
		
		Configuration config = gson.fromJson(jRequest.get("configuration"), Configuration.class);
		List<Task> taskList = processMiners(config,jRequest.get("miners").getAsJsonArray() );
		
		parseSolutions(jRequest.get("solutionsQueue").getAsJsonArray());
		bank.save();

		JsonElement responce = gson.toJsonTree(taskList);
		
		System.out.println("RESPONCE: "+responce);
		return responce.toString();
	}
	

	private void parseSolutions(JsonArray jSolutions ){
		System.out.println("> Solutions:");
		for(JsonElement s: jSolutions){
			Solution solution = gson.fromJson(s, Solution.class);
			Graph graph = solution.getGraph();
			
			//Send this  to api
			if(graph.size() > 102){
				String jsonSolution = graph.encodeAsJsonValue();
				//TODO: ADD API CALLL HERE
			}
			
			graph.assign();
			bank.put(graph);
			System.out.println("\tAdding : "+graph.encodeAsJsonValue());
			
			//Un-assign origin
			if(bank.contains(graph.getOriginId())){
				Graph origin = bank.get(graph.getOriginId());
				origin.unassign();
			}
			
			
		}
	}
	
	private List<Task> processMiners(Configuration mine, JsonArray jMiners){
		List<Task> taskList = new LinkedList<>();
		System.out.println("> Miners:");
		for(JsonElement m: jMiners){
			Miner miner = gson.fromJson(m, Miner.class);
			
			System.out.println("\t"+m);
			
			if(miner.hasTask() && activeTasks.containsKey(miner.getTask())){
				Task task = activeTasks.get(miner.getTask());
				task.justSeen();
				if(miner.failedToFindSolution())
					task.markFailed();
			}
			else{
				//TODO: Logic for what happens when a miner is working on a task the server knows nothing about
			}
				
			if(!miner.isRunning()){
				
				Graph bestAvailable  = bank.getBest(mine.isLongTerm()?Configuration.SLOW_CUTOFF:Configuration.FAST_CUTOFF);
				Task task = assign(mine,miner.getId(), bestAvailable);
				
				taskList.add(task); //To sent to server
				
				System.out.println("\t ^ ASSIGNING: "+ task);
			}
			
			
			
			
		}
		return taskList;
	}
	
	private Task assign(Configuration mine, UUID targetMiner, Graph seed){
		Task task = new Task(mine, targetMiner, seed);
		
		activeTasks.put(task.getTaskId(), task);
		
		task.getSeed().assign();
		return task;
	}
	
	
}
