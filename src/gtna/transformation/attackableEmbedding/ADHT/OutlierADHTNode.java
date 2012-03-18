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
 * OutlierADHTNode.java
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
public class OutlierADHTNode extends SimpleADHTNode {

	/**
	 * @param index
	 * @param g
	 * @param adht
	 */
	public OutlierADHTNode(int index, Graph g, ADHT adht) {
		super(index, g, adht);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void turn(Random rand) {
		if (this.knownIDs.length == 1){
			if (dist(knownIDs[0], this.ask(this, rand)) > Math.pow(2,-this.adht.minForSucc)){
				this.adht.getIds()[this.getIndex()].setPosition((knownIDs[0] - rand.nextDouble()*Math.pow(2,-this.adht.minForSucc)%1));
			}
			return;
		}
		Vector<Integer> index = new Vector<Integer>();
		int k;
		int countOld = 0;
		double oldID = this.ask(this,rand);
		double log2 = Math.log(2);
		for (int i = 0; i < this.knownIDs.length; i++){
        	k = (int) Math.min(Math.floor(Math.log(1/dist(oldID,knownIDs[i]))/log2), this.adht.upBound);
        	if (!index.contains(k)){
        		countOld++;
        		index.add(k);
        	}
        }
		
        double newID = rand.nextDouble();
        if (countOld < this.adht.exp[this.getIndex()] - this.adht.out*this.adht.var[this.getIndex()]){
        	this.adht.getIds()[this.getIndex()].setPosition(newID);
        	return;
        }
        index = new Vector<Integer>();
        int countNew = 0;
        for (int i = 0; i < this.knownIDs.length; i++){
        	k = (int) Math.min(Math.floor(Math.log(1/dist(newID,knownIDs[i]))/log2), this.adht.upBound);
        	if (!index.contains(k)){
        		countNew++;
        		index.add(k);
        	}
        }
        
        if (countNew > countOld){
        	this.adht.getIds()[this.getIndex()].setPosition(newID);
        }
	}

}
