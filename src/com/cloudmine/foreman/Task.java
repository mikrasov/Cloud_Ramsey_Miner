package com.cloudmine.foreman;

import java.util.UUID;

import com.cloudmine.Graph;

public class Task {

	private final UUID taskId = UUID.randomUUID();
	private final UUID targetMiner;
	private final Graph seed;
	
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

	@Override
	public String toString() {
		return taskId.toString();
	}
}
