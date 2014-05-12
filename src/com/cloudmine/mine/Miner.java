package com.cloudmine.mine;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.cloudmine.Graph;
import com.cloudmine.Task;

public abstract class Miner implements Runnable{

	protected transient Thread thread = new Thread(this);
	protected transient Graph current;
	protected transient Queue<Solution> solutionQueue = new ConcurrentLinkedQueue<>();
	
	private final UUID id = UUID.randomUUID();
	
	protected String error;
	protected boolean running = false;
	protected int size = -1;
	protected UUID task;
	
	public void mine(Graph graph){
		this.current = graph;
		thread.start();
	}
	
	public void reset(){
		running = false;
		current = null;
		size = -1;
		task = null;
		error = null;
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
	
	public Solution poll(){
		return solutionQueue.poll();
	}
	
	public UUID getId(){
		return id;
	}
	
	protected void sendSolution(boolean isSolved){
		Graph normalized = current.normalize();
		solutionQueue.add(new Solution(task,normalized.encodeAsJsonValue(), current.size(), isSolved));
	}
	
	@Override
	public void run() {
		running = true;
		
		try {
			process();
		} catch (Exception e) {
			error = e.getMessage();
		}
		running = false;
	}
	
	public abstract void process() throws Exception;
}
