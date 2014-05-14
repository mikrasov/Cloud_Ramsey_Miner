package com.cloudmine;

public class Configuration {

	public static final String	VERSION = "0.1";
	public static final int 	DEAFULT_REMORTING_INTERVAL = 5*1000;
	public static final String	FOREMAN_ADDRESS = "http://128.111.84.163:8080";
	
	public static final Configuration LOCAL 	 = new Configuration("LOCAL_PC",true,2,DEAFULT_REMORTING_INTERVAL,"http://localhost:8080");
	public static final Configuration AWS 		 = new Configuration("AWS",true,1,DEAFULT_REMORTING_INTERVAL,FOREMAN_ADDRESS);
	public static final Configuration AWS_BIG	 = new Configuration("AWS-BIG",true,2,DEAFULT_REMORTING_INTERVAL,FOREMAN_ADDRESS);
	public static final Configuration EUCALYPTUS = new Configuration("EUCALYPTUS",true,1,DEAFULT_REMORTING_INTERVAL,FOREMAN_ADDRESS);
	public static final Configuration CONDOR 	 = new Configuration("CONDOR",false,1,DEAFULT_REMORTING_INTERVAL,FOREMAN_ADDRESS);
	
	protected final String type;
	protected final String version;
	protected final boolean longTerm;
	protected final int	numMiners;
	
	protected final transient String foreman;
	protected final transient int reportingInterval;

	public Configuration(String type, boolean longTerm, int numMiners, int reportingInterval, String foreman) {
		super();
		this.type = type;
		this.longTerm = longTerm;
		this.numMiners = numMiners;
		this.version = VERSION;
		this.foreman = foreman;
		this.reportingInterval = reportingInterval;
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

	public String getVersion() {
		return version;
	}

	public String getForeman() {
		return foreman;
	}

	public int getReportingInterval() {
		return reportingInterval;
	}

}
