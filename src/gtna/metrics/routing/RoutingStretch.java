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
 * RoutingStretch.java
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
public class RoutingStretch extends Metric {
    private Route[] routes;
	private short[] shortestPaths;
	private double stretch;
	private double[][] stretchDist;
	public static double precision = 0.01;

	public RoutingStretch() {
		super("ROUTING_STRETCH");
	}





	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return m.containsKey("ROUTING") && m.get("ROUTING") instanceof Routing ;
	}

	@Override
	public void computeData(Graph graph, Network network,
			HashMap<String, Metric> metrics) {
		Routing routing = (Routing) metrics.get("ROUTING");
        this.routes = routing.getRoutes();
		this.computeStretchDistribution(graph);
	}

	
	

	
	private void computeStretchDistribution(Graph g){
		int node = -1;
		int curNode;
		short[] curPaths = {-1};
		int index = 0;
		double s = 0;
		this.shortestPaths = new short[this.routes.length];
		for (int i = 0; i < this.routes.length; i++){
			curNode = this.routes[i].getFirstNode();
			if (curNode != node){
				curPaths = this.getShortestPaths(g, curNode);
				node = curNode;
			}
			if (this.routes[i].isSuccessful()){
				this.shortestPaths[i] = curPaths[this.routes[i].getLastNode()];
				index++;
				s = s + (double)this.routes[i].getHops()/(double)this.shortestPaths[i];
			}
		}
		s= s/(double)index;
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
		int m = (int)Math.ceil((dist[dist.length-1]-1)/RoutingStretch.precision)+1;
		int c = 0;
		double[][] res = new double[m][2];
		while (i < dist.length){
			while (i < dist.length && dist[i] <= 1 + k*RoutingStretch.precision){
				c++;
				i++;
			}
			
			res[k][0] = 1 + k*RoutingStretch.precision;
			res[k][1] = (double)c/(double)dist.length;
			k++;
		}
		this.stretch = s;
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
		
		success &= DataWriter.writeWithoutIndex(this.stretchDist,
				"ROUTING_STRETCH_STRETCH_DISTRIBUTION", folder);
		return success;
	}

	@Override
	public Single[] getSingles() {

		Single stretch = new Single("ROUTING_STRETCH_STRETCH",
				this.stretch);
		return new Single[] {stretch };
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
