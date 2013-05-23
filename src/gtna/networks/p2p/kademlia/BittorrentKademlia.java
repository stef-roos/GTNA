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
 * BittorrentKademlia.java
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
package gtna.networks.p2p.kademlia;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.sorting.NodeSorter;
import gtna.graph.sorting.RandomNodeSorter;
import gtna.id.BIIdentifier;
import gtna.networks.Network;
import gtna.networks.p2p.chord.Chord.IDSelection;
import gtna.transformation.Transformation;
import gtna.transformation.id.RandomKademliaIDSpace;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author stefanie
 *
 */
public class BittorrentKademlia extends Network {

	private int[] k;
	private int bits;
	private IDSelection selection;
	/**
	 * @param key
	 * @param nodes
	 * @param parameters
	 * @param transformations
	 */
	public BittorrentKademlia(int nodes, int bits, IDSelection selection,
			Transformation[] transformations) {
		super("BITTORRENT_KADEMLIA", nodes, new Parameter[] { new IntParameter("BITS", bits), 
		new StringParameter("ID_SELECTION", selection.toString())}, transformations);
		this.bits = bits;
		this.k = new int[bits+1];
		for (int i = 0; i < this.k.length-4; i++){
			this.k[i] = 8;
		}
		this.k[k.length-4] = 16;
		this.k[k.length-3] = 32;
		this.k[k.length-2] = 64;
		this.k[k.length-1] = 128;
	}

	/* (non-Javadoc)
	 * @see gtna.networks.Network#generate()
	 */
	@Override
	public Graph generate() {
		Graph graph = new Graph(this.getDescription());
		Node[] nodes = Node.init(this.getNodes(), graph);
		graph.setNodes(nodes);
		RandomKademliaIDSpace t = new RandomKademliaIDSpace(this.bits,
				this.selection == IDSelection.UNIFORM);
		graph = t.transform(graph);

		KademliaIdentifierSpace idSpace = (KademliaIdentifierSpace) graph
				.getProperty("ID_SPACE_0");
		KademliaPartition[] partitions = (KademliaPartition[]) idSpace
				.getPartitions();

		Edges edges = new Edges(nodes, nodes.length * this.bits*8);
		Random rand = new Random();
		NodeSorter randomize = new RandomNodeSorter();
		int val;
		
		for (Node node : nodes) {
			int[] counts = new int[this.bits+1];
			BIIdentifier id = partitions[node.getIndex()].getRepresentativeID();
			Node[] randNodes = randomize.sort(graph, rand);
            for (int j = 0; j < randNodes.length; j++){
            	if (randNodes[j].getIndex() != node.getIndex()){
            	val = id.distance(partitions[randNodes[j].getIndex()].getRepresentativeID()).bitLength();
            	if (counts[val] < this.k[val]){
            		counts[val]++;
            		edges.add(node.getIndex(), randNodes[j].getIndex());
            	}
            	}
            }
		}
		edges.fill();

		graph.getTimer().end();
		return graph;
	}
	
}	
