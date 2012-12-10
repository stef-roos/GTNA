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

	private RoutingAlgorithm ra;
	private int routesPerNode;

	private Route[] routes;
	private short[] shortestPaths;

	private Distribution hopDistribution;
	private Distribution hopDistributionAbsolute;

	private double[] betweennessCentrality;

	private double successRate;
	private double failureRate;
	private double stretch;
	private double[][] stretchDist;
	public static double precision = 0.01;
	private boolean isStretch;
	private double spEdgesAll;
	private double spEdgesAv;
	private double tree; 
	private double[][] spR;
	private double[][] percEdges;

	public RoutingSP(RoutingAlgorithm ra, boolean isStretch) {
		super("ROUTINGSP", new Parameter[] { new ParameterListParameter(
				"ROUTING_ALGORITHM", ra) });

		this.ra = ra;
		this.routesPerNode = Config.getInt("ROUTING_ROUTES_PER_NODE");
        this.isStretch = isStretch;
		this.init();
	}

	public RoutingSP(RoutingAlgorithm ra, int routesPerNode) {
		super("ROUTING_SP", new Parameter[] {
				new ParameterListParameter("ROUTING_ALGORITHM", ra),
				new IntParameter("ROUTES_PER_NODE", routesPerNode) });

		this.ra = ra;
		this.routesPerNode = routesPerNode;
		this.isStretch = isStretch;
		this.init();
	}

	private void init() {
		this.hopDistribution = new Distribution(new double[] { -1 });
		this.hopDistributionAbsolute = new Distribution(new double[] { -1 });
		this.betweennessCentrality = new double[] { -1 };
		this.routes = new Route[0];
		this.successRate = -1;
		this.failureRate = -1;
	}

	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return this.ra.applicable(g) && g.hasProperty("SPANNINGTREE");
	}

	@Override
	public void computeData(Graph graph, Network network,
			HashMap<String, Metric> metrics) {
		this.ra.preprocess(graph);
		Random rand = new Random();
		this.routes = new Route[graph.getNodes().length * this.routesPerNode];
		int index = 0;
		if (isStretch){
        this.shortestPaths = new short[routes.length];
		}
		int parallel = Config.getInt("PARALLEL_ROUTINGS");
		if (parallel == 1) {
			for (Node start : graph.getNodes()) {
				if (this.isStretch){
				   short[] paths = this.getShortestPaths(graph, start.getIndex());
				
				for (int i = 0; i < this.routesPerNode; i++) {
					this.routes[index] = ra.routeToRandomTarget(graph,
							start.getIndex(), rand);
					if (this.routes[index].isSuccessful()){
						this.shortestPaths[index] = paths[this.routes[index].getLastNode()];
					}
					index++;
				}
				} else {
					for (int i = 0; i < this.routesPerNode; i++) {
						this.routes[index] = ra.routeToRandomTarget(graph,
								start.getIndex(), rand);
						index++;
					}
				}
			}
		} else {
			RoutingThread[] threads = new RoutingThread[parallel];
			for (int i = 0; i < threads.length; i++) {
				int start = graph.getNodes().length / threads.length * i;
				int end = graph.getNodes().length / threads.length * (i + 1)
						- 1;
				if (i == threads.length - 1) {
					end = graph.getNodes().length - 1;
				}
				threads[i] = new RoutingThread(start, end, this.routesPerNode,
						graph, ra, rand, this);
				threads[i].start();
			}
			index = 0;
			for (RoutingThread t : threads) {
				try {
					t.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				for (Route r : t.getRoutes()) {
					this.routes[index++] = r;
				}
			}
		}

		this.hopDistribution = this.computeHopDistribution();
		this.hopDistributionAbsolute = this.computeHopDistributionAbsolute();
		this.betweennessCentrality = this.computeBetweennessCentrality(graph
				.getNodes().length);

		this.successRate = this.computeSuccessRate();
		this.failureRate = 1 - this.successRate;
		this.computeSPEdges(graph);
		if (this.isStretch){
		  this.computeStretchDistribution();
		}
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
			distT[index] = sp.getTreeDistance(nodes[0], nodes[nodes.length-1]);
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

	private double computeSuccessRate() {
		int success = 0;
		for (Route route : this.routes) {
			if (route.isSuccessful()) {
				success++;
			}
		}
		return (double) success / (double) this.routes.length;
	}

	private double[] computeBetweennessCentrality(int nodes) {
		double[] bc = new double[nodes];
		for (Route route : this.routes) {
			for (int i = 1; i < route.getRoute().length - 1; i++) {
				bc[route.getRoute()[i]]++;
			}
		}
		Arrays.sort(bc);
		for (int i = 0; i < bc.length; i++) {
			bc[i] /= (double) this.routes.length;
		}
		return bc;
	}

	private Distribution computeHopDistribution() {
		long[] hops = new long[1];
		long counter = 0;
		for (Route route : this.routes) {
			if (route.isSuccessful()) {
				hops = this.inc(hops, route.getHops());
				counter++;
			}
		}
		return new Distribution(hops, counter);
	}

	private Distribution computeHopDistributionAbsolute() {
		long[] hops = new long[1];
		for (Route route : this.routes) {
			if (route.isSuccessful()) {
				hops = this.inc(hops, route.getHops());
			}
		}
		return new Distribution(hops, this.routes.length);
	}
	

	
	private void computeStretchDistribution(){
		int index = 0;
		double s = 0;
		for (int i = 0; i < this.routes.length; i++){
			if (this.routes[i].isSuccessful()){
				s = s + (double)this.routes[i].getHops()/(double)this.shortestPaths[i];
				index++;
			}
		}
		this.stretch = s/(double)index;
		double[] dist = new double[index];
		index = 0;
		for (int i = 0; i < this.routes.length; i++){
			if (this.routes[i].isSuccessful()){
				dist[index] = (double)this.routes[i].getHops()/(double)this.shortestPaths[i];
				index++;
			}
		}
		Arrays.sort(dist);
		int k = 0;
		int i = 0;
		int m = (int)Math.ceil((dist[dist.length-1]-1)/RoutingSP.precision)+1;
		int c = 0;
		double[][] res = new double[m][2];
		while (i < dist.length){
			while (i < dist.length && dist[i] <= 1 + k*RoutingSP.precision){
				c++;
				i++;
			}
			
			res[k][0] = 1 + k*RoutingStretch.precision;
			res[k][1] = (double)c/(double)dist.length;
			k++;
		}
		this.stretchDist = res;
	}

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
		success &= DataWriter.writeWithIndex(
				this.hopDistribution.getDistribution(),
				"ROUTINGSP_HOP_DISTRIBUTION", folder);
		success &= DataWriter.writeWithIndex(this.hopDistribution.getCdf(),
				"ROUTINGSP_HOP_DISTRIBUTION_CDF", folder);
		success &= DataWriter.writeWithIndex(
				this.hopDistributionAbsolute.getDistribution(),
				"ROUTINGSP_HOP_DISTRIBUTION_ABSOLUTE", folder);
		success &= DataWriter.writeWithIndex(
				this.hopDistributionAbsolute.getCdf(),
				"ROUTINGSP_HOP_DISTRIBUTION_ABSOLUTE_CDF", folder);
		success &= DataWriter.writeWithIndex(this.betweennessCentrality,
				"ROUTINGSP_BETWEENNESS_CENTRALITY", folder);
		success &= DataWriter.writeWithoutIndex(this.spR,
				"ROUTINGSP_TREESTRETCH_DISTRIBUTION", folder);
		success &= DataWriter.writeWithoutIndex(this.percEdges,
				"ROUTINGSP_TREEEDGE_DISTRIBUTION", folder);
		if (this.isStretch)
		success &= DataWriter.writeWithoutIndex(this.stretchDist,
				"ROUTINGSP_STRETCH_DISTRIBUTION", folder);
		return success;
	}

	@Override
	public Single[] getSingles() {
		Single averageHops = new Single("ROUTINGSP_HOPS_AVG",
				this.hopDistribution.getAverage());
		Single medianHops = new Single("ROUTINGSP_HOPS_MED",
				this.hopDistribution.getMedian());
		Single maximumHops = new Single("ROUTINGSP_HOPS_MAX",
				this.hopDistribution.getMax());

		Single successRate = new Single("ROUTINGSP_SUCCESS_RATE",
				this.successRate);
		Single failureRate = new Single("ROUTINGSP_FAILURE_RATE",
				this.failureRate);
		Single treeEdges = new Single("ROUTINGSP_TREE_EDGES",
				this.spEdgesAll);
		Single treeEdgesA = new Single("ROUTINGSP_TREE_EDGES_AVG",
				this.spEdgesAv);
		Single treestretch = new Single("ROUTINGSP_TREESTRETCH",
				this.tree);
		if (this.isStretch){
		Single stretch = new Single("ROUTINGSP_STRETCH",
				this.stretch);
		return new Single[] { averageHops, medianHops, maximumHops,
				successRate, failureRate,stretch,treeEdges, treeEdgesA,treestretch };
		} else {
			return new Single[] { averageHops, medianHops, maximumHops,
					successRate, failureRate, treeEdges, treeEdgesA, treestretch};
		}
	}

	private class RoutingThread extends Thread {
		private int start;

		private int end;

		private int times;

		private Graph graph;

		private RoutingAlgorithm ra;

		private Random rand;

		private Route[] routes;
		
		private RoutingSP routing;

		private RoutingThread(int start, int end, int times, Graph graph,
				RoutingAlgorithm ra, Random rand, RoutingSP routing) {
			this.start = start;
			this.end = end;
			this.times = times;
			this.graph = graph;
			this.ra = ra;
			this.rand = rand;
			this.routes = new Route[(end - start + 1) * times];
			this.routing = routing;
		}

		public void run() {
			int index = 0;
			for (int i = this.start; i <= this.end; i++) {
				short[] paths = routing.getShortestPaths(graph, i);
				for (int j = 0; j < this.times; j++) {
					this.routes[index] = ra.routeToRandomTarget(graph, i,
							rand);
					if (this.routes[index].isSuccessful()){
						routing.shortestPaths[index] = paths[this.routes[index].getLastNode()];
					}
					index++;
				}
			}
		}

		/**
		 * @return the routes
		 */
		public Route[] getRoutes() {
			return this.routes;
		}

		public String toString() {
			return this.start + " => " + this.end;
		}
	}
	
	private short[] getShortestPaths(Graph g, int start){
		Node[] nodes = g.getNodes();
		short[] paths = new short[nodes.length];
		for (int i = 0; i < paths.length; i++){
			paths[i] = -1;
		}
		paths[start] = 0;
		LinkedList<Integer> queue = new LinkedList<Integer>();
		queue.add(start);
		int current; int[] out;
		while (!queue.isEmpty()){
			current = queue.pollFirst();
			out = nodes[current].getOutgoingEdges();
			for (int i = 0; i < out.length; i++){
				if (paths[out[i]] == -1){
					paths[out[i]] = (short) (paths[current]+1);
					queue.add(out[i]);
				}
			}
		}
		//System.out.println("Got here " + start);
		return paths;
	}

}
