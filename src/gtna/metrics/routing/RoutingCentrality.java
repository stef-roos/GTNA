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
 * RoutingCentrality.java
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
package gtna.metrics.routing;

import gtna.data.Single;
import gtna.graph.Graph;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.routing.Route;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @author stef
 *
 */
public class RoutingCentrality extends Metric {
	private int ttl;
    private boolean success;
    private Route[] routes;
    private double[] betweennessCentrality;
	private double maxCentrality;
	private double minCentrality;
	private double meanCentrality;
	private double medianCentrality;

	
	/**
	 * @param ttl: up which ttl should routing be considered
	 * @param success: only consider successful attempts 
	 */
	public RoutingCentrality(int ttl, boolean success) {
		super("ROUTING_CENTRALITY", new Parameter[] {new IntParameter("TTL", ttl), new BooleanParameter("SUCCESS", success)});
		this.success = success;
		this.ttl = ttl;
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#computeData(gtna.graph.Graph, gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		Routing routing = (Routing) m.get("ROUTING");
        this.routes = routing.getRoutes();
        this.betweennessCentrality = this.computeBetweennessCentrality(g.getNodeCount()) ;
	}
	
	private double[] computeBetweennessCentrality(int nodes) {
		double[] bc = new double[nodes];
		int count = 0;
		for (Route route : this.routes) {
			if (!success || (route.isSuccessful() && route.getHops() <= ttl)){
				count++;
			for (int i = 1; i < Math.min(route.getRoute().length - 1,ttl+1); i++) {
				bc[route.getRoute()[i]]++;
			}
		    }
		}
		Arrays.sort(bc);
		double av = 0;
		for (int i = 0; i < bc.length; i++) {
			bc[i] /= (double) count;
			av = av + bc[i];
		}
		this.minCentrality = bc[0];
		this.maxCentrality = bc[nodes-1];
		this.medianCentrality = bc[nodes/2];
		this.meanCentrality = av/(double)nodes;
		return bc;
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#writeData(java.lang.String)
	 */
	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithIndex(this.betweennessCentrality,
				"ROUTING_CENTRALITY_BETWEENNESS_CENTRALITY", folder);
		return success;
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#getSingles()
	 */
	@Override
	public Single[] getSingles() {
		Single avCentral = new Single("ROUTING_CENTRALITY_AVG",
				this.meanCentrality);
		Single medianCentral = new Single("ROUTING_CENTRALITY_MED",
				this.medianCentrality);
		Single minCentral = new Single("ROUTING_CENTRALITY_MIN",
				this.minCentrality);
		Single maxCentral = new Single("ROUTING_CENTRALITY_MAX",
				this.maxCentrality);
		return new Single[] { avCentral, medianCentral,
				minCentral, maxCentral};
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#applicable(gtna.graph.Graph, gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return m.containsKey("ROUTING") && m.get("ROUTING") instanceof Routing ;
	}

}
