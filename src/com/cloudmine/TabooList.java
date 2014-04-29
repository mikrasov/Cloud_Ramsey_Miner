package com.cloudmine;

import java.util.HashSet;

public class TabooList {

	private HashSet<Integer> list = new HashSet<>();

	public void add(int i, int j){
		list.add(hash(i,j));
	}

	public boolean contains(int i, int j){
		return list.contains(hash(i,j));
	}
	
	public int size(){
		return list.size();
	}
	
	protected int hash(int i, int j){
		return (i << 16) | (j & 0XFFFF);
	}
}
