package com.cloudmine.mine;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.cloudmine.Configuration;
import com.cloudmine.Task;
import com.cloudmine.http.AppClient;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Mine implements Runnable{

	
	protected final Configuration configuration;
	
	protected List<Solution> solutionsQueue = new LinkedList<>();
	
	protected transient AppClient master;;
	protected transient Gson gson = new Gson();
	protected transient JsonParser jparse = new JsonParser();
	protected transient Thread thread = new Thread(this);
	protected transient int timeSinceLastPost = 0;
	
	protected  Miner[] miners;
	
	public Mine(Configuration config) {
		this.configuration = config;
		master = new AppClient(config.getForemanAdress());
		
		miners = new Miner[config.getNumMiners()];
		for(int i=0; i < miners.length; i++)
			miners[i] = new ForwardMiner(config.getMinUseful(), config.getMaxUseful());
	}
	
	protected void contactServer() throws IOException{
		String json = gson.toJson(this);		
		String responce = master.post(json);
		JsonArray jTasks = jparse.parse(responce).getAsJsonArray();
		
		for(JsonElement t: jTasks){
			Task task = gson.fromJson(t, Task.class);
		
			System.out.println("New Task "+task);
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
	
	public int numMiners(){
		return miners.length;
	}
	
	@Override
	public void run() {
		while(true){		
			try {
				pollMiners();
				contactServer();
			} catch (Exception e) {
				System.err.println("EXCEPTION: "+e.getMessage());
				e.printStackTrace();
			}
			
			try {
				Thread.sleep(configuration.getReportingInterval());
			} catch (InterruptedException e) {}
		}
	}

	public void start(){
		thread.start();
	}
	
	public Configuration getConfiguration(){
		return configuration;
	}
	
	public static void main(String[] args) {
		Configuration config = null;
		
		if(args.length >= 1)
			config = Configuration.get(args[0]);
		
		if(config == null){
			System.out.println("Usage Mine ["+Configuration.getUsage()+"] {IP}");
			System.exit(0);
		}
		
		if(args.length >= 2)
			config.setAdditionalInfo(args[1]);
		
		Mine mine = new Mine(config);
		System.out.println("Starting Mine (V."+config.getVersion()+")");
		System.out.println(config);
		
		mine.start();
		
		System.out.println("---------------------------------\n");
	}
	
}
