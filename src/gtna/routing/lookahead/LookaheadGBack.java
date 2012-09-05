
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
 * LookaheadGreedy.java
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
package gtna.routing.lookahead;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.id.BIIdentifierSpace;
import gtna.id.BIPartition;
import gtna.id.DIdentifierSpace;
import gtna.id.DPartition;
import gtna.id.Identifier;
import gtna.id.IdentifierSpace;
import gtna.id.Partition;
import gtna.id.data.DataStorageList;
import gtna.id.lookahead.LookaheadElement;
import gtna.id.lookahead.LookaheadList;
import gtna.id.lookahead.LookaheadLists;
import gtna.routing.Route;
import gtna.routing.RouteImpl;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.lookahead.Lookahead.ViaSelection;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/**
 * @author stef
 *
 */
public class LookaheadGBack extends RoutingAlgorithm {

	protected int ttl;

	protected ViaSelection viaSelection;

	protected IdentifierSpace idSpace;

	protected Partition[] p;

	protected LookaheadLists lists;

	private DataStorageList dsl;
	
	private double greedy;

	

	public LookaheadGBack(ViaSelection viaSelection) {
		this(Integer.MAX_VALUE,viaSelection,0);
	}

	public LookaheadGBack(int ttl, ViaSelection viaSelection, double greedy) {
		super("LGB", new Parameter[] { new IntParameter("TTL", ttl), 
				new StringParameter("VIA", viaSelection.toString()),
				new DoubleParameter("GREEDY", greedy)});
		this.ttl = ttl;
		this.viaSelection = viaSelection;
		this.greedy = greedy;
	}

	@Override
	public Route routeToRandomTarget(Graph graph, int start, Random rand) {
		Identifier target = this.idSpace.randomID(rand);
		while (this.p[start].contains(target)) {
			target = this.idSpace.randomID(rand);
		}
		return this.route(new ArrayList<Integer>(), start, target, rand,
				graph.getNodes(), new HashMap<Integer, Integer>());
	}

	@Override
	public Route routeToTarget(Graph graph, int start, Identifier target,
			Random rand) {
		return this.route(new ArrayList<Integer>(), start, target, rand,
				graph.getNodes(), new HashMap<Integer, Integer>());
	}

	private Route route(ArrayList<Integer> route, int current,
			Identifier target, Random rand, Node[] nodes, HashMap<Integer,Integer> from) {
		route.add(current);
		if (this.idSpace.getPartitions()[current].contains(target)) {
			return new RouteImpl(route, true);
		}
		if (this.dsl != null
				&& this.dsl.getStorageForNode(current).containsId(target)) {
			return new RouteImpl(route, true);
		}
		if (route.size() > this.ttl) {
			return new RouteImpl(route, false);
		}
		//greedy check
		if (this.idSpace instanceof DIdentifierSpace){
			DIdentifierSpace idSpaceD = (DIdentifierSpace)this.idSpace;
			DPartition[] pD = (DPartition[]) idSpaceD.getPartitions();
		double currentDist = idSpaceD.getPartitions()[current]
				.distance(target);
		double minDist = idSpaceD.getMaxDistance();
		int minNode = -1;
		for (int out : nodes[current].getOutgoingEdges()) {
			double dist = pD[out].distance(target);
			if (dist < minDist && dist < currentDist && !from.containsKey(out)) {
				minDist = dist;
				minNode = out;
			}
		}
		if (minDist <= this.greedy){
			from.put(minNode, current);
			return this.route(route, minNode, target, rand, nodes, from);
		}
		} else {
			BIIdentifierSpace idSpaceBI = (BIIdentifierSpace)this.idSpace;
			BIPartition[] pBI = (BIPartition[]) idSpaceBI.getPartitions();
			BigInteger currentDist = idSpaceBI.getPartitions()[current]
					.distance(target);
			BigInteger minDist = idSpaceBI.getMaxDistance();
			int minNode = -1;
			for (int out : nodes[current].getOutgoingEdges()) {
				BigInteger dist = pBI[out].distance(target);
				if (dist.compareTo(minDist) == -1
						&& dist.compareTo(currentDist) == -1 &&
						!from.containsKey(out)) {
					minDist = dist;
					minNode = out;
				}
			}
			if (minDist.doubleValue() <= this.greedy){
				from.put(minNode, current);
				return this.route(route, minNode, target, rand, nodes, from);
			}
		}
		
		
		LookaheadList list = this.lists.getList(current);

		int via = -1;
          
		if (list.getList().length == 0) {
			return new RouteImpl(route, false);
		}

		if (list.getList()[0].getPartition() instanceof DPartition) {
			double currentDist = (Double) this.p[current].distance(target);
			double minDist = (Double) this.idSpace.getMaxDistance();
			if (this.viaSelection == ViaSelection.sequential) {
				for (LookaheadElement l : list.getList()) {
					double dist = ((DPartition) l.getPartition())
							.distance(target);
					if (dist < minDist && dist < currentDist
							&& !from.containsKey(l.getVia())) {
						minDist = dist;
						via = l.getVia();
					}
				}
			} else if (this.viaSelection == ViaSelection.minVia) {
				ArrayList<LookaheadElement> best = new ArrayList<LookaheadElement>();
				for (LookaheadElement l : list.getList()) {
					double dist = ((DPartition) l.getPartition())
							.distance(target);
					if (dist < minDist && dist < currentDist
							&& !from.containsKey(l.getVia())) {
						best.clear();
						minDist = dist;
						best.add(l);
					} else if (dist == minDist && !from.containsKey(l.getVia())) {
						best.add(l);
					}
				}
				if (best.size() == 1) {
					via = best.get(0).getVia();
				} else if (best.size() > 1) {
					via = best.get(0).getVia();
					minDist = ((DPartition) this.p[best.get(0).getVia()])
							.distance(target);
					for (int i = 1; i < best.size(); i++) {
						double dist = ((DPartition) this.p[best.get(i).getVia()])
								.distance(target);
						if (dist < minDist) {
							minDist = dist;
							via = best.get(i).getVia();
						}
					}
				}
			}
		} else if (list.getList()[0].getPartition() instanceof BIPartition) {
			BigInteger currentDist = (BigInteger) this.p[current]
					.distance(target);
			BigInteger minDist = (BigInteger) this.idSpace.getMaxDistance();
			if (this.viaSelection == ViaSelection.sequential) {
				for (LookaheadElement l : list.getList()) {
					BigInteger dist = ((BIPartition) l.getPartition())
							.distance(target);
					if (dist.compareTo(minDist) == -1
							&& dist.compareTo(currentDist) == -1
							&& !from.containsKey(l.getVia())) {
						minDist = dist;
						via = l.getVia();
					}
				}
			} else if (this.viaSelection == ViaSelection.minVia) {
				ArrayList<LookaheadElement> best = new ArrayList<LookaheadElement>();
				for (LookaheadElement l : list.getList()) {
					BigInteger dist = ((BIPartition) l.getPartition())
							.distance(target);
					if (dist.compareTo(minDist) == -1
							&& dist.compareTo(currentDist) == -1
							&& !from.containsKey(l.getVia())) {
						best.clear();
						minDist = dist;
						best.add(l);
					} else if (dist.equals(minDist)
							&& !from.containsKey(l.getVia())) {
						best.add(l);
					}
				}
				if (best.size() == 1) {
					via = best.get(0).getVia();
				} else if (best.size() > 1) {
					via = best.get(0).getVia();
					minDist = ((BIPartition) this.p[best.get(0).getVia()])
							.distance(target);
					for (int i = 1; i < best.size(); i++) {
						BigInteger dist = ((BIPartition) this.p[best.get(i)
								.getVia()]).distance(target);
						if (dist.compareTo(minDist) == -1) {
							minDist = dist;
							via = best.get(i).getVia();
						}
					}
				}
			}
		} else {
			return null;
		}

		if (via == -1) {
			Integer pre  = from.get(current);
			if (pre == null){
			  return new RouteImpl(route, false);
			} else {
				via = pre;
			}
		} else {
			from.put(via, current);
		}
		return this.route(route, via, target, rand, nodes, from);
	}

	@Override
	public boolean applicable(Graph graph) {
		return graph.hasProperty("LOOKAHEAD_LIST_0")
				&& graph.getProperty("LOOKAHEAD_LIST_0") instanceof LookaheadLists;
	}

	@Override
	public void preprocess(Graph graph) {
		this.idSpace = (IdentifierSpace) graph.getProperty("ID_SPACE_0");
		this.p = (Partition[]) this.idSpace.getPartitions();
		this.lists = (LookaheadLists) graph.getProperty("LOOKAHEAD_LIST_0");
		if (graph.hasProperty("DATA_STORAGE_0")) {
			this.dsl = (DataStorageList) graph.getProperty("DATA_STORAGE_0");
		}
	}

}
