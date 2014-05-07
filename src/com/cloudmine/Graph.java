package com.cloudmine;

import java.io.Serializable;
import java.util.BitSet;
import java.util.Random;

public class Graph implements Comparable<Graph>, Serializable{
	
	private static final long serialVersionUID = 5253025260499448857L;
	
	private static final int SUB_GRAPH_SIZE = 6;
	private static final Random rnd = new Random();
	
	private BitSet matrix;
	private int size; 
		
	/**
	 * Create new matrix based Graph representation (zero filled)
	 * @param size of graph to create
	 */
	public Graph (int size) {
		this.size = size;
		matrix = new BitSet(size*size);
	}
	
	/**
	 * Create new matrix based representations, based on existing graph
	 * @param size of graph to create
	 * @param graph represented as a single line binary string sequence
	 */
	public Graph (int size, String graph) {
		this(size);
		for(int i=0; i <graph.length(); i++){
			if(graph.charAt(i) == '1')
				matrix.set(i);	
		}
	}
	
	/**
	 * Get graph size
	 * @return number of nodes in graph
	 */
	public int size(){
		return size;
	}

	/**
	 * Get value of matrix represented edge
	 * @param row 
	 * @param col
	 * @return value
	 */
	public boolean get(int row, int col) { 
		return matrix.get( (size*row)+col);
	}

	/**
	 * Set value of edge
	 * @param row
	 * @param col
	 * @param value
	 */
	public void set(int row, int col, boolean value) { 
		if(value)
			matrix.set( (size*row)+col);
		else
			matrix.clear( (size*row)+col);
	}
	
	/**
	 * Flip value of edge
	 * @param row
	 * @param col
	 */
	public void flip(int row, int col) { 
		matrix.flip( (size*row)+col);
	}
	
	/**
	 * Flip a random edge (in the upper triangle)
	 */
	public void flipRandom(){
		int row = rnd.nextInt(size-1);
		int col = row + 1+ rnd.nextInt(size-row-1);
		flip(row,col);
	}
	
	/**
	 * Get clique count
	 * @return clique count
	 */
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
	
	/**
	 * Extend the graph by adding a node (increasing the size by 1).
	 * The new edges are zero filled.
	 * @return extended graph
	 */
	public Graph extend(){
		int newSize = size +1;
		Graph graph = new Graph(newSize);
		
		for(int row=0; row < size; row++) {
			for(int col=0; col < size; col++) {
				graph.set(row, col, get(row,col));
			}
		}
		
		return graph;
	}
	
	/**
	 * Extend the graph by adding a node (increasing the size by 1).
	 * The new edges are filled with random values.
	 * @return extended graph
	 */
	public Graph extendRandom(){
		Graph graph = extend();
		
		for(int row=0; row < graph.size()-1; row++) {
			if(rnd.nextBoolean()) 
				graph.set(row, graph.size()-1, true);
		}
		return graph;
	}
	
	/**
	 * Mirror upper triangle to lower triangle
	 * @return mirrored graph
	 */
	public Graph mirror(){
		Graph graph = new Graph(size);
		
		for(int row=0; row < size; row++) {
			for(int col=row+1; col < size; col++) {
				if(this.get(row, col)) {
					graph.set(row,col, true);
					graph.set(col,row, true);
				}
			}
		}
		return graph;
	}
	
	/**
	 * Generate random graph as upper triangular matrix
	 * @param size of graph to generate
	 * @return generated graph
	 */
	public static Graph generateRandom(int size){
		Graph g = new Graph(size);
		
		for(int row=0; row < size; row++) {
			for(int col=row+1; col < size; col++) {
				if(rnd.nextBoolean()) 
					g.set(row,col, true);
			}
		}
		return g;
	}

	/**
	 * Checks to see if this graph is IsoMorph of another graph
	 * @param o graph to check against
	 * @return true if isomorph
	 */
	public boolean isIsomorphOf(Graph o){
		//METHOD STUB
		return this.equals(o);
	}
	
	/**
	 * Mirror upper triangular matrix, and encode it as a string
	 * @return string
	 */
	public String encodeAsJsonValue(){
		Graph graph = this.mirror();
		
		String out ="";
		for(int i=0; i < size*size; i++) {
			out+=  graph.matrix.get(i)?1:0 ;
		}
		return out;
	}
	
	@Override
	public String toString() {		
		String out ="";
		for(int row=0; row < size; row++) {
			for(int col=0; col < size; col++)
			{
				out+=  (get(row,col)?1:0) +" ";
			}
			out += "\n";
		}
		return out;
	}
	
	@Override
	public boolean equals(Object obj) {
		return matrix.equals( ((Graph)obj).matrix );
	}

	@Override
	public int compareTo(Graph o) {
		BitSet a = this.matrix;
		BitSet b = o.matrix;
		
		if(a.equals(b)) {
            return 0;
        } else if(a.length() > b.length()) {
            return 1;
        } else if(b.length() > a.length()) {
            return -1;
        } else {
            for(int i = 0; i < a.length(); i++) {
               if(a.get(i) != b.get(i)) {
                   if(a.get(i)) {
                      return 1;
                   } else {
                      return -1;
                   }
                }
             }
             return 0;
         }
	}
	
}