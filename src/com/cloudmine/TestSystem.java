package com.cloudmine;

import java.io.IOException;

import com.cloudmine.foreman.Operation;
import com.cloudmine.mine.Mine;

public class TestSystem {

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("Starting Test");
		Operation.main(args);
		Thread.sleep(1000);
		System.out.println("Starting Test Mine");
		Mine.main(args);
	}
}
