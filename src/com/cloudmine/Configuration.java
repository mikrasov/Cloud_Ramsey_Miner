package com.cloudmine;

import java.util.HashMap;
import java.util.Map;

public class Configuration {
	
	public static final int 	CONTROL_BOARD_PORT = 80;
	public static final int 	FOREMAN_PORT = 8080;
	public static final int 	FAST_CUTOFF = 50;
	public static final int 	SLOW_CUTOFF = 101;
	
	
	public static final String	VERSION = "0.07";
	
	public static final int 	LONG_REMORTING_INTERVAL = 10*60*1000;
	public static final int 	SHORT_REMORTING_INTERVAL = 1*60*1000;
	public static final String	FOREMAN_ADDRESS = "http://128.111.84.163";
	public static final int 	MIN_USEFULL_SOLUTION = 50;
	public static final int 	MAX_USEFULL_SOLUTION = 101;
	
	private static final Map<String, Configuration> configurations = new HashMap<>();
	static{
		//				  TYPE			Long	Cores	Interval					Foreman Address			Min						Max	
		new Configuration("LOCAL_PC",	true,	1,		5*1000,						"http://localhost",		40,						99);
        new Configuration("LOCAL_MAC",	true,	2,		5*1000,						"http://localhost", 	40,						99);
        new Configuration("AWS",		true,	1,		LONG_REMORTING_INTERVAL,	FOREMAN_ADDRESS,		MIN_USEFULL_SOLUTION,	MAX_USEFULL_SOLUTION);
        new Configuration("AWS-LARGE",	true,	2,		LONG_REMORTING_INTERVAL,	FOREMAN_ADDRESS,		MIN_USEFULL_SOLUTION,	MAX_USEFULL_SOLUTION);
        new Configuration("EUCALYPTUS",	true,	1,		LONG_REMORTING_INTERVAL,	FOREMAN_ADDRESS,		MIN_USEFULL_SOLUTION,	MAX_USEFULL_SOLUTION);
        new Configuration("CONDOR",		false,	1,		SHORT_REMORTING_INTERVAL,	FOREMAN_ADDRESS,		MIN_USEFULL_SOLUTION,	MAX_USEFULL_SOLUTION);
        new Configuration("APP-ENGINE",	true,	1,		SHORT_REMORTING_INTERVAL,	FOREMAN_ADDRESS,		MIN_USEFULL_SOLUTION,	MAX_USEFULL_SOLUTION);
	}
	
	protected final String type;
	protected final String version;
	protected final boolean longTerm;
	protected final int	numMiners;
	
	protected String additionalInfo ="";
	
	protected final transient int minUseful;
	protected final transient int maxUseful;
	protected final transient String foremanAdress;
	protected final transient int reportingInterval;

	public Configuration(String type, boolean longTerm, int numMiners, int reportingInterval, String foremanAdress, int minUseful, int maxUseful) {
		super();
		this.type = type;
		this.longTerm = longTerm;
		this.numMiners = numMiners;
		this.version = VERSION;
		this.foremanAdress = foremanAdress;
		this.reportingInterval = reportingInterval;
		this.minUseful = minUseful;
		this.maxUseful = maxUseful;
		
		configurations.put(type.toUpperCase(), this);
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

	public String getForemanAdress() {
		return foremanAdress+":"+FOREMAN_PORT;
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
	
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	
	public void setAdditionalInfo(String info) {
		additionalInfo = info;
	}

	@Override
	public String toString() {
		return 	"> Type:\t\t" + type + "\n" +
				"> Additional:\t" + additionalInfo + "\n" +
				"> Long Term:\t" + longTerm +  "\n" +
				"> Num Miners:\t" + numMiners +  "\n" +
				"> Min Useful:\t" + minUseful + "\n" +
				"> Max Useful:\t" + maxUseful +  "\n" +
				"> Foreman:\t" + getForemanAdress() +  "\n" +
				"> Reporting:\t" + reportingInterval+" ms";
	}

	
	public static Configuration get(String id){
		return configurations.get(id.toUpperCase());
	}

	public static String getUsage(){
		String out ="";
		for(String s: configurations.keySet())
			out += s+" | ";
		
		return out;
	}
}
