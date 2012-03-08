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
 * DiverseADHTNode.java
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
public class DiverseADHTNode extends SimpleADHTNode{
	public static double exponent = 1;

	/**
	 * @param index
	 * @param g
	 * @param adht
	 */
	public DiverseADHTNode(int index, Graph g, ADHT adht) {
		super(index, g, adht);
		
	}
	
	@Override
	public void turn(Random rand) {
		int[] twopowers = new int[this.knownIDs.length];
		double countOld = 0;
		double oldID = this.ask(this,rand);
		double log2 = Math.log(2);
		for (int i = 0; i < twopowers.length; i++){
			twopowers[i] = (int) Math.min(Math.floor(Math.log(1/dist(oldID,knownIDs[i]))/log2), this.adht.upBound);
		}
		for (int i = 0; i < twopowers.length; i++){
			for (int j = i+1; j < twopowers.length; j++){
				countOld = countOld + Math.pow(Math.abs(twopowers[i]-twopowers[j]), exponent);
			}
		}
		
		 double newID = rand.nextDouble();
	        double countNew = 0;
	        for (int i = 0; i < twopowers.length; i++){
				twopowers[i] = (int) Math.min(Math.floor(Math.log(1/dist(newID,knownIDs[i]))/log2), this.adht.upBound);
			}
			for (int i = 0; i < twopowers.length; i++){
				for (int j = i+1; j < twopowers.length; j++){
					countNew = countNew + Math.pow(Math.abs(twopowers[i]-twopowers[j]), exponent);
				}
			}
		if (countNew > countOld){
			this.adht.getIds()[this.getIndex()].setPosition(newID);
		}
	}
}
