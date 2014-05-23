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
			
			if(activeTasks.containsKey(solution.getTaskId())){
				Task task = activeTasks.get(solution.getTaskId());
				task.generatedGraph(graph);
			}
			
			//Send this  to api
			if(graph.size() > 102){
				//TODO: ADD API CALLL HERE
				//To send solution call graph.encodeAsJsonValue()
			}
			
			graph.assign();
			bank.put(graph);
			System.out.println("\tAdding : "+graph.encodeAsJsonValue());
			
			//Un-assign origin
			if(graph.getOriginId() != null && bank.contains(graph.getOriginId())){
				Graph origin = bank.get(graph.getOriginId());
				origin.unassign();
			}
		}
	}
	
	private List<Task> processMiners(Configuration mineConfig, JsonArray jMiners){
		List<Task> tasksToSend = new LinkedList<>();
		System.out.println("> Miners:");
		for(JsonElement m: jMiners){
			Miner miner = gson.fromJson(m, Miner.class);
			
			System.out.println("\t"+m);
			
			System.out.println("\tTask ID "+miner.getTask()+" "+miner.hasTask());
			
			if(miner.hasTask()){	
				if(activeTasks.containsKey(miner.getTask())){
					//Tracked task just seen
					Task task = activeTasks.get(miner.getTask());
					task.justSeen();
					if(miner.failedToFindSolution()){
						task.markFailed();		
						
						if(task.getSeed() != null)
							task.getSeed().unassign();
					}
					System.out.println("Just seen");
				}
				else{
					//Unknown task added
					Task task = new Task(miner.getTask(), mineConfig, miner.getId());
					task.justSeen();
					activeTasks.put(task.getTaskId(), task);
					System.out.println("Unkown task added");
				}
			}	
			
			//Miner is stopped
			if(!miner.isRunning()){
				Graph bestAvailable  = bank.getBest(mineConfig.isLongTerm()?Configuration.SLOW_CUTOFF:Configuration.FAST_CUTOFF);				
				Task task = new Task(mineConfig, miner.getId(), bestAvailable);
				task.getSeed().assign();
				task.justSeen();
				activeTasks.put(task.getTaskId(), task);
				
				tasksToSend.add(task); //To sent to server
				System.out.println("\t ^ ASSIGNING: "+ task);
			}
			
		}
		return tasksToSend;
	}
	
}
