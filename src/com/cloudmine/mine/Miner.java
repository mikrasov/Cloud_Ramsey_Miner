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
	protected transient final int minUseful, maxUseful;
	
	private final UUID id = UUID.randomUUID();
	
	protected UUID taskId;
	protected String error;
	protected boolean running = false;
	protected boolean failedToFindSolution = false;
	
	public Miner(int minUseful, int maxUseful){
		this.maxUseful = maxUseful;
		this.minUseful = minUseful;
	}
	
	public void mine(Graph graph){
		this.current = graph;
		thread.start();
	}
	
	public void reset(){
		failedToFindSolution = false;
		running = false;
		current = null;
		taskId = null;
		error = null;
	}
	
	public void assign(Task task){
		if(!this.id.equals(task.getTargetMiner())) return;
		
		if(task.getSeed() == null){
			reset();
			return;
		}
		this.taskId = task.getTaskId();
		
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
		
		if(current.size()> minUseful)
			solutionQueue.add(new Solution(taskId,normalized));
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
	public UUID getTask() 		{ return taskId; }
	public boolean hasTask() 	{ return taskId != null; }
	public boolean failedToFindSolution(){ return failedToFindSolution; }

	public void process() throws Exception{
		
	}
}
