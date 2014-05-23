package com.cloudmine;

import java.util.UUID;

public class Task {

	private final UUID taskId;
	private final UUID targetMinerId;
	
	private Graph seed;
	
	private transient boolean failed = false;
	private transient long lastSeen=0;
	private transient long lastProgress=0;
	private transient long startedOn = System.currentTimeMillis();
	private transient Configuration targetMine;
	
	public Task(Configuration targetMine, UUID targetMiner, Graph seed) {
		this.taskId = UUID.randomUUID();
		this.targetMine = targetMine;
		this.targetMinerId = targetMiner;
		this.seed = seed;
	}
	
	public Task(UUID taskId, Configuration targetMine, UUID targetMiner) {
		this.taskId = taskId;
		this.targetMine = targetMine;
		this.targetMinerId = targetMiner;
		this.seed = null;
	}
	
	public UUID getTaskId(){
		return taskId;
	}
	
	public UUID getTargetMiner(){
		return targetMinerId;
	}
	
	public Configuration getTargetMine(){
		return targetMine;
	}

	public Graph getSeed(){
		return seed;
	}
	
	public void setLastSeen(long timeMillesecond){
		this.lastSeen = timeMillesecond;
	}
	
	
	public void justSeen(){
		lastSeen = System.currentTimeMillis();
	}
	
	public void generatedGraph(Graph derivatice){
		lastProgress = System.currentTimeMillis();
		this.seed = derivatice;
	}
	
	public long lastSeen(){
		return lastSeen;
	}
	
	public long startedOn(){
		return startedOn;
	}
	
	
	public long timeSinceLastSeen(){
		return System.currentTimeMillis() - lastSeen;
	}
	
	public long timeSinceLastProgress(){
		return System.currentTimeMillis() - lastProgress;
	}
	
	public void markFailed(){
		failed = true;
		seed.failedToFindSolution();
	}
	
	public int currentSize(){
		if(seed == null)
			return -1;
		else
			return seed.size();
	}
	
	public boolean hasFailed(){
		return failed;
	}

	@Override
	public String toString() {
		return "Task "+taskId +" ["+ (seed==null?"":seed.toString())+"]";
	}
}
