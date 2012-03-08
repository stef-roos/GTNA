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
 * SuccADHTNode.java
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

import java.util.Random;
import java.util.Vector;

import gtna.graph.Graph;

/**
 * @author stef
 *
 */
public class SuccADHTNode extends SimpleADHTNode {

	/**
	 * @param index
	 * @param g
	 * @param adht
	 */
	public SuccADHTNode(int index, Graph g, ADHT adht) {
		super(index, g, adht);
		
	}
	
	@Override
	public void turn(Random rand) {
		boolean preOld = false;
		Vector<Integer> index = new Vector<Integer>();
		int k;
		int countOld = 0;
		double oldID = this.ask(this,rand);
		double log2 = Math.log(2);
		double dist;
        for (int i = 0; i < this.knownIDs.length; i++){
        	dist = dist(oldID,knownIDs[i]);
        	k = (int) Math.min(Math.floor(Math.log(1/dist)/log2), this.adht.upBound);
        	if (!index.contains(k)){
        		if (k <= this.adht.minForSucc && (knownIDs[i] - oldID == dist || 1 + knownIDs[i] -oldID == dist)){
        			preOld = true;
        		}
        		countOld++;
        		index.add(k);
        	}
        }
        double newID = rand.nextDouble();
        index = new Vector<Integer>();
        int countNew = 0;
        boolean preNew = false;
        for (int i = 0; i < this.knownIDs.length; i++){
        	dist = dist(newID,knownIDs[i]);
        	k = (int) Math.min(Math.floor(Math.log(1/dist)/log2), this.adht.upBound);
        	if (!index.contains(k)){
        		if (k <= this.adht.minForSucc && (knownIDs[i] - oldID == dist || 1 + knownIDs[i] -oldID == dist)){
        			preNew = true;
        		}
        		countNew++;
        		index.add(k);
        	}
        }
        
        if (countNew > countOld && preNew){
        	System.out.println("changed 1");
        	this.adht.getIds()[this.getIndex()].setPosition(newID);
        } else {
        	if (countNew > countOld && !preOld){
        		//System.out.println("changed 2");
        		this.adht.getIds()[this.getIndex()].setPosition(newID);
        	} else {
        		//System.out.println("UNchanged");
        	}
        }
	}

}
