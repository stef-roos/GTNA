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
 * Modularity.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: stef;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.metrics.communities;

import gtna.communities.Community;
import gtna.data.Single;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.metrics.Metric;
import gtna.networks.Network;

import java.util.HashMap;

/**
 * @author stef
 *
 */
public class Modularity extends Metric {
	int[] clusters;
	
	public Modularity(int[] clusters){
		super("Modularity");
		this.clusters = clusters;
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#computeData(gtna.graph.Graph, gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		Node[] nodes = g.getNodes();
//		double E = g.getEdges().size();
//		double Q = 0;
//		int max = 0;
//		for (int i = 0; i < nodes.length; i++){
//			if (clusters[i] > max){
//				max = clusters[i];
//			}
//		}
//		for (int i = 0; i <= max; i++) {
//			double IC = 0;
//			double OC = 0;
//			for (Node node : nodes) {
//				if (clusters[node.getIndex()] == i){
//				for (int out : node.getOutgoingEdges()) {
//					if (clusters[out] == i) {
//						IC++;
//					} else {
//						OC++;
//					}
//				}
//				for (int in : node.getIncomingEdges()) {
//					if (clusters[in]!= i) {
//						OC++;
//					}
//				}
//				}
//			}
//
//			Q += IC / E - Math.pow((IC + OC) / E, 2);
//		}
		//Node[] nodes = g.getNodes();
		double edges = g.computeNumberOfEdges();
		double mod = 0;
		for (int i = 0; i < nodes.length; i++){
			int[] out = nodes[i].getOutgoingEdges();
			for (int j = 0; j < nodes.length; j++){
				if (j != i && this.clusters[j] == this.clusters[i]){
					if (nodes[i].hasNeighbor(j)){
						mod =  mod + 1;
					}
					mod = mod - (double)(nodes[i].getOutDegree()*nodes[j].getOutDegree())/edges;
				}
			}
		}
        mod = mod/edges;
        System.out.println(mod);
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
		return false;
	}

}
