package com.cloudmine.mine;

import java.util.List;
import java.util.UUID;

import com.cloudmine.Graph;

public abstract class Miner implements Runnable{

	protected transient Thread thread = new Thread(this);
	protected transient Graph current;
	protected transient List<Solution> solutionQueue;
	
	private final UUID id = UUID.randomUUID();
	
	protected boolean running = false;
	protected int taskId = -1;
	protected int size = -1;
	
	public Miner(List<Solution> solutionQueue) {
		this.solutionQueue = solutionQueue;
	}
	
	public void mine(Graph seed, int taskId){
		if(seed == null){
			reset();
			return;
		}
		this.current = seed;
		this.taskId = taskId;
		running = true;
		thread.start();
	}
	
	public void reset(){
		running = false;
		current = null;
		size = -1;
		taskId = -1;
	}
	
	protected void sendSolution(boolean isSolved){
		solutionQueue.add(new Solution(taskId,current.encodeAsJsonValue(), current.size(), isSolved));
	}
}
