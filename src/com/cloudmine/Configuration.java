package com.cloudmine;

public class Configuration {

	
	public static final String	VERSION = "0.1";
	public static final int 	LONG_REMORTING_INTERVAL = 10*60*1000;
	public static final int 	SHORT_REMORTING_INTERVAL = 1*60*1000;
	public static final String	FOREMAN_ADDRESS = "http://128.111.84.163:8080";
	public static final int 	MIN_USEFULL_SOLUTION = 50;
	public static final int 	MAX_USEFULL_SOLUTION = 101;
	
	public static final Configuration LOCAL 	 = new Configuration("LOCAL_PC",true,2, 5*1000,"http://localhost:8080",40,99);
	public static final Configuration AWS 		 = new Configuration("AWS",true,1,LONG_REMORTING_INTERVAL,FOREMAN_ADDRESS,MIN_USEFULL_SOLUTION,MAX_USEFULL_SOLUTION);
	public static final Configuration AWS_BIG	 = new Configuration("AWS-BIG",true,2,LONG_REMORTING_INTERVAL,FOREMAN_ADDRESS,MIN_USEFULL_SOLUTION,MAX_USEFULL_SOLUTION);
	public static final Configuration EUCALYPTUS = new Configuration("EUCALYPTUS",true,1,LONG_REMORTING_INTERVAL,FOREMAN_ADDRESS,MIN_USEFULL_SOLUTION,MAX_USEFULL_SOLUTION);
	public static final Configuration CONDOR 	 = new Configuration("CONDOR",false,1,SHORT_REMORTING_INTERVAL,FOREMAN_ADDRESS,MIN_USEFULL_SOLUTION,MAX_USEFULL_SOLUTION);
	
	
	//CHANGE ME
	public static final Configuration TARGET_PLATFORM = EUCALYPTUS;
	
	
	protected final String type;
	protected final String version;
	protected final boolean longTerm;
	protected final int	numMiners;
	
	protected final transient int minUseful;
	protected final transient int maxUseful;
	protected final transient String foreman;
	protected final transient int reportingInterval;

	public Configuration(String type, boolean longTerm, int numMiners, int reportingInterval, String foreman, int minUseful, int maxUseful) {
		super();
		this.type = type;
		this.longTerm = longTerm;
		this.numMiners = numMiners;
		this.version = VERSION;
		this.foreman = foreman;
		this.reportingInterval = reportingInterval;
		this.minUseful = minUseful;
		this.maxUseful = maxUseful;
	}

	public String getType() {
		return type;
	}

	public boolean isLongTerm() {
		return longTerm;
	}

	public int getNumMiners() {
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

	public int getMinUseful() {
		return minUseful;
	}

	public int getMaxUseful() {
		return maxUseful;
	}

	@Override
	public String toString() {
		return 	"> Type:\t\t" + type + "\n" +
				"> Long Term:\t" + longTerm +  "\n" +
				"> Num Miners:\t" + numMiners +  "\n" +
				"> Min Useful:\t" + minUseful + "\n" +
				"> Max Useful:\t" + maxUseful +  "\n" +
				"> Foreman:\t" + foreman +  "\n" +
				"> Reporting:\t" + reportingInterval+" ms";
	}

	
	
}
