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
 * TwoSideADHTNode.java
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
public class TwoSideADHTNode extends SimpleADHTNode {

	/**
	 * @param index
	 * @param g
	 * @param adht
	 */
	public TwoSideADHTNode(int index, Graph g, ADHT adht) {
		super(index, g, adht);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void turn(Random rand) {
		Vector<Integer> index = new Vector<Integer>();
		int k;
		int countOld = 0;
		double oldID = this.ask(this,rand);
		double log2 = Math.log(2);
		double d;
		for (int i = 0; i < this.knownIDs.length; i++){
			d = distSigned(oldID,knownIDs[i]);
        	k = (int) (Math.min(Math.floor(Math.log(1/Math.abs(d))/log2), this.adht.upBound)*Math.signum(d));
        	if (!index.contains(k)){
        		countOld++;
        		index.add(k);
        	}
        }
		
        double newID = rand.nextDouble();
        index = new Vector<Integer>();
        int countNew = 0;
        for (int i = 0; i < this.knownIDs.length; i++){
        	d = distSigned(newID,knownIDs[i]);
        	k = (int) (Math.min(Math.floor(Math.log(1/Math.abs(d))/log2), this.adht.upBound)*Math.signum(d));
        	if (!index.contains(k)){
        		countNew++;
        		index.add(k);
        	}
        }
        
        if (countNew > countOld){
        	this.adht.getIds()[this.getIndex()].setPosition(newID);
        }
	}
	
	public double distSigned(double a, double b){
		double d = b -a;
		if (d < -0.5){
			d++;
		} 
		return d;
	}

}
