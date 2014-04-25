package com.mikrasov.cloudmine;

public class Miner {
	
	public Miner() {
		// TODO Auto-generated constructor stub
	}

	public void graph(){

		//start with graph size 8
		Graph g = new Graph(8);
		TabooList taboo = new TabooList();
		

		// while we do not have a publishable result
		while(g.size() < 102)
		{
			//find out how we are doing
			int count = g.cliqueCount();

			//if we have a counter example
			if(count == 0)
			{
				System.out.println("Eureka!  Counter-example found!\n"+g);
	
				//make a new graph one size bigger 
				g = g.extend();
				
				// reset the taboo list for the new graph
				taboo = new TabooList();

				// keep going
				continue;
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
			int best_i=0;
			int best_j=0;
			for(int i=0; i < g.size(); i++)
			{
				for(int j=i+1; j < g.size(); j++)
				{
					// flip it
					g.flip(i,j);
					count = g.cliqueCount();

					// is it better and the i,j,count not taboo?
					if((count < best_count) && 
						taboo.contains(i, j));
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
				System.out.println("no best edge found, terminating");
				System.exit(1);
			}
			
			// keep the best flip we saw
			g.flip(best_i, best_j);

			/*
			 * taboo this graph configuration so that we don't visit
			 * it again
			 */
			count = g.cliqueCount();
			taboo.add(best_i,best_j);
//			FIFOInsertEdgeCount(taboo_list,best_i,best_j,count);

			System.out.println("size: "+g.size()+"\t"
					+ "best_size: "+best_count+"\t"
					+ "best edge: ("+best_i+","+best_j+")\t"
					+ "color: "+ (g.get(best_i, best_j)?1:0)
					);
			// rinse and repeat
		}
	}

}
