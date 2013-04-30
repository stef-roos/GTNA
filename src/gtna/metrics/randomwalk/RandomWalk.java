/* ===========================================================
 * GTNA : Graph-Theoretic Network Analyzer
 * ===========================================================
 *
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors
 *
 * Project Info:  http://www.p2p.tu-darmstadt.de/research/gtna/
 *
 * GTNA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GTNA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * ---------------------------------------
 * RandomWalk.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: stefanie;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.metrics.randomwalk;

import gtna.data.Single;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.util.Distribution;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * @author stefanie
 *
 */
public abstract class RandomWalk extends Metric {
	boolean perNode;
	int number;
	boolean fixed;
	double length;
	int[][] walks;
	Distribution lengths;
	double[] hits;
	double hits_mean;
	double hits_max;
	double hits_min;
	double hits_median;
	Distribution hitsStat;
	double guessProb;
	String name;
	

	/**
	 * @param key
	 */
	public RandomWalk(String name,boolean perNode, int number, boolean fixed, double length) {
		super(name, makePara(perNode,number,fixed,length));
		this.perNode = perNode;
		this.number = number;
		this.fixed = fixed;
		this.length = length;
		this.name =name;
		// TODO Auto-generated constructor stub
	}
	
	private static Parameter[] makePara(boolean perNode, int number, boolean fixed, double length){
		return new Parameter[] {new BooleanParameter("PERNODE",perNode),
				new IntParameter("NUMBER",number),
				new BooleanParameter("FIXED",fixed),
				new DoubleParameter("LENGTH",length)};
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#computeData(gtna.graph.Graph, gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		Node[] nodes = g.getNodes();
		hits = new double[nodes.length];
		int curNode;
		int count = 0;
		int max = 0;
		Random rand = new Random();
		int total = this.perNode?number*nodes.length:number;
		walks = new int[total][];
		for (int i = 0; i < total; i++){
			curNode = this.perNode?count/number:rand.nextInt(nodes.length);
			if (nodes[curNode].getOutDegree() == 0){
				walks[i] = new int[]{curNode};
				continue;
			}
			ArrayList<Integer> visited = new ArrayList<Integer>();
			visited.add(curNode);
			int next; 
			while ((next = this.getNext(nodes, visited,rand)) != -1){
				if (!visited.contains(next)){
					this.hits[next]++;
				}
				visited.add(next);
				
			}
			walks[i] = new int[visited.size()];
			for (int j = 0; j < visited.size(); j++){
				walks[i][j] = visited.get(j);
			}
			if (visited.size() > max){
				max = visited.size();
			}
			count++;
		}
        double[] paths = new double[max];
        for (int i = 0; i < walks.length; i++){
        	paths[walks[i].length-1]++;
        }
        for (int i = 0; i < paths.length; i++){
        	paths[i] = paths[i]/(double)total;
        }
        for (int i = 0; i < hits.length; i++){
        	hits[i] = hits[i]/(double)total;
        }
        double returned = 0;
        int all = 0;
        if (this.fixed){
        	guessProb = total;
        	all = (int)((int)total*this.length);
        	if (this.length > 1){
        	for (int i = 0; i < walks.length; i++){
        		int first = walks[i][0];
        		int third = walks[i][2];
        		if (first == third){
        			returned++;
        		} else {
        			if (nodes[third].hasIn(first)){
        				guessProb = guessProb + 1/(double)(nodes[third].getInDegree()-1);
        			}
        		}
        	}
        	for (int j = 3; j <= this.length; j++){
        		for (int i = 0; i < walks.length; i++){
            		if (walks[i][j-1] == walks[i][0]){
            			returned++;
            			guessProb++;
            		}
            	}
        	}
        	for (int i = 0; i < walks.length; i++){
        		if (walks[i][walks[i].length-1] == walks[i][0]){
        			returned++;
        		}
        	}
        	}
        } else {
        	guessProb = total;
        	for (int i = 0; i < walks.length; i++){
        		all = all + walks[i].length-1;
            	for (int j=3; j < walks[i].length; j++){
            		if (walks[i][j-1] == walks[i][0]){
            			returned++;
            			guessProb++;
            		}
            	}
        	}
        	for (int i = 0; i < walks.length; i++){
        		if (walks[i][walks[i].length-1] == walks[i][0]){
        			returned++;
        		}
        	}
        	
        }
        guessProb = guessProb/(double)(all-returned);
        this.lengths = new Distribution(paths);
        double[] allhits = new double[10001];
        for (int i = 0; i < hits.length; i++){
        	allhits[(int)hits[i]*10000]++;
        }
        for (int i = 0; i < allhits.length; i++){
        	allhits[i] = allhits[i]/(double)nodes.length;
        }
        this.hitsStat = new Distribution(allhits);
        double[] cloneHits = hits.clone();
        Arrays.sort(cloneHits);
        this.hits_max = cloneHits[cloneHits.length-1];
        this.hits_min = cloneHits[0];
        this.hits_median = cloneHits[cloneHits.length/2];
        this.hits_mean = 0;
        for (int i = 0; i < hits.length; i++){
        	this.hits_mean = this.hits_mean + hits[i];
        }
        this.hits_mean = this.hits_mean/(double)hits.length;
	}
	
	public abstract int getNext(Node[] nodes,ArrayList<Integer> visited, Random rand);

	
	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#writeData(java.lang.String)
	 */
	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithIndex(
				this.lengths.getDistribution(),
				name+"_LENGTH_DISTRIBUTION", folder);
		success &= DataWriter.writeWithIndex(
				this.lengths.getCdf(),
				name+"_LENGTH_DISTRIBUTION_CDF", folder);
		success &= DataWriter.writeWithIndex(
				this.hitsStat.getDistribution(),
				name+"_HITSTAT_DISTRIBUTION", folder);
		success &= DataWriter.writeWithIndex(
				this.hitsStat.getCdf(),
				name+"_HITSTAT_DISTRIBUTION_CDF", folder);
		success &= DataWriter.writeWithIndex(
				this.hits,
				name+"_HITS", folder);
		return success;
	}
	
	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#getSingles()
	 */
	@Override
	public Single[] getSingles() {
		Single minLength = new Single(this.name+"_MIN_LENGTH",this.lengths.getMin());
		Single maxLength = new Single(this.name+"_MAX_LENGTH",this.lengths.getMax());
		Single medianLength = new Single(this.name+"_MEDIAN_LENGTH",this.lengths.getMedian());
		Single meanLength = new Single(this.name+"_MEAN_LENGTH",this.lengths.getAverage());
		Single minHits = new Single(this.name+"_MIN_HITS",this.lengths.getMin());
		Single maxHits = new Single(this.name+"_MAX_HITS",this.lengths.getMax());
		Single medianHits = new Single(this.name+"_MEDIAN_HITS",this.lengths.getMedian());
		Single meanHits = new Single(this.name+"_MEAN_HITS",this.lengths.getAverage());
		Single guess = new Single(this.name+"_GUESS_PROB",this.guessProb);
		return new Single[]{minLength,maxLength,medianLength,meanLength,minHits,maxHits,
				medianHits, meanHits,guess};
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#applicable(gtna.graph.Graph, gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		// TODO Auto-generated method stub
		return true;
	}

}
