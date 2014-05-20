package com.cloudmine.mine;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.cloudmine.Graph;
import com.cloudmine.Task;

public class Miner implements Runnable{

	protected transient Thread thread = new Thread(this);
	protected transient Graph current;
	protected transient Queue<Solution> solutionQueue = new ConcurrentLinkedQueue<>();
	
	private final UUID id = UUID.randomUUID();
	
	protected String error;
	protected boolean running = false;
	protected boolean failedToFindSolution = false;
	protected int size = -1;
	protected UUID task;
	
	public void mine(Graph graph){
		this.current = graph;
		thread.start();
	}
	
	public void reset(){
		failedToFindSolution = false;
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
		normalized.setSolved(isSolved);
		solutionQueue.add(new Solution(task,normalized));
		//System.out.println(" # SOLUTION FOUND " + normalized);
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
		
	public String getError() 	{ return error; }
	public boolean isRunning()	{ return running; }
	public int getTaskSize() 	{ return size; }
	public UUID getTask() 		{ return task; }
	public boolean hasTask() 	{ return task != null; }
	public boolean failedToFindSolution(){ return failedToFindSolution; }

	public void process() throws Exception{
		
	}
}
