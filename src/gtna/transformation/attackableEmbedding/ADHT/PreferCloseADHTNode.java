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
 * PreferCloseADHTNode.java
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

import java.util.Random;
import java.util.Vector;

/**
 * @author stef
 *
 */
public class PreferCloseADHTNode extends SimpleADHTNode {
	
	/**
	 * @param index
	 * @param g
	 * @param adht
	 */
	public PreferCloseADHTNode(int index, Graph g, ADHT adht) {
		super(index, g, adht);
		
	}
	
	@Override
	public void turn(Random rand) {
		Vector<Integer> index = new Vector<Integer>();
		int k;
		int countOld = 0;
		double oldID = this.ask(this,rand);
		double log2 = Math.log(2);
		int max = 0;
		double dist;
        for (int i = 0; i < this.knownIDs.length; i++){
        	dist = dist(oldID,knownIDs[i]);
        	k = (int) Math.min(Math.floor(Math.log(1/dist)/log2), this.adht.upBound);
        	if (!index.contains(k)){
        		countOld++;
        		index.add(k);
        		if (k > max){
        			max = k;
        		}
        	}
        }
        double newID = rand.nextDouble();
        index = new Vector<Integer>();
        int countNew = 0;
        int maxNew = 0;
        for (int i = 0; i < this.knownIDs.length; i++){
        	dist = dist(oldID,knownIDs[i]);
        	k = (int) Math.min(Math.floor(Math.log(1/dist)/log2), this.adht.upBound);
        	if (!index.contains(k)){
        		countNew++;
        		index.add(k);
        		if (k > maxNew){
        			k = maxNew;
        		}
        	}
        }
        if (countNew > countOld){
        	this.adht.getIds()[this.getIndex()].setPosition(newID);
        } else {
        	if (countNew == countOld && maxNew > max){
        		this.adht.getIds()[this.getIndex()].setPosition(newID);
        	}
        }
	}

}
