package com.cloudmine;

import java.util.UUID;

import com.cloudmine.Graph;

public class Task {

	private final UUID taskId = UUID.randomUUID();
	private final UUID targetMiner;
	private final Graph seed;
	
	private long lastSeen = System.currentTimeMillis();
	
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
		return new Graph(seed);
	}
	
	public void setLastSeen(long timeMillesecond){
		this.lastSeen = timeMillesecond;
	}
	
	public long lastSeen(){
		return lastSeen;
	}
	
	public long timeSinceLastSeen(){
		return System.currentTimeMillis() - lastSeen;
	}

	@Override
	public String toString() {
		return taskId.toString();
	}
}
