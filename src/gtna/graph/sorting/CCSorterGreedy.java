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
 * CCSorterGreedy.java
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
package gtna.graph.sorting;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.sorting.CCSorterItertively.Computation;
import gtna.graph.sorting.NodeSorter.NodeSorterMode;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 * @author stefanie
 *
 */
public class CCSorterGreedy extends NodeSorter {
	boolean bidirectional;
	private Computation computation;
	
	public enum Computation{
		NODEBASED, DEGREEBASED
	}
	
		public CCSorterGreedy(boolean bidirectional, Computation computation) {
			super("CC-GREEDY"+computation.toString(), NodeSorterMode.ASC);
			this.bidirectional = bidirectional;
			this.computation = computation;
		}

		@Override
		public boolean applicable(Graph g) {
			return true;
		}

		@Override
		public Node[] sort(Graph g, Random rand) {
			Node[] sorted = this.clone(g.getNodes());
			Arrays.sort(sorted, new DegreeAsc());
			this.randomize(sorted, rand);
			if (this.mode == NodeSorterMode.DESC) {
				sorted = this.reverse(sorted);
			}
			return sorted;
		}

		private class DegreeAsc implements Comparator<Node> {
			public int compare(Node n1, Node n2) {
				return n1.getDegree() - n2.getDegree();
			}
		}

		@Override
		protected boolean isPropertyEqual(Node n1, Node n2) {
			return n1.getDegree() == n2.getDegree();
		}

}
