package com.mikrasov.cloudmine;

import java.io.Serializable;
import java.util.TreeSet;

public class Bank implements Serializable{

	private static final long serialVersionUID = 8867753203624856389L;

	@SuppressWarnings("unchecked")
	private TreeSet<Artifact>[] bank = new TreeSet[102];
	
	public Bank() {
		for(int i=0; i<bank.length; i++){
			bank[i]= new TreeSet<Artifact>();
		}
	}

	public boolean put( Artifact example){
		TreeSet<Artifact> set = bank[example.size()];
		
		//Do Isomorph check
		for(Artifact a: set) if(a.isIsomorphOf(example)) return false;

		bank[example.size()].add(example);
		return true;
	}
	
	public TreeSet<Artifact> get(int level){
		return bank[level];
	}
}
