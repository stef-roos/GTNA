package gtna.transformation.embedding.prefix;
///* ===========================================================
// * GTNA : Graph-Theoretic Network Analyzer
// * ===========================================================
// *
// * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
// * and Contributors
// *
// * Project Info:  http://www.p2p.tu-darmstadt.de/research/gtna/
// *
// * GTNA is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// *
// * GTNA is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// *
// * You should have received a copy of the GNU General Public License
// * along with this program. If not, see <http://www.gnu.org/licenses/>.
// *
// * ---------------------------------------
// * PrefixEmbedding.java
// * ---------------------------------------
// * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
// * and Contributors 
// *
// * Original Author: Andreas Höfer;
// * Contributors:    -;
// *
// * Changes since 2011-05-17
// * ---------------------------------------
// *
// */
//package gtna.transformation.Embeddings;
//
//import gtna.graph.Graph;
//import gtna.graph.Node;
//import gtna.id.prefix.PrefixSIdentiferSpaceSimple;
//import gtna.id.prefix.PrefixSIdentifier;
//import gtna.id.prefix.PrefixSPartitionSimple;
//import gtna.transformation.Transformation;
//import gtna.util.parameter.IntParameter;
//import gtna.util.parameter.Parameter;
//
//import java.util.LinkedList;
//import java.util.Queue;
//
///**
// * @author Andreas Höfer
// * 
// * Implementation of the prefix embedding algorithm. The spanning tree used for the implementation is constructed implicitly during the embedding and is not stored.
// * 
// */
//public class PrefixEmbedding extends Transformation {
//
//	private boolean debug = true;
//	private int root;
//	/**
//	 * @param root index of the root node
//	 */
//	public PrefixEmbedding(int root) {
//		super("PREFIXEMBEDDING", new Parameter[] {new IntParameter("ROOT", root) });
//		this.root = root;
//	}
//
//	
//	/* (non-Javadoc)
//	 * @see gtna.transformation.Transformation#transform(gtna.graph.Graph)
//	 */
//	@Override
//	public Graph transform(Graph g) {
//		Node[] nodes = g.getNodes();
//		
//		// mark traversed nodes during the embedding
//		boolean[] embedded = new boolean[nodes.length];
//		
//		// compute a new embedding 
//		// array contains the coordinates of all nodes; is indexed with the node index from the graph 
//		short[][] coordinates = new short[nodes.length][];  	
//		
//		int rootIndex;
//		if (root < 0 || root > nodes.length -1)
//			// choose a node randomly as start node for the graph traversal
//			rootIndex = (int) Math.floor(nodes.length * Math.random());
//		else
//			rootIndex = root;
//		
//		// root has an empty coordinate vector 
//		coordinates[rootIndex] = new short[]{};
//		embedded[rootIndex] = true;
//		
//		Queue<Node> nodequeue = new LinkedList<Node>();
//		nodequeue.add(nodes[rootIndex]);
//
//		// while currentNode has some child node which has not been visited
//		while(!nodequeue.isEmpty()){
//			Node parentN = nodequeue.poll(); 
//			short[] parentC = coordinates[parentN.getIndex()];
//			
//			int[] outgoing = parentN.getOutgoingEdges();
//			// along the spanning tree assign the children indices
//			
//			// index of the current child
//			int child_index = 0;
//			
//			for (int n: outgoing){
//				if (!embedded[n]){			
//					// the new coordinates are based on the coords from the parent plus the additional coordinates
//					coordinates[n] = new short[parentC.length + 1];
//					System.arraycopy(parentC, 0, coordinates[n], 0, parentC.length);
//					
//					coordinates[n][parentC.length +1] = (short) child_index;
//					
//					child_index++;
//					
//					embedded[n] = true;
//					if (debug){
//						System.out.print("node: " + n + " :");
//						for (int i=0; i < coordinates[n].length; i++)
//							System.out.print(" " + coordinates[n][i]);
//						System.out.println();
//					}
//					nodequeue.add(nodes[n]);
//				}					
//			}
//		}
//		
//		// create the id space, the partitions and assign each partition its identifier/coordinates
//		PrefixSIdentiferSpaceSimple idSpace = new PrefixSIdentiferSpaceSimple();
//		PrefixSPartitionSimple[] partitions = new PrefixSPartitionSimple[nodes.length];
//		for (int n=0; n < nodes.length; n++){
//			partitions[n] = new PrefixSPartitionSimple(new PrefixSIdentifier(coordinates[n]));
//		}
//		idSpace.setPartitions(partitions);
//
//		g.addProperty(g.getNextKey("ID_SPACE"), idSpace);
//		return g;
//	}
//
//	/* 
//	 * Dummy method
//	 */
//	@Override
//	public boolean applicable(Graph g) {
//		return true;
//	}
//
//}
