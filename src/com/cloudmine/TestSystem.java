package com.cloudmine;

import java.io.IOException;

import com.cloudmine.foreman.Foreman;
import com.cloudmine.mine.Mine;

public class TestSystem {

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("Starting Test");
		Foreman master = new Foreman(8080);
		   
	   System.out.println("Starting Foreman");
	   master.load();
	   
	  
	   master.start();
	   System.out.println("Started");
	   
	   Thread.sleep(1000);
	   System.out.println("Starting Test Mine");
	   Mine mine = new Mine(4);
		mine.start();
	}
}
