package com.cloudmine;

public class Configuration {

	protected final String type;
	protected final boolean longTerm;
	protected final int	numMiners;

	public Configuration(String type, boolean longTerm, int numMiners) {
		super();
		this.type = type;
		this.longTerm = longTerm;
		this.numMiners = numMiners;
	}

	public String getType() {
		return type;
	}

	public boolean isLongTerm() {
		return longTerm;
	}

	public int getNumCores() {
		return numMiners;
	}
	
	
	
	

}
