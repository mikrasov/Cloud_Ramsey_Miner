package com.cloudmine.mine;

import java.util.LinkedList;
import java.util.List;

import com.cloudmine.Task;
import com.cloudmine.http.AppClient;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Mine implements Runnable{

	private static transient final int THREAD_TIMEOUT = 5*1000;
	public static final String SERVER_IP = "http://localhost:8080";
	
	protected final String type;
	protected final boolean longTerm;
	protected List<Solution> solutionsQueue = new LinkedList<>();
	
	protected transient AppClient master = new AppClient(SERVER_IP);
	protected transient Gson gson = new Gson();
	protected transient JsonParser jparse = new JsonParser();
	protected transient Thread thread = new Thread(this);
	protected transient int timeSinceLastPost = 0;
	
	protected  Miner[] miners;
	
	public Mine(String machineType, int numMiners, boolean longTerm) {
		this.type = machineType;
		this.longTerm = longTerm;
		miners = new Miner[numMiners];
		for(int i=0; i < miners.length; i++)
			miners[i] = new ForwardMiner();
	}
	
	protected void contactServer(){
		String json = gson.toJson(this);
		
		//System.out.println("M REQUEST:"+json);
		String responce = master.post(json);

		//System.out.println("M RESPONCE:"+responce);
		
		JsonArray jTasks = jparse.parse(responce).getAsJsonArray();
		System.out.println("M Tasks: "+jTasks);

		
		for(JsonElement t: jTasks){
			Task task = gson.fromJson(t, Task.class);
			
			//Declare this as the origin
			task.getSeed().setAsOrigin();
			
			System.out.println("Task "+task);
			for(Miner m : miners)
				m.assign(task);
		}
		solutionsQueue.clear();
	}
	
	protected void pollMiners(){
		for(Miner m :miners){
			Solution s = m.poll();
			while(s != null){
				solutionsQueue.add(s);
				s = m.poll();
			}
		}
	}
	
	
	@Override
	public void run() {
		while(true){		
			try {
				pollMiners();
				contactServer();
				Thread.sleep(THREAD_TIMEOUT);
			} catch (Exception e) {
				System.err.println("EXCEPTION: "+e.getMessage());
			}
		}
	}

	public void start(){
		thread.start();
	}
	
	public static void main(String[] args) {
		Mine mine = new Mine("LOCAL_PC",2,true);
		mine.start();
	}
}
