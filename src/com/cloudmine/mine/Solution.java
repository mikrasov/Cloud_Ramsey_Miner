package com.cloudmine.mine;

import java.util.UUID;

import com.cloudmine.Graph;

public class Solution {
	
	private final UUID taskId;
	private final String graph;
	private final int size;
	private final boolean solved;
	
	public Solution(UUID taskId, String graph, int size, boolean solved) {
		super();
		this.taskId = taskId;
		this.size = size;
		this.graph = graph;
		this.solved = solved;
	}

	public UUID getTaskId() {
		return taskId;
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
	
	public Graph convertToGraph(){
		return new Graph(size, graph);
	}
	
	@Override
	public String toString() {
		return graph;
	}
}
