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
 * CCalculator.java
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
package gtna.metrics.id;

import gtna.data.Single;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.id.DPartition;
import gtna.id.ring.RingIdentifierSpace;
import gtna.id.ring.RingIdentifierSpaceSimple;
import gtna.id.ring.RingPartitionSimple;
import gtna.metrics.Metric;
import gtna.networks.Network;

import java.util.HashMap;

/**
 * @author stefanie
 *
 */
public class CCalculator extends Metric {

	/**
	 * @param key
	 */
	public CCalculator() {
		super("CCALCULATOR");
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#computeData(gtna.graph.Graph, gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		// TODO Auto-generated method stub

	}
	
	private HashMap<Integer,Integer> getMap(DPartition[] parts){
		HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
		double[] 
		return map;
	}
	
	private int[] getC(Node[] nodes, int index, DPartition[] parts, HashMap<Integer,Integer> map){
		Node cur = nodes[index];
		double left = 1;
		double right = 1;
		int indexLeft = -1, indexRight=-1;
		int[] out = cur.getOutgoingEdges();
		double curID = ((RingPartitionSimple)parts[index]).getId().getPosition();
		double d;
		for (int j = 0; j < out.length; j++){
			double id = ((RingPartitionSimple)parts[index]).getId().getPosition();
			d = clockwiseDist(curID, id);
			if (d < left){
				left = d;
				indexLeft = out[j];
			}
			d = 1-d;
			if (d < right){
				right = d;
				indexRight = out[j];
			}
		}
		int[] res = {indexLeft, indexRight};
		return res;
	}
	
	private double clockwiseDist(double a, double b){
		if (b >= a){
			return b - a;
		} else {
			return (1+b-a);
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
		return g.hasProperty("ID_SPACE_0")
				&& (g.getProperty("ID_SPACE_0") instanceof RingIdentifierSpace ||
						g.getProperty("ID_SPACE_0") instanceof RingIdentifierSpaceSimple	);
	}

}
