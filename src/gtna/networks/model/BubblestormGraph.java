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
 * BubblestormGraph.java
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
package gtna.networks.model;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.Random;
import java.util.Vector;

/**
 * @author stef
 *
 */
public class BubblestormGraph extends Network {
	double[] dd;
	
	
	public BubblestormGraph(int n, double[] dd, String name, Transformation[] t){
		super("BUBBLESTORM", n, new Parameter[]{new StringParameter("DESC", name)},t);
		this.dd = dd;
	}

	/* (non-Javadoc)
	 * @see gtna.networks.Network#generate()
	 */
	@Override
	public Graph generate() {
		Graph graph = new Graph(this.getDescription());
		Random rand = new Random(System.currentTimeMillis());
		Node[] nodes = Node.init(this.getNodes(), graph);
		Vector<int[]> circle = new Vector<int[]>();
		circle.add(new int[]{0,0});
		for (int i = 0; i < nodes.length; i++){
			//draw number of edges
			int k = 0;
			double s = rand.nextDouble();
			while(dd[k] < s){
				k++;
			}
			for (int j = 0; j < k; j++){
				int[] e = circle.get(rand.nextInt(circle.size()));
				int[] ne = new int[2];
				ne[1] = e[1];
				ne[0] = i;
				e[1] = i; 
				circle.add(ne);
			}
		}
		Edges edges = new Edges(nodes, circle.size()*2);
		for (int i = 0; i < circle.size(); i++){
			int[] e = circle.get(i);
			edges.add(e[0],e[1]);
			edges.add(e[1],e[0]);
		}
		edges.fill();

		graph.setNodes(nodes);
		
		return graph;
	}

}
