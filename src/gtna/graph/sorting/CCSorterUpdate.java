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
 * CCSorterUpdate.java
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

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 * @author stef
 *
 */
public class CCSorterUpdate extends NodeSorterUpdate {
	private boolean bidirectional;
	private int[][] degs;
	private double epm;
	private double ep;
	private double f;
	private Node[] sorted;
	private double[] c;

	/**
	 * @param key
	 * @param mode
	 */
	public CCSorterUpdate(boolean bidirectional) {
		super("CC", NodeSorterMode.ASC);
		this.bidirectional = bidirectional;
	}

	/* (non-Javadoc)
	 * @see gtna.graph.sorting.NodeSorterUpdate#update(boolean[], int, java.util.Random)
	 */
	@Override
	public Node[] update(boolean[] deleted, int index, Random rand) {
		int node = sorted[index].getIndex();
		if (this.bidirectional){
			this.epm = this.epm - degs[node][0]*degs[node][0]*f;
			this.ep = this.ep - 2*degs[node][0]*f;
		} else {
			this.epm = this.epm - degs[node][0]*degs[node][1]*f;
			this.ep = this.ep - (degs[node][0]+degs[node][1])*f;
		}
		int[] in = sorted[index].getIncomingEdges();
		for (int k = 0; k < in.length; k++){
			if (this.bidirectional){
			   this.epm = this.epm - 2*degs[in[k]][0]*f+f;
			} else {
				this.epm = this.epm - degs[in[k]][0]*f;
			}
			degs[in[k]][1]--;
		}
		if (!bidirectional){
		int[] out = sorted[index].getOutgoingEdges();
		for (int k = 0; k < out.length; k++){
			this.epm = this.epm - degs[out[k]][1]*f;
            degs[out[k]][0]--;
		}
		}
		System.out.println("Actual " + (2*epm-2*ep) + " " + 1/(double)(sorted.length-index)*(2*epm-2*ep) + " epm " + epm + " ep " + ep);
		//resort
		this.setC();
		for (int i = index+1; i < sorted.length-1; i++){
			int j = i+1;
			while (j < sorted.length && c[sorted[j].getIndex()] < c[sorted[i].getIndex()]){
				j++;
			}
			if (j > i+1){
				Node temp = sorted[i];
				sorted[i] = sorted[j-1];
				sorted[j-1] = temp;
			}
		}
		System.out.println("Predicted " + c[sorted[index+1].getIndex()]);
		return sorted;
	}

	/* (non-Javadoc)
	 * @see gtna.graph.sorting.NodeSorter#sort(gtna.graph.Graph, java.util.Random)
	 */
	@Override
	public Node[] sort(Graph g, Random rand) {
		this.sorted = this.clone(g.getNodes());
		if (this.bidirectional){
		this.degs = new int[sorted.length][1];
		} else {
			this.degs = new int[sorted.length][2];
		}
		f = 1/(double)this.degs.length;
		for (int i = 0; i < degs.length; i++){
			degs[i][0] = sorted[i].getInDegree();
			if (!this.bidirectional){
				degs[i][1] = sorted[i].getOutDegree();
			}
			if (this.bidirectional){
			   this.epm = this.epm +degs[i][0]*degs[i][0];
			} else {
				this.epm = this.epm +degs[i][0]*degs[i][1];
			}
			this.ep = this.ep + degs[i][0];
		}
		this.epm = this.epm*f;
		this.ep = this.ep*f;
		this.setC();
		Arrays.sort(sorted, new CCAsc());
		this.randomize(sorted, rand);
		System.out.println("Predicted " + c[sorted[0].getIndex()]);
		if (this.mode == NodeSorterMode.DESC) {
			sorted = this.reverse(sorted);
		}
		return sorted;
	}
	
	private void setC(){
		this.c = new double[sorted.length];
		for (int i = 0; i <c.length; i++){
			c[i] = getCWithout(i);
		}
	}
	
	private double getCWithout(int index){
		double c = 0;
		if (this.bidirectional){
			double e2 = this.degs[index][0]*this.degs[index][0]*f;
			double e1 = this.degs[index][0]*f;
			double p = e1/ep;
			c = (1-p)*(1-p)*(this.epm-e2)-2*(1-p)*(ep-e1);
		} else {
			double e2 = 2*this.degs[index][0]*this.degs[index][1]*f;
			double ein = this.degs[index][0]*f;
			double eout = this.degs[index][1]*f;
			double pin = ein/ep;
			double pout = eout/ep;
			c = (1-pin)*(1-pout)*(2*epm - e2)-(1-pout)*(ep-ein)-(1-pin)*(ep-eout);
		}
		return c;
	}
	
	private class CCAsc implements Comparator<Node> {
		public int compare(Node n1, Node n2) {
			double d = c[n1.getIndex()] - c[n2.getIndex()];
			if (d < 0){
			return -1;
			} else {
				if (d == 0){
					return 0;
				}else {
					return 1;
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see gtna.graph.sorting.NodeSorter#applicable(gtna.graph.Graph)
	 */
	@Override
	public boolean applicable(Graph g) {
		return true;
	}

	/* (non-Javadoc)
	 * @see gtna.graph.sorting.NodeSorter#isPropertyEqual(gtna.graph.Node, gtna.graph.Node)
	 */
	@Override
	protected boolean isPropertyEqual(Node n1, Node n2) {
		return false;
	}

}