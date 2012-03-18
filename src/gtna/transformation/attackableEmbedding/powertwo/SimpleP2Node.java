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
 * SimpleP2Node.java
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
import gtna.transformation.attackableEmbedding.AttackableEmbeddingNode;
import gtna.transformation.attackableEmbedding.ADHT.ADHT;
import gtna.transformation.attackableEmbedding.ADHT.SimpleADHTNode;

import java.util.Random;
import java.util.Vector;

/**
 * @author stef
 *
 */
public class SimpleP2Node extends AttackableEmbeddingNode{
	

	public SimpleP2Node(int index, Graph g, PowerTwoVar ptv) {
		super(index, g);
		this.ptv = ptv;
	}

	protected PowerTwoVar ptv;

	/* (non-Javadoc)
	 * @see gtna.transformation.attackableEmbedding.AttackableEmbeddingNode#updateNeighbors(java.util.Random)
	 */
	@Override
	public void updateNeighbors(Random rand) {
		int[] out = this.getOutgoingEdges();
		 for (int i = 0; i < out.length; i++) {
		    this.knownIDs[i] = ((SimpleP2Node) this.getGraph().getNode(out[i])).ask(this, rand);
		 }

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
		for (int i = 0; i < twopowers.length; i++){
			twopowers[i] = (int) Math.min(Math.floor(Math.log(1/dist(oldID,knownIDs[i]))/log2), this.ptv.upBound);
		}
		for (int i = 0; i < twopowers.length; i++){
			for (int j = i+1; j < twopowers.length; j++){
				countOld = countOld + Math.pow(Math.abs(twopowers[i]-twopowers[j]), this.ptv.exponent);
			}
		}
		
		 double newID = rand.nextDouble();
	        double countNew = 0;
	        for (int i = 0; i < twopowers.length; i++){
				twopowers[i] = (int) Math.min(Math.floor(Math.log(1/dist(newID,knownIDs[i]))/log2), this.ptv.upBound);
			}
			for (int i = 0; i < twopowers.length; i++){
				for (int j = i+1; j < twopowers.length; j++){
					countNew = countNew + Math.pow(Math.abs(twopowers[i]-twopowers[j]), this.ptv.exponent);
				}
			}
		if (countNew > countOld){
			this.ptv.getIds()[this.getIndex()].setPosition(newID);
		}
	}
	
	protected double dist(double a, double b){
		return Math.min(Math.min(1-a+b,1-b+a), Math.abs(b-a));
	}
	
	 protected double ask(SimpleP2Node caller, Random rand) {
		    
	        return this.ptv.getIds()[this.getIndex()].getPosition();
	 }
}