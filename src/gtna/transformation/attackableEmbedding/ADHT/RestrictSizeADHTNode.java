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
 * RestrictSizeADHTNode.java
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
package gtna.transformation.attackableEmbedding.ADHT;

import gtna.graph.Graph;

import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

/**
 * @author stef
 *
 */
public class RestrictSizeADHTNode extends SimpleADHTNode {
	
	public static double p = 0.5; 
	
	/**
	 * @param index
	 * @param g
	 */
	public RestrictSizeADHTNode(int index, Graph g, ADHT adht) {
		super(index, g, adht);
	}



	

	/* (non-Javadoc)
	 * @see gtna.transformation.attackableEmbedding.AttackableEmbeddingNode#turn(java.util.Random)
	 */
	@Override
	public void turn(Random rand) {
		Vector<Integer> index = new Vector<Integer>();
		int k;
		int countOld = 0;
		double oldID = this.ask(this,rand);
		double log2 = Math.log(2);
		HashMap<Integer,Integer> counts = new HashMap<Integer, Integer>();
		for (int i = 0; i < this.knownIDs.length; i++){
        	k = (int) Math.min(Math.floor(Math.log(1/dist(oldID,knownIDs[i]))/log2), this.adht.upBound);
        	if (!index.contains(k)){
        		countOld++;
        		index.add(k);
        		counts.put(k, 1);
        	} else{
        		int c = counts.remove(k);
        		counts.put(k, c+1);
        	}
        }
		for (int i = 0; i < index.size(); i++){
			if (counts.get(index.get(i)) > this.knownIDs.length*p){
				countOld = 0;
				this.adht.getIds()[this.getIndex()].setPosition(rand.nextDouble());
				return;
			}
		}
        double newID = rand.nextDouble();
        index = new Vector<Integer>();
        int countNew = 0;
        for (int i = 0; i < this.knownIDs.length; i++){
        	k = (int) Math.min(Math.floor(Math.log(1/dist(newID,knownIDs[i]))/log2), this.adht.upBound);
        	if (!index.contains(k)){
        		countNew++;
        		index.add(k);
        	}
        }
        if (rand.nextDouble() < (double)countNew/(double)countOld){
        	this.adht.getIds()[this.getIndex()].setPosition(newID);
        }
	}

}
