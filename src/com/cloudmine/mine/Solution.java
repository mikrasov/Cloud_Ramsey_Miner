package com.cloudmine.mine;

import java.util.UUID;

import com.cloudmine.Graph;

public class Solution {
	
	private final UUID taskId;
	private final Graph graph;
	
	public Solution(UUID taskId, Graph graph) {
		super();
		this.taskId = taskId;
		this.graph = graph;
		this.graph.setSolved(true);
	}
	
	public UUID getTaskId() {
		return taskId;
	}
	
	public Graph getGraph() {
		return graph;
	}
		
	@Override
	public String toString() {
		return graph.toString();
	}
}
