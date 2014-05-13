package com.cloudmine.mine;

import java.util.UUID;

import com.cloudmine.Graph;

public class Solution {
	
	private final UUID taskId;
	private final String graph;
	private final int size;
	private final boolean solved;
	private final int originId;
	
	public Solution(UUID taskId, String graph, int size, boolean solved, int originId) {
		super();
		this.taskId = taskId;
		this.graph = graph;
		this.size = size;
		this.originId = originId;
		this.solved = solved;
	}
	
	public Solution(UUID taskId, Graph g) {
		this(taskId, g.encodeAsJsonValue(), g.size(), g.isSolved(), g.getOriginId());
	}

	public UUID getTaskId() {
		return taskId;
	}

	public int getSize() {
		return size;
	}

	public int getOriginId() {
		return originId;
	}
	
	public String getGraph() {
		return graph;
	}
	
	public boolean isSolved(){
		return solved;
	}
	
	public Graph convertToGraph(){
		Graph g = new Graph(size, graph,originId,solved);
		return g;
	}
	
	@Override
	public String toString() {
		return graph;
	}
}
