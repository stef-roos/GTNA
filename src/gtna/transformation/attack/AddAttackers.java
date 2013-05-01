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
 * AddAttackers.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: stefanie;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.attack;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.id.BIIdentifier;
import gtna.transformation.Transformation;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author stefanie
 *
 */
public class AddAttackers extends Transformation implements GraphProperty{
	private int add;
	private int[] att;
	private Choice choice;
	private boolean[] isAttacker;
	private BIIdentifier target;
	public enum Choice{
		RANDOM, GIVEN, GIVENBOOL
	}
	
	
	/**
	 * @param attacker: number of attackers
	 */
	public AddAttackers(int attacker, Choice choice, BIIdentifier target) {
		super("ADD_ATTACKERS", new Parameter[]{new IntParameter("ADD",attacker), 
				new StringParameter("CHOICE",choice.toString())});
		this.add = attacker;
		this.choice = choice;
		this.target = target;
	}
	
	/**
	 * @param key
	 */
	public AddAttackers(int[] attacker, BIIdentifier target) {
		super("ADD_ATTACKERS", new Parameter[]{new IntParameter("ADD",attacker.length), 
				new StringParameter("CHOICE",Choice.GIVEN.toString())});
		this.choice = Choice.GIVEN;
		this.att = attacker;
		this.target = target;
	}
	
	public AddAttackers(boolean[] attacker, BIIdentifier target) {
		super("ADD_ATTACKERS", new Parameter[]{new IntParameter("ADD",attacker.length), 
				new StringParameter("CHOICE",Choice.GIVEN.toString())});
		this.choice = Choice.GIVENBOOL;
		this.isAttacker = attacker;
		this.target = target;
	}

	

	/* (non-Javadoc)
	 * @see gtna.graph.GraphProperty#write(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean write(String filename, String key) {
		//Filereader read = new Fileraeder(file)
		return false;
	}

	/* (non-Javadoc)
	 * @see gtna.graph.GraphProperty#read(java.lang.String, gtna.graph.Graph)
	 */
	@Override
	public void read(String filename, Graph graph) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.Transformation#transform(gtna.graph.Graph)
	 */
	@Override
	public Graph transform(Graph g) {
		if (this.choice == Choice.RANDOM){
			this.isAttacker = new boolean[g.getNodes().length];
			Random rand = new Random();
			int count = 0;
			while (count < this.add && count < this.isAttacker.length){
				int next = rand.nextInt(this.isAttacker.length);
				if (!this.isAttacker[next]){
					this.isAttacker[next] = true;
					count++;
				}
			}
		}
		if (this.choice == Choice.GIVEN){
			this.isAttacker = new boolean[g.getNodes().length];
			for (int i = 0; i < this.att.length; i++){
				this.isAttacker[att[i]] = true;
			}
		}
		g.addProperty(g.getNextKey("ATTACKER"), this);
		return g;
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.Transformation#applicable(gtna.graph.Graph)
	 */
	@Override
	public boolean applicable(Graph g) {
		return true;
	}
	
	public boolean[] getAttackers(){
		return this.isAttacker;
	}
	
	public BIIdentifier getTarget(){
		return this.target;
	}
	
	

}
