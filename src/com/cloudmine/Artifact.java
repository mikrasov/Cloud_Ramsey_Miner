package com.cloudmine;

import java.util.LinkedList;
import java.util.List;

public class Artifact extends Graph {

	private static final long serialVersionUID = -6991504613898888514L;

	protected List<Artifact> lowerOrder = new LinkedList<>();
	protected List<Artifact> higherOrder = new LinkedList<>();
	
	public Artifact(int size) {
		super(size);
	}
	
	public void addLowerOrder(Artifact lower){
		lowerOrder.add(lower);
	}

	public void addHigherOrder(Artifact higher){
		higherOrder.add(higher);
	}
	
	public List<Artifact> getHigherOrder(){
		return higherOrder;
	}
	
	public List<Artifact> getLowerOrder(){
		return lowerOrder;
	}
	
	public boolean isDeadEnd(){
		return false;
	}
}
