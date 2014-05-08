package com.cloudmine.foreman;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import com.cloudmine.Bank;
import com.cloudmine.http.AppServer;

public class ControlBoard extends AppServer {

	public static final int DEAFAULT_PORT = 80;
	
	protected Bank bank = new Bank();
	protected Map<UUID, Task> map = new TreeMap<>();
	
	public ControlBoard(Bank bank, Map<UUID, Task> map) {
		super(DEAFAULT_PORT, AppServer.CONTENT_HTML);
		this.bank = bank;
		this.map = map;
	}

	@Override
	public String process(String request) throws IOException {
		String out = "<html>\n";
		
		out += genBank();
		out += genTaskList();
		
		return out+"</html>";
	}

	private String genBank(){
		String out = "<h1>Bank:</h1>";
		out += "<table width='100%'>\n";
		out += "<tr>";
		
		int n = 0;
		for(int i=0;i <bank.numLevels(); i++){
			
			out += "<td align='right'><strong>"+i+":</strong></td>";
			
			int sz = bank.get(i).size();
			out += "<td align='right'>"+(sz>0?sz:"-")+"</td>";
			
			
			n++;
			if(n > 10){
				n = 0;
				out += "</tr>\n<tr>";
			}
		}
		out += "</tr>\n";
		out += "</table>\n";
		return out;
	}
	
	private String genTaskList(){
		String out = "<h1>Tasks:</h1>";
		out += "<table width='100%'>\n";
		out += "<tr><td>Task ID</td></tr>";
		for(UUID k: map.keySet()){
			out += "<tr>";
			Task t = map.get(k);			
			out += "<td align='right'><strong>"+t.getTaskId()+":</strong></td>";
			out += "</tr>\n";
		}
		out += "</table>\n";
		return out;
	}
	
}
