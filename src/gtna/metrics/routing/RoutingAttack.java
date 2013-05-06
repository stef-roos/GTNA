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
 * RoutingAttack.java
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
package gtna.metrics.routing;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.graph.Node;
import gtna.id.BIIdentifier;
import gtna.id.BIIdentifierSpace;
import gtna.id.BIPartition;
import gtna.id.Partition;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.routing.Route;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.attack.AddAttackers;
import gtna.util.Config;
import gtna.util.Distribution;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Random;

/**
 * @author stefanie
 *
 */
public class RoutingAttack extends Routing {

	/**
	 * @param ra
	 * @param routesPerNode
	 */
	public RoutingAttack(RoutingAlgorithm ra) {
		super(ra);
	}
	
	@Override
	public void computeData(Graph graph, Network network,
			HashMap<String, Metric> metrics) {
		this.ra.preprocess(graph);
		Random rand = new Random();
		int att = 0;
		GraphProperty p = graph.getProperty("ATTACKER_0");
		GraphProperty p1 = graph.getProperty("ID_SPACE_0");
		BIIdentifierSpace idSpaceBI = (BIIdentifierSpace) p1;
		Partition<BigInteger>[] pBI = idSpaceBI.getPartitions();
		boolean[] isAtt;
		BIIdentifier target;
		if (p != null){
		   isAtt = ((AddAttackers)p).getAttackers();
		   target = ((AddAttackers)p).getTarget();
		   
		for (int i = 0; i < isAtt.length; i++){
			if (isAtt[i]){
				att++;
			}
			this.routes = new Route[graph.getNodes().length-att-1];
		}}else {
			isAtt = new boolean[graph.getNodes().length];
			target = null;
			this.routes = new Route[graph.getNodes().length-att];
		}
		
		
		
		int index = 0;
        for (Node start : graph.getNodes()) {
				if (!isAtt[start.getIndex()] && (target == null ||
						!pBI[start.getIndex()].contains(target))){
				this.routes[index++] = ra.routeToRandomTarget(graph,
							start.getIndex(), rand);
				}
			}
        this.hopDistribution = this.computeHopDistribution();
		this.hopDistributionAbsolute = this.computeHopDistributionAbsolute();
		this.betweennessCentrality = this.computeBetweennessCentrality(graph
				.getNodes().length);

		this.successRate = this.computeSuccessRate();
		this.failureRate = 1 - this.successRate;
	}
	
	protected Distribution computeHopDistribution() {
		long[] hops = new long[1];
		long counter = 0;
		for (Route route : this.routes) {
			//if (route.isSuccessful()) {
				hops = this.inc(hops, route.getHops());
				counter++;
			//}
		}
		return new Distribution(hops, counter);
	}

}
