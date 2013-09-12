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
 * ClosenessCentralityNSUpdate.java
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
import gtna.graph.sorting.NodeSorter.NodeSorterMode;
import gtna.graph.sorting.algorithms.GraphSPAllFloyd;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

/**
 * @author stef
 *
 */
public class ClosenessCentralityNSUpdate extends NodeSorterUpdate{
	
	private HashMap<Node, Double> map = new HashMap<Node, Double>();
    private Graph g;
	/**
	 * @param key
	 * @param mode
	 */
	public ClosenessCentralityNSUpdate() {
		super("CLOSENESS-UPDATE", NodeSorter.NodeSorterMode.DESC);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see gtna.graph.sorting.NodeSorterUpdate#update(boolean[], int, java.util.Random)
	 */
	@Override
	public Node[] update(boolean[] deleted, int index, Random rand) {
		
		this.calculate(g, deleted);
		Node[] sorted = this.clone(g.getNodes());
		Arrays.sort(sorted, new CentralityAsc());
		this.randomize(sorted, rand);
		if (this.mode == NodeSorterMode.DESC) {
		sorted = this.reverse(sorted);
		}
		return sorted;
	}

	/*
	* (non-Javadoc)
	*
	* @see gtna.graph.sorting.NodeSorter#sort(gtna.graph.Graph,
	* java.util.Random)
	*/
	@Override
	public Node[] sort(Graph g, Random rand) {
		this.g = g;
	this.calculate(g, new boolean[g.getNodeCount()]);
	Node[] sorted = this.clone(g.getNodes());
	Arrays.sort(sorted, new CentralityAsc());
	this.randomize(sorted, rand);
	if (this.mode == NodeSorterMode.DESC) {
	sorted = this.reverse(sorted);
	}
	return sorted;
	}

	/**
	*
	*/
	private void calculate(Graph g, boolean[] removed) {
		map = new HashMap<Node, Double>();
	GraphSPAllFloyd floyd = new GraphSPAllFloyd(g,removed);
	for (int i = 0; i < g.getNodes().length; i++) {
		
	double sum = 0;
	if (!removed[i]){
	
	for (int j = 0; j < g.getNodes().length; j++) {
		if (removed[j]) continue;
	if (floyd.dist(i, j) < floyd.INF) {
	sum += floyd.dist(i, j);
	}
	}
		} else {
			sum = 0;
		}
	map.put(g.getNode(i), 1.0 / sum);
	}

	}

	private class CentralityAsc implements Comparator<Node> {
	public int compare(Node n1, Node n2) {
	double point1 = map.get(n1)
	.doubleValue();
	double point2 = map.get(n2)
	.doubleValue();
	int result;
	if (point1 == point2) {
	result = 0;
	} else if (point1 > point2) {
	result = 1;
	} else {
	result = -1;
	}
	return result;
	}
	}

	/*
	* (non-Javadoc)
	*
	* @see gtna.graph.sorting.NodeSorter#applicable(gtna.graph.Graph)
	*/
	@Override
	public boolean applicable(Graph g) {
	return true;
	}

	/*
	* (non-Javadoc)
	*
	* @see gtna.graph.sorting.NodeSorter#isPropertyEqual(gtna.graph.Node,
	* gtna.graph.Node)
	*/
	@Override
	protected boolean isPropertyEqual(Node n1, Node n2) {
	return map.get(n1).doubleValue() == map.get(n2).doubleValue();
	}

	public double getCentrality(Node n) {
	return map.get(n);
	}
	
	

}
