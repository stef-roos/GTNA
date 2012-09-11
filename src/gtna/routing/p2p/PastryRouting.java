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
 * PastryRouting.java
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
import gtna.networks.p2p.chord.ChordIdentifier;
import gtna.networks.p2p.pastry.PastryIdentifier;
import gtna.networks.p2p.pastry.PastryIdentifierSpace;
import gtna.networks.p2p.pastry.PastryPartition;
import gtna.routing.Route;
import gtna.routing.RouteImpl;
import gtna.routing.RoutingAlgorithm;
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
public class PastryRouting extends RoutingAlgorithm {


	private PastryIdentifierSpace idSpaceBI;

	private PastryPartition[] pBI;

	private DataStorageList dsl;

	private int ttl;

	public PastryRouting() {
		super("PASTRY_ROUTING");
		this.ttl = Integer.MAX_VALUE;
	}

	public PastryRouting(int ttl) {
		super("PASTRY_ROUTING", new Parameter[] { new IntParameter("TTL", ttl) });
		this.ttl = ttl;
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
		BIIdentifier target = (BIIdentifier) part[rand.nextInt(part.length)].getRepresentativeID();
		while (this.pBI[start].contains(target)) {
			target = (BIIdentifier) this.idSpaceBI.randomID(rand);
		}
		return this.routeBI(new ArrayList<Integer>(), start, target, rand,
				graph.getNodes());
	}

	private Route routeBI(ArrayList<Integer> route, int current,
			BIIdentifier target, Random rand, Node[] nodes) {
		route.add(current);
		//System.out.println("At " + this.pBI[current].getSucc().getId() + " " + ((PastryIdentifier)target).getId() +
			//	" distance " + this.pBI[current].distance(target).bitLength());
		if (this.idSpaceBI.getPartitions()[current].contains(target)) {
			//System.out.println("Found");
			return new RouteImpl(route, true);
		}
		if (this.dsl != null
				&& this.dsl.getStorageForNode(current).containsId(target)) {
			return new RouteImpl(route, true);
		}
		if (route.size() > this.ttl) {
			return new RouteImpl(route, false);
		}
		int L = this.idSpaceBI.getL();
		for (int out = 1; out <= L/2; out++){
			int next = (current +out) % nodes.length;
			if (this.pBI[next].contains(target)){
				return this.routeBI(route, next, target, rand, nodes);
			}
			next = current -out >-1?current-out:current-out+nodes.length;
			if (this.pBI[next].contains(target)){
				return this.routeBI(route, next, target, rand, nodes);
			}
		}
		int currentDist = this.idSpaceBI.getPartitions()[current]
				.distance(target).bitLength();
		int minDist = this.idSpaceBI.getBits();
		int minNode = -1;
		Vector<Integer> best = new Vector<Integer>();
		for (int out : nodes[current].getOutgoingEdges()) {
			int dist = this.pBI[out].distance(target).bitLength();
			//System.out.println(dist + " " + currentDist);
			if (dist <= currentDist){
				if (dist == minDist){
					best.add(out);
				}
			if (dist < minDist) {
				minDist = dist;
				best = new Vector<Integer>();
				best.add(out);
			}
			
			}
		}
		if (best.size() == 1){
			minNode = best.get(0);
		} else {
			BigInteger min = this.idSpaceBI.getMaxDistance();
			BigInteger id;
			BigInteger t = ((PastryIdentifier)target).getId();
			for (int i = 0; i < best.size(); i++){
				id = this.pBI[best.get(i)].getSucc().getId();
				BigInteger diff = id.subtract(t).abs();
				if (diff.compareTo(this.idSpaceBI.getMaxDistance()) == 1){
					diff = this.idSpaceBI.getModulus().subtract(diff);
				}
				if (diff.compareTo(min) == -1){
					min = diff;
					minNode = best.get(i);
				}
			}
		}
		
		if (minNode == -1) {
			return new RouteImpl(route, false);
		}
		return this.routeBI(route, minNode, target, rand, nodes);
	}

	

	@Override
	public boolean applicable(Graph graph) {
		return graph.hasProperty("ID_SPACE_0")
				&& graph.getProperty("ID_SPACE_0") instanceof PastryIdentifierSpace;
	}

	@Override
	public void preprocess(Graph graph) {
		GraphProperty p = graph.getProperty("ID_SPACE_0");
		this.idSpaceBI = (PastryIdentifierSpace) p;
		this.pBI = (PastryPartition[]) this.idSpaceBI.getPartitions();
		
		if (graph.hasProperty("DATA_STORAGE_0")) {
			this.dsl = (DataStorageList) graph.getProperty("DATA_STORAGE_0");
		}
	}

}
