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
 * DepthFirstGreedy.java
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
package gtna.routing.greedyVariations;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.graph.Node;
import gtna.id.BIIdentifierSpace;
import gtna.id.DIdentifier;
import gtna.id.DIdentifierSpace;
import gtna.id.DPartition;
import gtna.id.Identifier;
import gtna.id.data.DataStorageList;
import gtna.routing.Route;
import gtna.routing.RouteImpl;
import gtna.routing.RoutingAlgorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * a weighted depth first search (= Freenet routing)
 * 
 * @author stefanie
 * 
 */
public class ModerateDFS extends RoutingAlgorithm{
	int ttl;
	private DIdentifierSpace idSpaceD;

	private DPartition[] pD;
	
	private DataStorageList dsl;
	
	boolean[] seen;
	HashMap<Integer, int[]> from;
	boolean[][] contacted;

	public ModerateDFS() {
		super("MODERATE_DFS");
		this.ttl = Integer.MAX_VALUE;
	}

	public ModerateDFS(int ttl) {
		super("MODERATE_DFS");
		this.ttl = ttl;
	}

	@Override
	public Route routeToRandomTarget(Graph graph, int start, Random rand) {
		if (this.idSpaceD != null) {
			return this.routeToRandomTargetD(graph, start, rand);
		} else {
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Route routeToTarget(Graph graph, int start, Identifier target,
			Random rand) {
		if (this.idSpaceD != null) {
			return this.routeD(new ArrayList<Integer>(), start,
					(DIdentifier) target, rand, graph.getNodes(),-1);
		} else {
			return null;
		}
	}

	

	private Route routeToRandomTargetD(Graph graph, int start, Random rand) {
		DIdentifier target = (DIdentifier) this.idSpaceD.randomID(rand);
		while (this.pD[start].contains(target)) {
			target = (DIdentifier) this.idSpaceD.randomID(rand);
		}
		seen = new boolean[graph.getNodes().length];
		contacted = new boolean[seen.length][];
		from = new HashMap<Integer,int[]>();
		return this.routeD(new ArrayList<Integer>(), start, target, rand,
				graph.getNodes(), -1);
	}

	private Route routeD(ArrayList<Integer> route, int current,
			DIdentifier target, Random rand, Node[] nodes, int last) {
		route.add(current);
		if (last ==-1 && seen[current]){
			int[] pres = from.get(current);
			return this.routeD(route, pres[1]==-1?pres[0]:pres[1], target, rand, nodes, current);
		}
		if (contacted[current] == null){
			contacted[current] = new boolean[nodes[current].getOutDegree()];
		}
		if (this.idSpaceD.getPartitions()[current].contains(target)) {
			return new RouteImpl(route, true);
		}
		if (this.dsl != null
				&& this.dsl.getStorageForNode(current).containsId(target)) {
			return new RouteImpl(route, true);
		}
		if (route.size() > this.ttl) {
			return new RouteImpl(route, false);
		}
		double currentDist = this.idSpaceD.getPartitions()[current]
				.distance(target);
		double minDist = this.idSpaceD.getMaxDistance();
		int minNode = -1;
		int minIndex = -1;
		int[] out = nodes[current].getOutgoingEdges();
		for(int i = 0; i < contacted[current].length; i++){
			if (out[i] == last){
				contacted[current][i] = true;
			}
			double dist = this.pD[out[i]].distance(target);
			if (dist < minDist && !seen[out[i]] && !contacted[current][i]) {
				minDist = dist;
				minNode = out[i];
				minIndex = i;
			}
		}
		if (minDist >= currentDist){
			seen[current] = true;
		} else {
			contacted[current][minIndex] = true;
		}
		if (minNode == -1) {
			int[] pre = from.get(current);
			if (pre == null)
			return new RouteImpl(route, false);
			else {
				return this.routeD(route, pre[0], target, rand, nodes, current);	
			}
		} else{
		   int[] pre = from.get(minNode);
		   if (pre == null){
			   pre = new int[2];
			   pre[0] = current;
			   pre[1] = -1;
			   from.put(minNode, pre);
		   } else {
			   pre[1] = current;
		   }
		}
		if (minDist < currentDist){
		   return this.routeD(route, minNode, target, rand, nodes, -1);
		} else {
			 return this.routeD(route, minNode, target, rand, nodes, current);
		}
			
	}

	@Override
	public boolean applicable(Graph graph) {
		return graph.hasProperty("ID_SPACE_0")
				&& (graph.getProperty("ID_SPACE_0") instanceof DIdentifierSpace || graph
						.getProperty("ID_SPACE_0") instanceof BIIdentifierSpace);
	}

	@Override
	public void preprocess(Graph graph) {
		GraphProperty p = graph.getProperty("ID_SPACE_0");
		if (p instanceof DIdentifierSpace) {
			this.idSpaceD = (DIdentifierSpace) p;
			this.pD = (DPartition[]) this.idSpaceD.getPartitions();
		} else  {
			this.idSpaceD = null;
			this.pD = null;
		}
		if (graph.hasProperty("DATA_STORAGE_0")) {
			this.dsl = (DataStorageList) graph.getProperty("DATA_STORAGE_0");
		}
	}

}
