package com.mikrasov.cloudmine.mine;

import com.mikrasov.cloudmine.http.AppClient;
import com.mikrasov.cloudmine.http.AppServer;

public abstract class Miner extends Thread{

	private AppClient client = new AppClient(AppServer.SERVER_IP);
	
	public Miner() {
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void run() {
		
	}
	
	
	

}
