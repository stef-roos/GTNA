/* ===========================================================
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
 * KademliaRouting.java
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
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

/**
 * @author stef
 *
 */
public class KademliaRoutingFailure extends RoutingAlgorithm {

	private KademliaIdentifierSpace idSpaceBI;

	private KademliaPartition[] pBI;

	private DataStorageList dsl;

	private int ttl;
	
	private int alpha;
	
	private int beta;
	
	private double fprob;

	public KademliaRoutingFailure(int alpha, int beta, double fprob) {
		super("KADEMLIA_ROUTING_FAILURE", new Parameter[] { new DoubleParameter("FAIL_RATE",fprob)
				});
		this.ttl = Integer.MAX_VALUE;
		this.alpha = alpha;
		this.beta = beta;
		this.fprob = fprob;
	}

	public KademliaRoutingFailure(int ttl, int alpha, int beta, double fprob) {
		super("KADEMLIA_ROUTING_FAILURE", new Parameter[] { new DoubleParameter("FAIL_RATE",fprob),new IntParameter("TTL", ttl) });
		this.ttl = ttl;
		this.alpha = alpha;
		this.beta = beta;
		this.fprob = fprob;
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
		//BIIdentifier target = (BIIdentifier) part[rand.nextInt(part.length)].getRepresentativeID();
		BIIdentifier target = (BIIdentifier) this.idSpaceBI.randomID(rand);
		while (this.pBI[start].contains(target)) {
			target = (BIIdentifier) part[rand.nextInt(part.length)].getRepresentativeID();
			//target = (BIIdentifier) this.idSpaceBI.randomID(rand);
		}
		return this.routeBI(new ArrayList<Integer>(), start, target, rand,
				graph.getNodes());
	}

	private Route routeBI(ArrayList<Integer> route, int current,
			BIIdentifier target, Random rand, Node[] nodes) {
		//System.out.println("Routing");
		route.add(current);
		boolean[] contacted = new boolean[nodes.length];
		contacted[current] = true;
		Vector<Integer> list = new Vector<Integer>();
		int[] out =nodes[current].getOutgoingEdges();
		for (int i = 0; i < out.length; i++){
			list.add(out[i]);
		}
		int[] top = this.getTopAlpha(list, target, rand, nodes,contacted);
		route.add(top[0]);
		int[] next = new int[beta*alpha];
		
		while (route.size() < this.ttl){
			//System.out.println("iter");
			for (int j = 0; j < top.length; j++){
				if (top[j] != -1){
					list.removeElement(top[j]);
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
			for (int j = 0; j < top.length; j++){
				if (top[j] != -1){
					if (rand.nextDouble() >= this.fprob){
					   int[] nextj = this.getNext(top[j], target, rand, nodes);
					   for (int k = 0; k < nextj.length; k++){
							list.add(nextj[k]);
						}
					} 
					
				} 
			//	}
			}
			top = this.getTopAlpha(list, target, rand, nodes,contacted);
			if (top[0] == -1){
				return new RouteImpl(route, false);
			}
			route.add(top[0]);
		}
		return new RouteImpl(route, false);
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
			if (curEl != -1 && !contacted[curEl]){
			BigInteger dist = this.pBI[curEl].distance(target);
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
		BigInteger[] dists = new BigInteger[this.beta];
		for (int i = 0; i < next.length; i++){
			next[i] = -1;
			dists[i] = this.idSpaceBI.getMaxDistance();
		}
		BigInteger currentDist = this.idSpaceBI.getPartitions()[current]
				.distance(target);
		for (int out : nodes[current].getOutgoingEdges()) {
			BigInteger dist = this.pBI[out].distance(target);
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
	}

	@Override
	public void preprocess(Graph graph) {
		GraphProperty p = graph.getProperty("ID_SPACE_0");
		this.idSpaceBI = (KademliaIdentifierSpace) p;
		this.pBI = (KademliaPartition[]) this.idSpaceBI.getPartitions();
		
		if (graph.hasProperty("DATA_STORAGE_0")) {
			this.dsl = (DataStorageList) graph.getProperty("DATA_STORAGE_0");
		}
	}

}
