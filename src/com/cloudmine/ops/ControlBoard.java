package com.cloudmine.ops;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import com.cloudmine.Configuration;
import com.cloudmine.Graph;
import com.cloudmine.Task;
import com.cloudmine.http.AppServer;

public class ControlBoard extends AppServer {

	protected Bank bank = new Bank();
	protected Map<UUID, Task> map = new TreeMap<>();
	
	public ControlBoard(Bank bank, Map<UUID, Task> map) {
		super(Configuration.CONTROL_BOARD_PORT, AppServer.CONTENT_HTML);
		this.bank = bank;
		this.map = map;
	}

	@Override
	public String process(String request) throws IOException {
		String out = "<html>\n";
		
		out += genStats();
		out += genBank();
		out += genTaskList();
		out += genSolutions();
		
		return out+"</html>";
	}

	private String genStats(){
		String out = "<h1>Stats :</h1>";
		out += "<ul>Version: "+Configuration.VERSION+"</ul>";
		out += "<ul>Bank Size: "+bank.size()+"</ul>";
		return out;
	}
	
	
	private String genBank(){
		String out = "<h1>Bank:</h1>";
		out += "<table width='100%'>\n";
		out += "<tr>";
		
		int n = 0;
		for(int i=0;i <bank.numLevels(); i++){
			
			out += "<td align='right'><strong>"+i+":</strong></td>";
			
			int sz = bank.getLevel(i).size();
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
	
	private String genSolutions(){
		String out = "<h1>Solutions:</h1>";
		out += "<table width='100%'>\n";
		out += "<tr>";
		out += "<td><strong>ID</strong></td>";
		out += "<td><strong>Origin</strong></td>";
		out += "<td><strong>Size</strong></td>";
		out += "<td><strong>Assigned</strong></td>";
		out += "<td><strong>Times Assigned</strong></td>";
		out += "<td><strong>Solved</strong></td>";
		out += "</tr>\n";
		
		for(Graph g: bank){
			if(g.size() <40) continue;
			out += "<tr>";
			out += "<td>"+g.getId()+"</td>";
			out += "<td>"+g.getOriginId()+"</td>";
			out += "<td>"+g.size()+"</td>";
			out += "<td>"+(g.isAssigned()?"T":"F")+"</td>";
			out += "<td>"+g.timesAssigned()+"</td>";
			out += "<td>"+g.isSolved()+"</td>";
			out += "</tr>\n";
		}
		out += "</table>\n";
		return out;
	}
	
	private String genTaskList(){
		String out = "<h1>Tasks:</h1>";
		out += "<table width='100%'>\n";
		out += "<tr>";
		out += "<td><strong>Task ID</strong></td>";
		out += "<td><strong>LastSeen</strong></td>";
		out += "<td><strong>Type</strong></td>";
		out += "<td><strong>Additional</strong></td>";
		out += "<td><strong>Version</strong></td>";
		out += "</tr>";
		
		for(Task t: map.values()){
			out += "<tr>";
			Configuration c = t.getTargetMine();
			out += "<td>"+t.getTaskId()+":</td>";
			out += "<td>"+ago(t.timeSinceLastSeen())+"</td>";
			out += "<td>"+c.getType()+"</td>";
			out += "<td>"+c.getAdditionalInfo()+"</td>";
			out += "<td>"+c.getVersion()+"</td>";
			out += "</tr>\n";
		}
		out += "</table>\n";
		return out;
	}
	
	private String ago(long timeDiff){
		long seconds = timeDiff / 1000 % 60;
		long minutes = timeDiff / (60 * 1000) % 60;
		long hours = timeDiff / (60 * 60 * 1000) % 24;
		long days = timeDiff / (24 * 60 * 60 * 1000);

		if(days > 16000 )
			return "never";
		else if(days > 0)
			return days + " days ago";
		else if(hours > 0)
			return hours + " hours ago";
		else if(minutes > 0)
			return minutes + " min ago";
		else
			return seconds + " sec ago";
	}
	
}
