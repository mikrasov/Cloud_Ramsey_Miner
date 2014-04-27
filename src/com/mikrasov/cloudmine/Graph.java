package com.mikrasov.cloudmine;

import java.util.BitSet;

/**
 * Based on BitMatrix
 * @author Timothy A. Budd
 * @version 1.1 September 1999
 * @see jds.Collection
 */

public class Graph {
	private BitSet [ ] rows;
	private static final int SUB_GRAPH_SIZE = 6;
	
	private int size; 
	public Graph (int size) {
		this.size = size;
		rows = new BitSet[size];
		for (int i = 0; i < size; i++)
			rows[i] = new  BitSet(size);
	}


	public boolean get(int i, int j) { return rows[i].get(j); }

	public void set(int i, int j, boolean value) { 
		if(value)
			rows[i].set(j);
		else
			rows[i].clear(j);
	}
	
	public void flip(int i, int j) { 
		rows[i].flip(j);
	}
	
	public Graph extend(){
		int newSize = size +1;
		Graph graph = new Graph(newSize);
		
		for(int i=0; i < newSize; i++) {
			for(int j=0; j < newSize; j++) {
				graph.set(i, j, get(i,j));
			}
		}
		
		return graph;
	}
	
	
	public int size(){
		return size;
	}
	
	public int cliqueCount() {
	    int count=0;
	    
	    for(int i=0;i < size-SUB_GRAPH_SIZE+1; i++) {
	    	for(int j=i+1;j < size-SUB_GRAPH_SIZE+2; j++) {
	    		
	    		boolean value_i_j = get(i,j);
	    		
	    		for(int k=j+1;k < size-SUB_GRAPH_SIZE+3; k++) { 
	    			if( value_i_j == get(i,k)  && 
	    				value_i_j == get(j,k) )
	    			{
	    				for(int l=k+1;l < size-SUB_GRAPH_SIZE+4; l++) { 
							if(value_i_j == get(i,l) && 
							   value_i_j == get(j,l) && 
							   value_i_j == get(k,l) )
							{
								for(int m=l+1;m < size-SUB_GRAPH_SIZE+5; m++)  {
									if(value_i_j == get(i,m) && 
									   value_i_j == get(j,m) &&
									   value_i_j == get(k,m) && 
									   value_i_j == get(l,m) )
									{
									  for(int n=m+1; n< size-SUB_GRAPH_SIZE+6;n++) {
										  if( value_i_j == get(i,n) && 
										      value_i_j == get(j,n) &&
										      value_i_j == get(k,n) && 
										      value_i_j == get(l,n) &&
										      value_i_j == get(m,n) )
										  {
											  count++;
										  }
									  }
									}
								}
							}
	    				}
	    			}
	    		}
	         }
	     }
	    return count;
	}

	public String asJSON(){
		String out ="";
		for(int i=0; i < size; i++) {
			for(int j=0; j < size; j++) {
				out+=  get(i,j)?1:0 +" ";
			}
		}
		return out;
	}
	
	@Override
	public String toString() {		
		String out ="";
		for(int i=0; i < size; i++) {
			for(int j=0; j < size; j++)
			{
				out+=  get(i,j)?1:0 +" ";
			}
			out += "\n";
		}
		return out;
	}
}