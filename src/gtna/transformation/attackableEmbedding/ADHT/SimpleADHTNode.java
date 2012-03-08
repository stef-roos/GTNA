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
 * SimplyADHTNode.java
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
import gtna.transformation.attackableEmbedding.AttackableEmbeddingNode;
import gtna.transformation.attackableEmbedding.lmc.LMCNode;

import java.util.Random;
import java.util.Vector;

/**
 * @author stef
 *
 */
public class SimpleADHTNode extends AttackableEmbeddingNode {
	/**
	 * @param index
	 * @param g
	 */
	public SimpleADHTNode(int index, Graph g, ADHT adht) {
		super(index, g);
		this.adht = adht;
	}

	protected ADHT adht;

	/* (non-Javadoc)
	 * @see gtna.transformation.attackableEmbedding.AttackableEmbeddingNode#updateNeighbors(java.util.Random)
	 */
	@Override
	public void updateNeighbors(Random rand) {
		int[] out = this.getOutgoingEdges();
		 for (int i = 0; i < out.length; i++) {
		    this.knownIDs[i] = ((SimpleADHTNode) this.getGraph().getNode(out[i])).ask(this, rand);
		 }

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
		for (int i = 0; i < this.knownIDs.length; i++){
        	k = (int) Math.min(Math.floor(Math.log(1/dist(oldID,knownIDs[i]))/log2), this.adht.upBound);
        	if (!index.contains(k)){
        		countOld++;
        		index.add(k);
        	}
        }
		if (this.getIndex() == 1){
        	
        	String exact = "";
        	for (int j= 0; j < index.size(); j++){
        		exact = exact + index.get(j) + ", ";
        	}
        	System.out.println(exact);
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
        //
        if (this.getIndex() == 1){
        	System.out.println(countOld + " " + countNew + " " + this.knownIDs.length);
        	String exact = "";
        	for (int j= 0; j < index.size(); j++){
        		exact = exact + index.get(j) + ", ";
        	}
        	System.out.println(exact);
        }
        if (countNew > countOld){
        	this.adht.getIds()[this.getIndex()].setPosition(newID);
        }
	}
	
	protected double dist(double a, double b){
		return Math.min(Math.min(1-a+b,1-b+a), Math.abs(b-a));
	}
	
	 protected double ask(SimpleADHTNode caller, Random rand) {
		    if (this.adht == null){
		    	System.out.println("null index");
		    }
	        return this.adht.getIds()[this.getIndex()].getPosition();
	 }

}
