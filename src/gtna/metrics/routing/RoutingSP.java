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
 * RoutingSP.java
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
import gtna.graph.Node;
import gtna.graph.spanningTree.SpanningTree;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.routing.Route;
import gtna.routing.RoutingAlgorithm;
import gtna.util.Config;
import gtna.util.Distribution;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.ParameterListParameter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

/**
 * @author stef
 *
 */
public class RoutingSP extends Metric {

	
	private Route[] routes;
	
	
	public static double precision = 0.01;

	private double spEdgesAll;
	private double spEdgesAv;
	private double tree; 
	private double[][] spR;
	private double[][] percEdges;

	public RoutingSP() {
		super("ROUTINGSP");

	}





	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return m.containsKey("ROUTING") && m.get("ROUTING") instanceof Routing && g.hasProperty("SPANNINGTREE");
	}

	@Override
	public void computeData(Graph graph, Network network,
			HashMap<String, Metric> metrics) {
//		if (!metrics.containsKey("ROUTING")) {
//			System.err
//					.println("There is no metric called ROUTING -- cannot evaluate ROUTINGSP");
//			return;
//		}
//		if (!(metrics.get("ROUTING") instanceof Routing)) {
//			System.err
//					.println("There is a metric called ROUTING that does not contain routing -- cannot evaluate ROUTINGSP");
//			return;
//		}
		Routing routing = (Routing) metrics.get("ROUTING");

		this.routes = routing.getRoutes();
//		this.ra.preprocess(graph);
//		Random rand = new Random();
//		this.routes = new Route[graph.getNodes().length * this.routesPerNode];
//		int index = 0;
//		if (isStretch){
//        this.shortestPaths = new short[routes.length];
//		}
//		int parallel = Config.getInt("PARALLEL_ROUTINGS");
//		if (parallel == 1) {
//			for (Node start : graph.getNodes()) {
//				if (this.isStretch){
//				   short[] paths = this.getShortestPaths(graph, start.getIndex());
//				
//				for (int i = 0; i < this.routesPerNode; i++) {
//					this.routes[index] = ra.routeToRandomTarget(graph,
//							start.getIndex(), rand);
//					if (this.routes[index].isSuccessful()){
//						this.shortestPaths[index] = paths[this.routes[index].getLastNode()];
//					}
//					index++;
//				}
//				} else {
//					for (int i = 0; i < this.routesPerNode; i++) {
//						this.routes[index] = ra.routeToRandomTarget(graph,
//								start.getIndex(), rand);
//						index++;
//					}
//				}
//			}
//		} else {
//			RoutingThread[] threads = new RoutingThread[parallel];
//			for (int i = 0; i < threads.length; i++) {
//				int start = graph.getNodes().length / threads.length * i;
//				int end = graph.getNodes().length / threads.length * (i + 1)
//						- 1;
//				if (i == threads.length - 1) {
//					end = graph.getNodes().length - 1;
//				}
//				threads[i] = new RoutingThread(start, end, this.routesPerNode,
//						graph, ra, rand, this);
//				threads[i].start();
//			}
//			index = 0;
//			for (RoutingThread t : threads) {
//				try {
//					t.join();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				for (Route r : t.getRoutes()) {
//					this.routes[index++] = r;
//				}
//			}
//		}
//
//		this.hopDistribution = this.computeHopDistribution();
//		this.hopDistributionAbsolute = this.computeHopDistributionAbsolute();
//		this.betweennessCentrality = this.computeBetweennessCentrality(graph
//				.getNodes().length);
//
//		this.successRate = this.computeSuccessRate();
//		this.failureRate = 1 - this.successRate;
        this.computeSPEdges(graph);
//		if (this.isStretch){
//		  this.computeStretchDistribution();
//		}
	}
	
	private void computeSPEdges(Graph g){
		SpanningTree sp = (SpanningTree) g.getProperty("SPANNINGTREE");
		this.spEdgesAll = 0;
		this.spEdgesAv = 0;
		int t;
		int edges = 0;
		int curedges;
		int index = 0;
		for (Route route : this.routes) {
			if (route.isSuccessful()){
			index++;
			}	
		}
		double[] distE = new double[index];
		double[] distT = new double[index];
		index = 0;
		for (int i = 0; i < this.getRoutes().length; i++) {
			Route route = this.getRoutes()[i];
			if (route.isSuccessful()){
			int[] nodes = route.getRoute();
			t = 0;
			curedges = nodes.length-1;
			edges = edges + curedges;
			for (int j = 0; j < curedges; j++){
				if (sp.getParent(nodes[j]) == nodes[j+1] || sp.getParent(nodes[j+1]) == nodes[j]){
					t++;
				}
				
			}
			this.spEdgesAll = this.spEdgesAll + t;	
			distE[index] = t/(double)curedges;
			distT[index] = sp.getTreeDistance(nodes[0], nodes[nodes.length-1])/(double)curedges;
			this.tree = this.tree + distT[index];
			this.spEdgesAv = this.spEdgesAv + distE[index];
			index++;
			}	
		}
		this.spEdgesAll = this.spEdgesAll/(double)edges;
		this.spEdgesAv = this.spEdgesAv/(double)index;
		this.tree = this.tree/(double)index;
		Arrays.sort(distE);
		int k = 0;
		int i = 0;
		int m = (int)Math.ceil((distE[distE.length-1])/RoutingSP.precision)+1;
		int c = 0;
		this.percEdges = new double[m][2];
		while (i < distE.length){
			while (i < distE.length && distE[i] <= k*RoutingSP.precision){
				c++;
				i++;
			}
			
			this.percEdges[k][0] =  k*RoutingStretch.precision;
			this.percEdges[k][1] = (double)c/(double)distE.length;
			k++;
		}
		
		Arrays.sort(distT);
		k = 0;
		i = 0;
		m = (int)Math.ceil((distT[distT.length-1])/RoutingSP.precision)+1;
		c = 0;
		this.spR = new double[m][2];
		while (i < distT.length){
			while (i < distT.length && distT[i] <= k*RoutingSP.precision+1){
				c++;
				i++;
			}
			
			this.spR[k][0] = 1 + k*RoutingStretch.precision;
			this.spR[k][1] = (double)c/(double)distT.length;
			k++;
		}
		
	}

//	private double computeSuccessRate() {
//		int success = 0;
//		for (Route route : this.routes) {
//			if (route.isSuccessful()) {
//				success++;
//			}
//		}
//		return (double) success / (double) this.routes.length;
//	}
//
//	private double[] computeBetweennessCentrality(int nodes) {
//		double[] bc = new double[nodes];
//		for (Route route : this.routes) {
//			for (int i = 1; i < route.getRoute().length - 1; i++) {
//				bc[route.getRoute()[i]]++;
//			}
//		}
//		Arrays.sort(bc);
//		for (int i = 0; i < bc.length; i++) {
//			bc[i] /= (double) this.routes.length;
//		}
//		return bc;
//	}
//
//	private Distribution computeHopDistribution() {
//		long[] hops = new long[1];
//		long counter = 0;
//		for (Route route : this.routes) {
//			if (route.isSuccessful()) {
//				hops = this.inc(hops, route.getHops());
//				counter++;
//			}
//		}
//		return new Distribution(hops, counter);
//	}
//
//	private Distribution computeHopDistributionAbsolute() {
//		long[] hops = new long[1];
//		for (Route route : this.routes) {
//			if (route.isSuccessful()) {
//				hops = this.inc(hops, route.getHops());
//			}
//		}
//		return new Distribution(hops, this.routes.length);
//	}
	

	


	private long[] inc(long[] values, int index) {
		try {
			values[index]++;
			return values;
		} catch (ArrayIndexOutOfBoundsException e) {
			long[] valuesNew = new long[index + 1];
			System.arraycopy(values, 0, valuesNew, 0, values.length);
			valuesNew[index] = 1;
			return valuesNew;
		}
	}

	public Route[] getRoutes() {
		return this.routes;
	}

	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithoutIndex(this.spR,
				"ROUTINGSP_TREESTRETCH_DISTRIBUTION", folder);
		success &= DataWriter.writeWithoutIndex(this.percEdges,
				"ROUTINGSP_TREEEDGE_DISTRIBUTION", folder);
		return success;
	}

	@Override
	public Single[] getSingles() {
//		Single averageHops = new Single("ROUTINGSP_HOPS_AVG",
//				this.hopDistribution.getAverage());
//		Single medianHops = new Single("ROUTINGSP_HOPS_MED",
//				this.hopDistribution.getMedian());
//		Single maximumHops = new Single("ROUTINGSP_HOPS_MAX",
//				this.hopDistribution.getMax());
//
//		Single successRate = new Single("ROUTINGSP_SUCCESS_RATE",
//				this.successRate);
//		Single failureRate = new Single("ROUTINGSP_FAILURE_RATE",
//				this.failureRate);
		Single treeEdges = new Single("ROUTINGSP_TREE_EDGES",
				this.spEdgesAll);
		Single treeEdgesA = new Single("ROUTINGSP_TREE_EDGES_AVG",
				this.spEdgesAv);
		Single treestretch = new Single("ROUTINGSP_TREESTRETCH",
				this.tree);
//		if (this.isStretch){
//		Single stretch = new Single("ROUTINGSP_STRETCH",
//				this.stretch);
//		return new Single[] { averageHops, medianHops, maximumHops,
//				successRate, failureRate,stretch,treeEdges, treeEdgesA,treestretch };
//		} else {
//			return new Single[] { averageHops, medianHops, maximumHops,
//					successRate, failureRate, treeEdges, treeEdgesA, treestretch};
//		}
		return new Single[] { treeEdges, treeEdgesA, treestretch};
	}

	
	
	

}
