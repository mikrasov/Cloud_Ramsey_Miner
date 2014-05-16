package com.cloudmine;

import java.util.UUID;

import com.cloudmine.mine.Mine;
import com.cloudmine.mine.Miner;

public class Task {

	private final UUID taskId = UUID.randomUUID();
	private final UUID targetMinerId;
	private final Graph seed;
	
	private transient long lastSeen=0;
	private transient Configuration targetMine;
	
	public Task(Configuration targetMine, UUID targetMiner, Graph seed) {
		this.targetMine = targetMine;
		this.targetMinerId = targetMiner;
		this.seed = seed;
	}
	
	public UUID getTaskId(){
		return taskId;
	}
	
	public UUID getTargetMiner(){
		return targetMinerId;
	}
	
	public Configuration getTargetMine(){
		return targetMine;
	}

	public Graph getSeed(){
		return seed;
	}
	
	public void setLastSeen(long timeMillesecond){
		this.lastSeen = timeMillesecond;
	}
	
	
	public void justSeen(){
		lastSeen = System.currentTimeMillis();
	}
	
	public long lastSeen(){
		return lastSeen;
	}
	
	public long timeSinceLastSeen(){
		return System.currentTimeMillis() - lastSeen;
	}
	

	@Override
	public String toString() {
		return taskId.toString()+" ("+seed.size()+") - ID:"+seed.getId()+" Origin:"+seed.getOriginId();
	}
}
