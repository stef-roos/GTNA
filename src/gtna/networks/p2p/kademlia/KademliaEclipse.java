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
 * Kademlia.java
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
import gtna.transformation.id.RandomKademliaIDSpaceEclipse;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.Random;

/**
 * @author stefanie
 *
 */
public class KademliaEclipse extends Network {

	private int k;
	private int bits;
	private IDSelection selection;
	private int att;
	private AttackerSelection aSelect;
	private int target;
	public enum AttackerSelection{
		RANDOM, TARGET
	}
	/**
	 * @param key
	 * @param nodes
	 * @param parameters
	 * @param transformations
	 */
	public KademliaEclipse(int nodes, int bits, int k, IDSelection selection, int attackers,
			AttackerSelection aSelect,  int target,
			Transformation[] transformations) {
		super("KADEMLIA_ECLIPSE", nodes+attackers, new Parameter[] { new IntParameter("BITS", bits),
				new IntParameter("BUCKET_SIZE", k), 
		new StringParameter("ID_SELECTION", selection.toString()), 
				new StringParameter("ATTACK_SELECT", aSelect.toString()),
				new IntParameter("ATTACKER", attackers)}, transformations);
		this.bits = bits;
		this.k = k;
		this.aSelect = aSelect;
		this.att = attackers;
		this.target = target;
	}

	/* (non-Javadoc)
	 * @see gtna.networks.Network#generate()
	 */
	@Override
	public Graph generate() {
		Graph graph = new Graph(this.getDescription());
		Node[] nodes = Node.init(this.getNodes(), graph);
		graph.setNodes(nodes);
		if (this.aSelect == AttackerSelection.TARGET){
			//int target = (new Random()).nextInt(nodes.length-this.att);
		   RandomKademliaIDSpaceEclipse t = new RandomKademliaIDSpaceEclipse(this.bits,
				this.selection == IDSelection.UNIFORM, this.k,this.att, target);
		   graph = t.transform(graph);
		} else {
			RandomKademliaIDSpace t = new RandomKademliaIDSpace(this.bits,
					this.selection == IDSelection.UNIFORM, this.k);
			   graph = t.transform(graph);	
		}
		
		

		KademliaIdentifierSpace idSpace = (KademliaIdentifierSpace) graph
				.getProperty("ID_SPACE_0");
		KademliaPartition[] partitions = (KademliaPartition[]) idSpace
				.getPartitions();
		
		

		Edges edges = new Edges(nodes, nodes.length * this.bits*k);
		Random rand = new Random();
		NodeSorter randomize = new RandomNodeSorter();
		int val;
		
		for (Node node : nodes) {
			int[] counts = new int[this.bits+1];
			BIIdentifier id = partitions[node.getIndex()].getRepresentativeID();
			Node[] randNodes = randomize.sort(graph, rand);
            for (int j = 0; j < randNodes.length; j++){
            	val = id.distance(partitions[randNodes[j].getIndex()].getRepresentativeID()).bitLength();
            	if (counts[val] < this.k){
            		counts[val]++;
            		edges.add(node.getIndex(), randNodes[j].getIndex());
            	}
            }
		}
		edges.fill();

		graph.getTimer().end();
		return graph;
	}

}
