package com.cloudmine.mine;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.cloudmine.foreman.Task;
import com.cloudmine.http.AppClient;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Mine implements Runnable{

	private static transient final int THREAD_TIMEOUT = 5*1000;
	public static final String SERVER_IP = "http://localhost:8080";
	
	protected transient AppClient master = new AppClient(SERVER_IP);
	protected List<Solution> solutionsQueue = new LinkedList<>();
	
	protected transient Gson gson = new Gson();
	protected transient JsonParser jparse = new JsonParser();
	protected transient Thread thread = new Thread(this);
	protected transient int timeSinceLastPost = 0;
	
	protected  Miner[] miners;
	
	public Mine(int numMiners) {
		miners = new Miner[numMiners];
		for(int i=0; i < miners.length; i++)
			miners[i] = new ForwardMiner(solutionsQueue);
	}
	
	
	protected void contactServer(){
		String json = gson.toJson(this);
		
		//System.out.println("M REQUEST:"+json);
		String responce = master.post(json);
		
		
		System.out.println("M RESPONCE:"+responce);
		
		JsonArray jTasks = jparse.parse(responce).getAsJsonArray();
		System.out.println("M Tasks: "+jTasks);

		
		for(JsonElement t: jTasks){
			Task task = gson.fromJson(t, Task.class);
			
			for(Miner m : miners)
				m.assign(task);
			
				
			System.out.println("\tproc : "+task);
		}
		solutionsQueue.clear();
	}
	
	
	@Override
	public void run() {
		while(true){		
			try {
				contactServer();
				Thread.sleep(THREAD_TIMEOUT);
			} catch (Exception e) {}
		}
	}

	public void start(){
		thread.start();
	}
	
	
	public static void main(String[] args) {
		Mine mine = new Mine(4);
		mine.start();
	}
}
