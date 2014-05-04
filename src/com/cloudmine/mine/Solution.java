package com.cloudmine.mine;

public class Solution {
	
	private final int task_id;
	private final String graph;
	private final int size;
	private final boolean solved;
	
	public Solution(int task_id, String graph, int size, boolean solved) {
		super();
		this.task_id = task_id;
		this.size = size;
		this.graph = graph;
		this.solved = solved;
	}

	public int getTask_id() {
		return task_id;
	}

	public int getSize() {
		return size;
	}

	public String getGraph() {
		return graph;
	}
	
	public boolean isSolved(){
		return solved;
	}
}
