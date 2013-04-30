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
 * SimpleRandomWalk.java
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

import gtna.graph.Node;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author stefanie
 *
 */
public class SimpleRandomWalk extends RandomWalk{

	/**
	 * @param perNode
	 * @param number
	 * @param fixed
	 * @param length
	 */
	public SimpleRandomWalk(boolean perNode, int number, boolean fixed,
			double length) {
		super("SIMPLE_RANDOM_WALK",perNode, number, fixed, length);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.randomwalk.RandomWalk#getNext(gtna.graph.Node[], java.util.ArrayList)
	 */
	@Override
	public int getNext(Node[] nodes, ArrayList<Integer> visited, Random rand) {
		int last = visited.get(visited.size()-1);
		if (nodes[last].getOutDegree() == 0){
			return -1;
		}
	    int next = -1;
	    if ( (this.fixed && visited.size() <= this.length) ||
	    		(!this.fixed && rand.nextDouble() < this.length)){
	    	int[] out = nodes[last].getOutgoingEdges();
	    	next = out[rand.nextInt(out.length)];
	    }
		return next;
	}

	

	

}
