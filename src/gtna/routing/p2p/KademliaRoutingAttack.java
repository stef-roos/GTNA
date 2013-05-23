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
 * KademliaRoutingAttack.java
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
package gtna.routing.p2p;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.graph.Node;
import gtna.id.BIIdentifier;
import gtna.id.Identifier;
import gtna.id.Partition;
import gtna.id.data.DataStorageList;
import gtna.networks.p2p.kademlia.KademliaIdentifierSpace;
import gtna.networks.p2p.kademlia.KademliaPartition;
import gtna.routing.Route;
import gtna.routing.RouteImpl;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.attack.AddAttackers;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

/**
 * @author stefanie
 *
 */
public class KademliaRoutingAttack extends RoutingAlgorithm {

	private KademliaIdentifierSpace idSpaceBI;

	private KademliaPartition[] pBI;

	private DataStorageList dsl;

	private int ttl;
	
	private int alpha;
	
	private int beta;
	
	private BIIdentifier target;
	
	private boolean[] isAtt;
	

	public KademliaRoutingAttack(int alpha, int beta) {
		super("KADEMLIA_ROUTING_ATTACK", new Parameter[]{new IntParameter("ALPHA", alpha),
				new IntParameter("BETA", beta)});
		this.ttl = Integer.MAX_VALUE;
		this.alpha = alpha;
		this.beta = beta;
	}

	public KademliaRoutingAttack(int ttl, int alpha, int beta) {
		super("KADEMLIA_ROUTING_ATTACK", new Parameter[] {new IntParameter("ALPHA", alpha),
				new IntParameter("BETA", beta), new IntParameter("TTL", ttl) });
		this.ttl = ttl;
		this.alpha = alpha;
		this.beta = beta;
	}

	@Override
	public Route routeToRandomTarget(Graph graph, int start, Random rand) {
		if (this.idSpaceBI != null) {
			return this.routeToRandomTargetBI(graph, start, rand);
		} else {
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Route routeToTarget(Graph graph, int start, Identifier target,
			Random rand) {
		if (this.idSpaceBI != null) {
			return this.routeBI(new ArrayList<Integer>(), start,
					(BIIdentifier) target, rand, graph.getNodes());
		}  else {
			return null;
		}
	}

	private Route routeToRandomTargetBI(Graph graph, int start, Random rand) {
		Partition<BigInteger>[] part = this.idSpaceBI.getPartitions();
		BIIdentifier targetID;
		if (this.target == null){
			targetID = (BIIdentifier) this.idSpaceBI.randomID(rand);
		while (this.pBI[start].contains(targetID)) {
			targetID = (BIIdentifier) part[rand.nextInt(part.length)].getRepresentativeID();
		}
		} else {
			targetID = (BIIdentifier) this.target;
		}
		return this.routeBI(new ArrayList<Integer>(), start, targetID, rand,
				graph.getNodes());
	}

	private Route routeBI(ArrayList<Integer> route, int current,
			BIIdentifier target, Random rand, Node[] nodes) {
		route.add(current);
		boolean[] contacted = new boolean[nodes.length];
		contacted[current] = true;
		Vector<Integer> list = new Vector<Integer>();
		//int countA=0;
		//System.out.println("Start " +current);
		int[] out =nodes[current].getOutgoingEdges();
		for (int i = 0; i < out.length; i++){
			list.add(out[i]);
//			if (this.isAtt[out[i]]){
//				countA++;
//			}
		}
		
		int[] top = this.getTopAlpha(list, target, rand, nodes,contacted);
		int re = getRep(top);
//		if (countA > 2){
//			System.out.println("High att " + countA + " re " + re );
//		}
		if (re == -1){
			//count1++;
			route.add(top[0]);
			return new RouteImpl(route, false);
		}
		
		route.add(re);
		
		while (route.size() < this.ttl){
			for (int j = 0; j < top.length; j++){
				list.removeElement(top[j]);
				if (top[j] != -1){
					contacted[top[j]] = true;
				if (this.idSpaceBI.getPartitions()[top[j]].contains(target)) {
					return new RouteImpl(route, true);
				}
				if (this.dsl != null
						&& this.dsl.getStorageForNode(top[j]).containsId(target)) {
					return new RouteImpl(route, true);
				}
				}
			}
			Vector<Integer> conRound = new Vector<Integer>();
			for (int j = 0; j < top.length; j++){
				if (top[j] != -1){
					int[] nextj = this.getNext(top[j], target, rand, nodes);
					   for (int k = 0; k < nextj.length; k++){
						   if (nextj[k] == -1 || !conRound.contains(nextj[k])){
							list.add(nextj[k]);
							conRound.add(nextj[k]);
						   }
						}
				} else {
					for (int k = 0; k < this.beta; k++){
						   list.add(-1);
					}
				}
			//	}
			}
			top = this.getTopAlpha(list, target, rand, nodes,contacted);
			re = getRep(top);
			if (re == -1){
				route.add(current);
				return new RouteImpl(route, false);
			}
			route.add(re);
		}
		return new RouteImpl(route, false);
	}
	
	private int getRep(int[] top){
		int res = -1;
		int att = 0;
		for (int i = 0; i < top.length; i++){
			if (top[i] != -1 && !isAtt[top[i]]){
				res = top[i];
			} 
				else {
				att++;
			}

		}
		//System.out.println(att);
		return res;
	}
	
	private int[] getTopAlpha(Vector<Integer> list,
			BIIdentifier target, Random rand, Node[] nodes, boolean[] contacted){
		int[] top = new int[this.alpha];
		BigInteger[] dists = new BigInteger[this.alpha];
		for (int i = 0; i < top.length; i++){
			top[i] = -1;
			dists[i] = this.idSpaceBI.getMaxDistance();
		}
		for (int i = 0; i < list.size(); i++){
			int curEl = list.get(i);
			BigInteger dist;
			if (curEl != -1){
				dist = this.pBI[curEl].distance(target);
				} else {
					dist = BigInteger.ONE;
				}		
			if (curEl == -1 || !contacted[curEl]){
			for (int j = 0; j < this.alpha; j++){
				if (dist.compareTo(dists[j]) == -1){
					for (int k = this.alpha-1; k > j; k--){
						dists[k] = dists[k-1];
						top[k] = top[k-1];
					}
					dists[j] = dist;
					top[j] = curEl;
					break;
				}
			}
			}
				
		}
		
		return top;
	}
	
	private int[] getNext(int current,
			BIIdentifier target, Random rand, Node[] nodes){
		int[] next = new int[this.beta];
		if (this.isAtt[current]){
			for (int j = 0; j < next.length; j++){
				next[j] = -1;
			}
			return next;
		}
		BigInteger[] dists = new BigInteger[this.beta];
		for (int i = 0; i < next.length; i++){
			next[i] = -1;
			dists[i] = this.idSpaceBI.getMaxDistance();
		}
//		BigInteger currentDist = this.idSpaceBI.getPartitions()[current]
//				.distance(target);
		for (int out : nodes[current].getOutgoingEdges()) {
			BigInteger dist = this.pBI[out].distance(target);
			//if (this.isAtt[out]) 
			//System.out.println(dist);
			//if (dist.compareTo(currentDist) == -1) {
				for (int j = 0; j < this.beta; j++){
					if (dist.compareTo(dists[j]) == -1){
						for (int k = this.beta-1; k > j; k--){
							dists[k] = dists[k-1];
							next[k] = next[k-1];
						}
						dists[j] = dist;
						next[j] = out;
						break;
					}
				}
			//}
		}
		return next;
	}

	

	@Override
	public boolean applicable(Graph graph) {
		return graph.hasProperty("ID_SPACE_0")
				&& graph.getProperty("ID_SPACE_0") instanceof KademliaIdentifierSpace;
				//&& graph.hasProperty("ATTACKER_0");
	}

	@Override
	public void preprocess(Graph graph) {
		GraphProperty p = graph.getProperty("ID_SPACE_0");
		this.idSpaceBI = (KademliaIdentifierSpace) p;
		this.pBI = (KademliaPartition[]) this.idSpaceBI.getPartitions();
		p = graph.getProperty("ATTACKER_0");
		if (p != null){
		this.isAtt = ((AddAttackers)p).getAttackers();
		this.target = ((AddAttackers)p).getTarget();
		} else {
		 this.isAtt = new boolean[graph.getNodes().length];	
		}
		if (graph.hasProperty("DATA_STORAGE_0")) {
			this.dsl = (DataStorageList) graph.getProperty("DATA_STORAGE_0");
		}
	}

}
