package com.cloudmine;

public class Configuration {

	public final static transient String VERSION = "0.1";
	
	protected final String type;
	protected final String version;
	protected final boolean longTerm;
	protected final int	numMiners;

	public Configuration(String type, boolean longTerm, int numMiners) {
		super();
		this.type = type;
		this.longTerm = longTerm;
		this.numMiners = numMiners;
		this.version = VERSION;
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
