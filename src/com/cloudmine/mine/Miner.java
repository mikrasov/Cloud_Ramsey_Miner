package com.cloudmine.mine;

import java.util.List;
import java.util.UUID;

import com.cloudmine.Graph;
import com.cloudmine.foreman.Task;

public abstract class Miner implements Runnable{

	protected transient Thread thread = new Thread(this);
	protected transient Graph current;
	protected transient List<Solution> solutionQueue;
	
	private final UUID id = UUID.randomUUID();
	
	protected boolean running = false;
	protected int size = -1;
	protected UUID task;
	
	public Miner(List<Solution> solutionQueue) {
		this.solutionQueue = solutionQueue;
	}
	
	public void mine(Graph graph){
		running = true;
		this.current = graph;
		thread.start();
	}
	
	public void reset(){
		running = false;
		current = null;
		size = -1;
		task = null;
	}
	
	public void assign(Task task){
		if(!this.id.equals(task.getTargetMiner())) return;
		
		if(task.getSeed() == null){
			reset();
			return;
		}
		this.task = task.getTaskId();
		
		mine(task.getSeed());
	}
	
	public UUID getId(){
		return id;
	}
	
	protected void sendSolution(boolean isSolved){
		solutionQueue.add(new Solution(task,current.encodeAsJsonValue(), current.size(), isSolved));
	}
}
