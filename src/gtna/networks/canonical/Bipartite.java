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
 * Bipartite.java
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
package gtna.networks.canonical;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.Random;

/**
 * @author stef
 *
 */
public class Bipartite extends Network {
	private int n1;
	private int n2;
	private double p;
	private double q;
	
	public Bipartite(int n1,int n2, double p, double q,  Transformation[] t){
		super("BIPARTITE", n1+n2,new Parameter[] { new IntParameter(
				"N1", n1), new IntParameter(
						"N2", n2), new DoubleParameter(
								"P", p), new DoubleParameter(
										"Q", q)  }, t);
		this.n1 = n1;
		this.n2 = n2;
		this.p = p;
		this.q = q;
	}

	/* (non-Javadoc)
	 * @see gtna.networks.Network#generate()
	 */
	@Override
	public Graph generate() {
		Graph graph = new Graph(this.getDescription());
		Node[] nodes = Node.init(this.getNodes(), graph);
		Edges edges = new Edges(nodes, this.getNodes() * (this.getNodes() - 1));
		Random rand = new Random();
		for (int i = 0; i < nodes.length-this.n2; i++) {
			for (int j = nodes.length-this.n2; j < nodes.length; j++) {
				if (rand.nextDouble() < this.p){
					edges.add(i, j);
				}
				if (rand.nextDouble() < this.p){
					edges.add(j,i);
				}
				if (rand.nextDouble() < this.q){
					edges.add(i, j);
					edges.add(j,i);
				}
			}
		}
		
		edges.fill();
		graph.setNodes(nodes);
		return graph;
	}

}
