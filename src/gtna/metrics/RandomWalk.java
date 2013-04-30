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
package gtna.metrics;

import gtna.data.Single;
import gtna.graph.Graph;
import gtna.networks.Network;
import gtna.util.Distribution;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * @author stefanie
 *
 */
public class RandomWalk extends Metric {
	boolean perNode;
	int number;
	boolean fixed;
	double length;
	int[][] walks;
	Distribution lengths;
	double guessProb;
	

	/**
	 * @param key
	 */
	public RandomWalk(boolean perNode, int number, boolean fixed, double length) {
		super("RANDOMWALK", makePara(perNode,number,fixed,length));
		this.perNode = perNode;
		this.number = number;
		this.fixed = fixed;
		this.length = length;
		
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
		gtna.graph.Node[] nodes = g.getNodes();
		int curNode;
		int count = 0;
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
			
		}

	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#writeData(java.lang.String)
	 */
	@Override
	public boolean writeData(String folder) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#getSingles()
	 */
	@Override
	public Single[] getSingles() {
		// TODO Auto-generated method stub
		return null;
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
