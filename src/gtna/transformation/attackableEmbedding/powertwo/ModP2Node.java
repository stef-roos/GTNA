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
 * ModP2Node.java
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
package gtna.transformation.attackableEmbedding.powertwo;

import gtna.graph.Graph;

import java.util.Random;

/**
 * @author stef
 *
 */
public class ModP2Node extends SimpleP2Node {
	
	/**
	 * @param index
	 * @param g
	 * @param ptv
	 */
	public ModP2Node(int index, Graph g, PowerTwoVar ptv) {
		super(index, g, ptv);
		// TODO Auto-generated constructor stub
	}
	
	/* (non-Javadoc)
	 * @see gtna.transformation.attackableEmbedding.AttackableEmbeddingNode#turn(java.util.Random)
	 */
	@Override
	public void turn(Random rand) {
		if (this.knownIDs.length == 1){
			if (dist(knownIDs[0], this.ask(this, rand)) > Math.pow(2,-this.ptv.minForSucc)){
				this.ptv.getIds()[this.getIndex()].setPosition((knownIDs[0] - rand.nextDouble()*Math.pow(2,-this.ptv.minForSucc)%1));
			}
			return;
		}
		int[] twopowers = new int[this.knownIDs.length];
		double countOld = 0;
		double oldID = this.ask(this,rand);
		double log2 = Math.log(2);
		double sdist;
		for (int i = 0; i < twopowers.length; i++){
			sdist = distSigned(oldID,knownIDs[i]);
			twopowers[i] = (int) (Math.min(Math.floor(Math.log(1/Math.abs(sdist))/log2), this.ptv.upBound));
			if (sdist < 0){
			    twopowers[i] = 2*this.ptv.upBound +1 - twopowers[i];
			}	
		}
		for (int i = 0; i < twopowers.length; i++){
			for (int j = i+1; j < twopowers.length; j++){
				countOld = countOld + Math.pow(Math.min(Math.abs(twopowers[i]-twopowers[j]), 
						Math.min(2*this.ptv.upBound+1+twopowers[i]-twopowers[j], 2*this.ptv.upBound+1+twopowers[j]-twopowers[i])), this.ptv.exponent);
			}
		}
		
		 double newID = rand.nextDouble();
	        double countNew = 0;
	        for (int i = 0; i < twopowers.length; i++){
	        	sdist = distSigned(newID,knownIDs[i]);
	        	twopowers[i] = (int) (Math.min(Math.floor(Math.log(1/Math.abs(sdist))/log2), this.ptv.upBound));
				if (sdist < 0){
				    twopowers[i] = 2*this.ptv.upBound +1 - twopowers[i];
				}
			}
			for (int i = 0; i < twopowers.length; i++){
				for (int j = i+1; j < twopowers.length; j++){
					countNew = countNew + Math.pow(Math.min(Math.abs(twopowers[i]-twopowers[j]), 
							Math.min(2*this.ptv.upBound+1+twopowers[i]-twopowers[j], 2*this.ptv.upBound+1+twopowers[j]-twopowers[i])), this.ptv.exponent);
				}
			}
		if (countNew > countOld){
			this.ptv.getIds()[this.getIndex()].setPosition(newID);
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
