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
 * ConnectivityNodeSorter.java
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
package gtna.graph.sorting;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.partition.Partition;
import gtna.metrics.fragmentation.Fragmentation;
import gtna.transformation.partition.StrongConnectivityPartition;
import gtna.transformation.partition.WeakConnectivityPartition;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 * @author stef
 *
 */
public class ConnectivityNodeSorter extends NodeSorter {
	private StrongConnectivityPartition scp;
	private WeakConnectivityPartition wcp;
	private Fragmentation.Type type;
	private int[] size;

	
	public ConnectivityNodeSorter(Fragmentation.Type type) {
		super("CONNECTIVITY", NodeSorter.NodeSorterMode.ASC);
		this.type = type;
	}

	@Override
	public boolean applicable(Graph g) {
		return true;
	}

	@Override
	public Node[] sort(Graph g, Random rand) {
		Node[] sorted = this.clone(g.getNodes());
		this.calculate(g);
		Arrays.sort(sorted, new ConnectivityAsc());
		this.randomize(sorted, rand);
		return sorted;
	}
	
	private void calculate(Graph g){
		Node[] nodes = g.getNodes();
		this.size = new int[nodes.length];
		boolean[] seen = new boolean[nodes.length];
		for (int i = 0; i < nodes.length; i++){ 
			seen[i] = true;
			Partition p=null;
		    if (this.type.equals(Fragmentation.Type.STRONG)){
				p =StrongConnectivityPartition.getStrongPartition(g, seen.clone());
			}		
		    if (this.type.equals(Fragmentation.Type.WEAK)){
				p =WeakConnectivityPartition.getWeakPartition(g, seen.clone());
			}
		    size[i] = p.getLargestComponent().length;
		    //System.out.println(size[i]);
		    seen[i] = false;
		}
	}

	private class ConnectivityAsc implements Comparator<Node> {
		public int compare(Node n1, Node n2) {
			return size[n1.getIndex()]-size[n2.getIndex()];
		}
	}

	@Override
	protected boolean isPropertyEqual(Node n1, Node n2) {
		return size[n1.getIndex()] == size[n2.getIndex()];
	}
}