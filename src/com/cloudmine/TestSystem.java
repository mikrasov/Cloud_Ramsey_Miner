package com.cloudmine;

import java.io.IOException;

import com.cloudmine.mine.Mine;
import com.cloudmine.ops.Operation;

public class TestSystem {

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("Starting Test");
		Operation.main(new String[]{});
		Thread.sleep(1000);
		System.out.println("Starting Test Mine");
		Mine.main(new String[]{"LOCAL_PC","192.168.1.1"});
	}
}
