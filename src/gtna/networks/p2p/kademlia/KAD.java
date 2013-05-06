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
 * KAD.java
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
package gtna.networks.p2p.kademlia;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.sorting.NodeSorter;
import gtna.graph.sorting.RandomNodeSorter;
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
 * @author stef
 *
 */
public class KAD extends Network {

	private int k;
	private int bits;
	private IDSelection selection;
	/**
	 * @param key
	 * @param nodes
	 * @param parameters
	 * @param transformations
	 */
	public KAD(int nodes, int bits, int k, IDSelection selection,
			Transformation[] transformations) {
		super("KAD", nodes, new Parameter[] { new IntParameter("BITS", bits),
				new IntParameter("BUCKET_SIZE", k), 
		new StringParameter("ID_SELECTION", selection.toString())}, transformations);
		this.bits = bits;
		this.k = k;
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
				this.selection == IDSelection.UNIFORM, this.k);
		graph = t.transform(graph);

		KademliaIdentifierSpace idSpace = (KademliaIdentifierSpace) graph
				.getProperty("ID_SPACE_0");
		KademliaPartition[] partitions = (KademliaPartition[]) idSpace
				.getPartitions();

		Edges edges = new Edges(nodes, nodes.length * this.bits*k);
		Random rand = new Random();
		NodeSorter randomize = new RandomNodeSorter();
		int val;
		BigInteger dist;
		int valShift;
		for (Node node : nodes) {
			int[][] counts = new int[this.bits+1][];
			counts[this.bits] = new int[8];
			for (int i = 0; i < this.bits; i++){
				counts[i] = new int[5];
			}
			KademliaIdentifier id = (KademliaIdentifier) partitions[node.getIndex()].getRepresentativeID();
			Node[] randNodes = randomize.sort(graph, rand);
            for (int j = 0; j < randNodes.length; j++){
            	//val = id.distance(partitions[randNodes[j].getIndex()].getSucc()).bitLength();
            	dist = id.distance(partitions[randNodes[j].getIndex()].getRepresentativeID());
            	val = dist.bitLength();
            	if (val == this.bits){
            	   valShift = dist.shiftRight(val-4).intValue();
            	   if (counts[this.bits][valShift-8] < this.k){
            		   counts[val][valShift-8]++;
                       edges.add(node.getIndex(), randNodes[j].getIndex());
            	   }
            	}else {
            		 valShift = dist.shiftRight(val-4).intValue();
            		 int index = 0;
              	 if (valShift == 12 || valShift == 13){
            		   index = 1;
            	   }
              	if (valShift == 10 || valShift == 11){
         		   index = 2;
         	   }
              	if (valShift == 9){
         		   index = 3;
         	   }
              	if (valShift == 8){
         		   index = 4;
         	   }
              	if (counts[val][index] < this.k){
         		   counts[val][index]++;
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
