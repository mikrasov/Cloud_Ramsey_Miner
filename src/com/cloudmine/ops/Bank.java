package com.cloudmine.ops;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.cloudmine.Graph;

public class Bank implements Serializable, Iterable<Graph>{

	private static final long serialVersionUID = 8867753203624856389L;

	public static final int GRAPH_LESS_THAN = 103;
	public static final int GRAPH_AT_LEAST = 8;
	public static final String BANK_FILENAME = "bank.save";
	public static final String BANK_TEMP_FILENAME = "bank.tmp";

	private File bankFile = new File(BANK_FILENAME);
	private File bankTempFile = new File(BANK_TEMP_FILENAME);

	@SuppressWarnings("unchecked")
	private transient List<Graph>[] hierarchy = new List[GRAPH_LESS_THAN];
	private ArrayList<Graph> list = new ArrayList<>();
	
	public Bank() {
		for(int i=0; i<hierarchy.length; i++){
			hierarchy[i]= new LinkedList<Graph>();
		}
	}

	public synchronized boolean put( Graph example){
		List<Graph> set = hierarchy[example.size()];
		
		//Do Isomorph check
		for(Graph a: set) if(a.isIsomorphOf(example)) return false;

		example.setId(list.size());
		list.add(example);
		hierarchy[example.size()].add(example);
		
		return true;
	}
	
	public synchronized List<Graph> getLevel(int level){
		return hierarchy[level];
	}
	
	public synchronized Graph getBest(int startingAt){
		if(startingAt > hierarchy.length-1) 
			startingAt = hierarchy.length-1;
		
		//Search for best starting point
		for(int size=startingAt; size>=GRAPH_AT_LEAST; size--){
			for(Graph g: hierarchy[size]){
				if(!g.isAssigned())
					return g;
			}
			
		}
		//could not find one, so make one
		Graph g = Graph.generateRandom(GRAPH_AT_LEAST);
	
		put(g);
		return g;
	}

	public int size(){
		return list.size();
	}
	
	public int numLevels(){
		return hierarchy.length;
	}
	
	public synchronized void save() throws IOException{
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(bankTempFile));
		out.writeObject(list);
		out.close();
		
		//Move temp to permanent
	    bankFile.delete();
	    bankTempFile.renameTo(bankFile);
	}
	
	@SuppressWarnings("unchecked")
	public void load() throws FileNotFoundException, IOException, ClassNotFoundException{
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(bankFile));
		
		list = (ArrayList<Graph>)in.readObject();
		
		for(Graph g: list)
			hierarchy[g.size()].add(g);
			
		in.close();
	}

	@Override
	public Iterator<Graph> iterator() {
		return list.iterator();
	}
	
}
