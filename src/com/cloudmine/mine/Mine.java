package com.cloudmine.mine;

import java.util.ArrayList;
import java.util.List;

public class Mine extends Thread{

	public static final int EXPECTED_NUMBER_MINERS = 4;
	
	protected List<Miner> miners = new ArrayList<>(EXPECTED_NUMBER_MINERS);
	
	public Mine() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void run() {
		
	}

}
