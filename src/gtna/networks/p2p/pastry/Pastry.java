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
 * Pastry.java
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
package gtna.networks.p2p.pastry;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.sorting.NodeSorter;
import gtna.graph.sorting.RandomNodeSorter;
import gtna.networks.Network;
import gtna.networks.p2p.chord.Chord.IDSelection;
import gtna.transformation.Transformation;
import gtna.transformation.id.RandomPastryIDSpace;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Random;

/**
 * @author stefanie
 *
 */
public class Pastry extends Network {
	private int bits;
	private int prefix;
	private int l;
	private IDSelection selection;
	
	
	public Pastry(int nodes, int bits, int prefix, int l, IDSelection selection, Transformation[] t) {
		super("PASTRY", nodes, new Parameter[] { new IntParameter("BITS", bits),
				new IntParameter("PREFIX", prefix), new IntParameter("L", l),
		new StringParameter("ID_SELECTION", selection.toString())}, t);
		this.bits = bits;
		this.prefix = prefix;
		this.l = l;
		this.selection = selection;
	}

	/* (non-Javadoc)
	 * @see gtna.networks.Network#generate()
	 */
	@Override
	public Graph generate() {
		Graph graph = new Graph(this.getDescription());
		Node[] nodes = Node.init(this.getNodes(), graph);
		graph.setNodes(nodes);
		RandomPastryIDSpace t = new RandomPastryIDSpace(this.bits,
				this.selection == IDSelection.UNIFORM, this.l,this.prefix);
		graph = t.transform(graph);

		PastryIdentifierSpace idSpace = (PastryIdentifierSpace) graph
				.getProperty("ID_SPACE_0");
		PastryPartition[] partitions = (PastryPartition[]) idSpace
				.getPartitions();

		Edges edges = new Edges(nodes, nodes.length * this.bits);
		Random rand = new Random();
		NodeSorter randomize = new RandomNodeSorter();
		int[] diff;
		int val;
		int pre = (int)Math.pow(2, this.prefix);
		for (Node node : nodes) {
			PastryPartition p = partitions[node.getIndex()];
			BigInteger id = p.getSucc().getId();
			for (int i = 1; i <= this.l/2; i++){
				edges.add(node.getIndex(), node.getIndex()-i>-1?node.getIndex()-i:nodes.length+node.getIndex()-i);
				edges.add(node.getIndex(), (node.getIndex()+i)%nodes.length);
			}
			HashSet<Integer> neighbors = new HashSet<Integer>();
			Node[] randNodes = randomize.sort(graph, rand);
			for (int j = 0; j < randNodes.length; j++){
				if (node.getIndex() != randNodes[j].getIndex()){
					BigInteger other = partitions[randNodes[j].getIndex()].getSucc().getId();
					diff = p.getSucc().getPrefixLength(this.prefix, this.bits, other);
					val = diff[0]*pre + diff[1];
					if (!neighbors.contains(val)){
						edges.add(node.getIndex(), randNodes[j].getIndex());
						neighbors.add(val);
					}
				}
			}

		}
		edges.fill();

		graph.getTimer().end();
		return graph;

	}

	public int getBits() {
		return this.bits;
	}

	public int getPrefix() {
		return this.prefix;
	}

	public int getL() {
		return this.l;
	}

	public IDSelection getSelection() {
		return this.selection;
	}
	
	

}
