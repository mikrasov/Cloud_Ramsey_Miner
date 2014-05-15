package com.cloudmine;

import java.util.UUID;

public class Task {

	private final UUID taskId = UUID.randomUUID();
	private final UUID targetMiner;
	private final Graph seed;
	
	private transient long lastSeen=0;
	
	public Task(UUID targetMiner, Graph seed) {
		this.targetMiner = targetMiner;
		this.seed = seed;
	}
	
	public UUID getTaskId(){
		return taskId;
	}
	
	public UUID getTargetMiner(){
		return targetMiner;
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
	
	public long lastSeen(){
		return lastSeen;
	}
	
	public long timeSinceLastSeen(){
		return System.currentTimeMillis() - lastSeen;
	}
	

	@Override
	public String toString() {
		return taskId.toString()+" ("+seed.size()+") - ID:"+seed.getId()+" Origin:"+seed.getOriginId();
	}
}
