package com.cloudmine.ops;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.cloudmine.Task;

public class Operation {

	protected Bank bank = new Bank();
	protected Map<UUID, Task> map = new ConcurrentHashMap<>();
	
	protected Foreman foreman = new Foreman(bank, map);
	protected ControlBoard control = new ControlBoard(bank, map);

	public void load(){
		System.out.print("Trying to load from file '"+Bank.BANK_FILENAME+"' : ");
		try {
			bank.load();
			System.out.println("File loaded");
		} catch (ClassNotFoundException | IOException e) {
			System.out.println("Failed to Load file");
		}
	}
   
	public void start() throws IOException{
		foreman.start();
		control.start();
	}
   
	public static void main(String[] args) throws IOException {
		Operation op = new Operation();
		op.load();
		op.start();
	}
}
