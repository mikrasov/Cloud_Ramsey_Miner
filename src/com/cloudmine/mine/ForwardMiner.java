package com.cloudmine.mine;

import java.util.List;

import com.cloudmine.Graph;
import com.cloudmine.TabooList;

public class ForwardMiner extends Miner{

	private static final int MIN_USEFULL_SOLUTION = 45;
	
	public ForwardMiner(List<Solution> solutionQueue) {
		super(solutionQueue);
	}

	public boolean findCounterExample(Graph g){
		TabooList taboo = new TabooList();
		
		while(true){
			//find out how we are doing
			int count = g.cliqueCount();
	
			//if we have a counter example
			if(count == 0) {
				//YAY FOUND IT!
				return true;
			}
	
			/*
			 * otherwise, we need to consider flipping an edge
			 *
			 * let's speculative flip each edge, record the new count,
			 * and unflip the edge.  We'll then remember the best flip and
			 * keep it next time around
			 *
			 * only need to work with upper triangle of matrix =>
			 * notice the indices
			 */
			
			int best_count = Integer.MAX_VALUE;
			int best_i=-1;
			int best_j=-1;
			for(int i=0; i < g.size(); i++)
			{
				for(int j=i+1; j < g.size(); j++)
				{
					// flip it
					g.flip(i,j);
					count = g.cliqueCount();
	
					if(count == 0){
						//YAY FOUND IT!
						return true;
					}
					
					// is it better and the i,j,count not taboo?
					if( count < best_count && !taboo.contains(i, j))
					{
						best_count = count;
						best_i = i;
						best_j = j;	
					}
	
					//flip it back
					g.flip(i,j);
					
				}
			}
	
			if(best_count == Integer.MAX_VALUE) {
				System.out.println("no best edge found");
				return false;
			}
			
			// keep the best flip we saw
			g.flip(best_i, best_j);
	
			/*
			 * taboo this graph configuration so that we don't visit
			 * it again
			 */
			count = g.cliqueCount();
			taboo.add(best_i,best_j);
	
			/*
			System.out.println("size: "+g.size()+"\t"
					+ "best_count: "+best_count+"\t"
					+ "best edge: ("+best_i+","+best_j+")\t"
					+ "color: "+ (g.get(best_i, best_j)?1:0)
					);
		*/
			// rinse and repeat
		}
		
	}

	@Override
	public void run() {
		
		boolean isSolved = false;
		do{
			size = current.size();
			isSolved = findCounterExample(current);
			
			//System.out.println(">>Counter-example found on "+current.size()+"!\n"+current);
			
			if(size> MIN_USEFULL_SOLUTION){
				sendSolution(isSolved);
			}
			
			current = current.extendRandom();
		}
		while(current.size() < 103);
		
		running = false;
	}

}
