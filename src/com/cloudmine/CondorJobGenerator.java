package com.cloudmine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CondorJobGenerator {

	public static void main(String[] args) throws IOException {
		
		FileWriter writer = new FileWriter(new File("cloudmine.sub"));
		
		writer.write("  ####################\n");
		writer.write("  #\n");
		writer.write("  # Mine \n");
		writer.write("  # Executes Mines on Condor\n");
		writer.write("  #\n");
		writer.write("  ####################\n\n");
		
		writer.write("  universe = java\n");
		writer.write("  executable =  Mine.jar\n");
		writer.write("  jar_files = Mine.jar\n\n");
		
		for(int i=1; i<=50; i++){
			writer.write("  arguments = com.cloudmine.mine.Mine CONDOR condor"+i+"\n");
			writer.write("  output = condor"+i+".out\n");
			writer.write("  error = condor"+i+".err\n");
			writer.write("  log = condor"+i+".log\n");
			writer.write("  queue\n\n");
		}
		writer.close();
	}

}
