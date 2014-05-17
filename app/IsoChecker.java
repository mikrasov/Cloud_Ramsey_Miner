package com.cloudmine;

import java.util.Comparator;
import java.util.List;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Comparison;


public class IsoChecker {

	private class Vertex{	
		public int key;
		public List<Vertex> value;
		
		public Vertex(int count){
			this(count, null);
		}
		
		public Vertex(int count, List<Vertex> value){
			this.key = count;
			this.value = value;
		}
	}
	
	private final Comparator<Vertex> vertexComparator = new Comparator<IsoChecker.Vertex>() {
		@Override
		public int compare(Vertex o1, Vertex o2) {
			int a = o1.key;
			int b = o2.key;
			
			if(a == b) {
	            return 0;
	        } else if(a > b) {
	            return 1;
	        } else {
	            return -1;
	        }
	    } 
		
	};
	
	private class VertexComparator implements Comparator<Vertex> {

		@Override
		public int compare(Vertex o1, Vertex o2) {
			int a = o1.key;
			int b = o2.key;
			
			if(a == b) {
	            return 0;
	        } else if(a > b) {
	            return 1;
	        } else {
	            return -1;
	        }
		}
		
	}
	
	private class VertexList extends SortedList<VertexComparator>{


		
	}
	
	private VertexList makeListL2(Graph graph){
		VertexList list = new VertexList();
		for(int i=0; i< graph.size(); i++){
			VertexList minor = new VertexList();
			for(int j=0; j< graph.size(); j++){
				
				if(j==i) continue;
				
				//Count only edges set to 1
				if( !(i<j && graph.get(i, j)) || !( i>j && graph.get(j, i)))
					continue;
				
				minor.add(new Vertex(graph.countNeighbors(j)));
			}
			minor.sort();
			list.add(new Vertex(graph.countNeighbors(i), minor));
		}
		list.sort();
		return list;
	}
	
	private boolean compareL2(VertexList l1, VertexList l2){
		
		int count1;
		int count2;
		int count3;
		int okay;
		Vertex j1;
		Vertex j2;
		VertexList j3;

		VertexList m1;
		VertexList m2;
		VertexList m3;

		VertexList p1;
		VertexList p2;
		VertexList p3;
		
		
		return false;
	}
	
	public boolean isIso(Graph g1, Graph g2){
		
		VertexList l1 = makeListL2(g1);
		VertexList l2 = makeListL2(g2);
		return true;
	}

}
