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
 * PairwiseLookaheadList.java
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
package gtna.transformation.lookahead;

import gtna.graph.Node;
import gtna.id.lookahead.LookaheadElement;
import gtna.id.lookahead.LookaheadList;

import java.util.ArrayList;

/**
 * @author stef
 *
 */
public class PairwiseLookaheadList extends PartialLookaheadList {

	/**
	 * @param key
	 * @param sigma
	 * @param devi
	 * @param randomize
	 */
	public PairwiseLookaheadList(double sigma, Deviation devi,
			boolean randomize) {
		super("PAIRWISE_LOOKAHEAD_LIST", sigma, devi, randomize);
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.lookahead.PartialLookaheadList#addEdge(gtna.graph.Node, gtna.graph.Node)
	 */
	@Override
	public boolean addEdge(Node a, Node b) {
		int sum = a.getIndex() + b.getIndex();
		if (sum % 2 == 0){
			if (a.getIndex() < b.getIndex()){
				return true;
			} else {
				return false;
			}
		} else {
			if (a.getIndex() > b.getIndex()){
				return true;
			} else {
				return false;
			}
		}
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.lookahead.PartialLookaheadList#addRandom(java.util.ArrayList, gtna.graph.Node)
	 */
	@Override
	public ArrayList<LookaheadElement> addRandom(
			ArrayList<LookaheadElement> list, Node a) {
		return list;
	}

	

}
