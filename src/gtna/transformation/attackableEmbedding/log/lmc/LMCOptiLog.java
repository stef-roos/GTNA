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
 * LMCOptiLog.java
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
package gtna.transformation.attackableEmbedding.log.lmc;

import gtna.graph.Graph;
import gtna.transformation.attackableEmbedding.AttackableEmbeddingNode;
import gtna.transformation.attackableEmbedding.lmc.LMC;
import gtna.transformation.attackableEmbedding.lmc.LMCAttackerOpti;
import gtna.transformation.attackableEmbedding.lmc.LMCNode;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author stefanie
 *
 */
public class LMCOptiLog extends LMCNodeLog {

	public static double interval = 0.001;
	public static boolean trapez = false;
	
	public LMCOptiLog(int index, Graph g, LMCLog lmc) {
		 super(index, g, lmc);
		 }
		
		 /**
		 * select ID at longest distance to any node
		 */
		 public void turn(Random rand) {
		 double[] neighbors = this.knownIDs.clone();
		 this.lmc.getIds()[this.getIndex()].setPosition(AttackableEmbeddingNode.maxMiddle(neighbors) + rand
		 .nextDouble()* this.lmc.delta);
		 }
		
		 /**
		 * return ID close to neighbor to keep it from changing IDs
		 */
		 protected double ask(LMCNodeLog caller, Random rand) {
			 if (caller instanceof LMCOptiLog){
				 return 0;
			 }
			int[] index = caller.getOutgoingEdges();
			ArrayList<Double> list = new ArrayList<Double>();
			LMCNodeLog neighbor;
			for (int i = 0; i < index.length; i++){
				if (index[i] != this.getIndex()){
					neighbor = (LMCNodeLog)this.getGraph().getNode(index[i]);
					if (!(neighbor instanceof LMCOptiLog)){
						list.add(neighbor.ask(caller, rand));
					}
				}
			}
			double[] ids = new double[list.size()];
			for (int i = 0; i < list.size(); i++){
				ids[i] = list.get(i);
			}
			double minVal = 1;
			double min = caller.ask(caller, rand);
			int num = (int) Math.ceil(interval);
			double cur = (min + this.lmc.delta) % 1.0;
			double v = min;
			double curVal;
			for (int j = 0; j < num; j++){
				curVal = getIntegralDiffAttackerNum(ids, cur, v);
				if (curVal < minVal){
					min = cur;
					minVal = curVal;
				}
				cur = (cur + interval)%1.0;
			}
			return min;
		 }
		 
		 /**
			 * approximation (b-a)f((a+b)/2)
			 * @param a
			 * @param b
			 * @param f
			 * @return
			 */
			public static double getIntegralRectangular(double a, double b, double f){
				return (b-a)*f;
			}
			
			/**
			 * approximation (b-a)(f(a) + f(b))/2
			 * @param a
			 * @param b
			 * @param f
			 * @return
			 */
			public static double getIntegralTrapez(double a, double b, double fa, double fb){
				return (b-a)*(fa + fb)/2;
			}
			
			public static double getIntegralLMCRectangular(double[] neighbors, double attacker, double a, double b, double victim){
				double x = (a+b)/2;
				double po = 1;
				for (int i = 0; i < neighbors.length; i++){
					po = po * dist(neighbors[i],victim);
				}
				double pn = 1;
				for (int i = 0; i < neighbors.length; i++){
					pn = pn * dist(neighbors[i],x);
				}
				double poA = dist(attacker,victim);
				double pnA = dist(attacker,x);
				double f = po - pn;
				if (poA*po < pnA*pn){
					f = f*(poA*po/(pnA*pn));
				}
				return getIntegralRectangular(a,b,f);
				
			}
			
			public static double getIntegralLMCTrapez(double[] neighbors, double attacker, double a, double b, double victim){
				double po = 1;
				for (int i = 0; i < neighbors.length; i++){
					po = po * dist(neighbors[i],victim);
				}
				double pn1 = 1;
				double pn2 = 1;
				for (int i = 0; i < neighbors.length; i++){
					pn1 = pn1 * dist(neighbors[i],a);
					pn2 = pn2 * dist(neighbors[i],b);
				}
				double poA = dist(attacker,victim);
				double pnA1 = dist(attacker,a);
				double pnA2 = dist(attacker,b);
				double fa = po - pn1;
				double fb = po - pn2;
				if (poA*po < pnA1*pn1){
					fa = fa*(poA*po/(pnA1*pn1));
				}
				if (poA*po < pnA2*pn2){
					fb = fb*(poA*po/(pnA2*pn2));
				}
				return getIntegralTrapez(a,b,fa, fb);
				
			}
			
			
			
			/**
			 * !
			 * @param neighbor
			 * @param attack
			 * @return
			 */
			public static double getIntegralDiffAttackerNum(double[] neighbors,  double attack, double victim){
				double sum = 0;
				int count = (int)Math.ceil(1.0/interval);
				for (int j = 0; j < count-1; j++){
					double a = j*interval;
					double b = (j+1)*interval;
					if (trapez){
						 sum = sum + getIntegralLMCTrapez(neighbors, attack, a, b, victim);	
					} else {
					   sum = sum + getIntegralLMCRectangular(neighbors, attack, a, b, victim);
					}
				}
				return sum;
			}
			
			
			private static double dist(double a, double b){
				return Math.min(Math.abs(a-b), Math.min(1-a+b, 1-b+a));
			}

}
