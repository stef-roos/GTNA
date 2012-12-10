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
 * PrefixEmbedding.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Andreas Höfer;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.embedding.prefix;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.spanningTree.ParentChild;
import gtna.graph.spanningTree.SpanningTree;
import gtna.id.prefix.PrefixSIdentiferSpaceSimple;
import gtna.id.prefix.PrefixSIdentifier;
import gtna.id.prefix.PrefixSPartitionSimple;
import gtna.transformation.Transformation;
import gtna.transformation.spanningtree.RestrictedBFS;
import gtna.transformation.spanningtree.RestrictedFatTree;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author Andreas Höfer
 * Implementation of the prefix embedding algorithm. 
 * The spanning tree used for the implementation is passed as a parameter or is computed during the during the embeddding.
 * Either way the spanning tree is stored in SPANNINGTREE property of the graph.
 * .
 * 
 */
public class PrefixEmbeddingSTVirtual extends Transformation {

	private boolean debug = false;
	private int root;
	private int bitsPerCoord;
	private int idSpaceSize;
	private boolean fatTree = false;
	/**
	 * @param root index of the root node; root = -1 => draw the root randomly; root = -2 => root is the highest degree node
	 * @param bitsPerCoord
	 */
	public PrefixEmbeddingSTVirtual(int root, int bitsPerCoord, int idSpaceSize) {
		super("PREFIXEMBEDDINGSTVIRTUAL", new Parameter[] {new IntParameter("ROOT", root)
		, new IntParameter("BITSPERCOORD", bitsPerCoord), new IntParameter("IDSPACESIZE", idSpaceSize) });
		this.root = root;
		this.bitsPerCoord = bitsPerCoord;
		this.idSpaceSize = idSpaceSize;
	}

	
	/* (non-Javadoc)
	 * @see gtna.transformation.Transformation#transform(gtna.graph.Graph)
	 */
	@Override
	public Graph transform(Graph g) {
		Node[] nodes = g.getNodes();
		int rootIndex;
		SpanningTree st;
		if (!g.hasProperty("SPANNINGTREE")){
			if (root == -1){
				// choose a node randomly as start node for the graph traversal
				rootIndex = (int) Math.floor(nodes.length * Math.random());
			} else {
				if (root == -2)
					// choose one of the nodes with highest degree
					rootIndex = randomHighDegreeNode(nodes).getIndex();
				else
					rootIndex = root;
			}
			int maxdegree = (int) Math.pow(2, bitsPerCoord);
			if (fatTree)
				(new RestrictedFatTree(rootIndex, maxdegree)).transform(g);
			else
				(new RestrictedBFS(rootIndex, maxdegree)).transform(g);	
		}
		st = (SpanningTree) g.getProperty("SPANNINGTREE");
		rootIndex = st.getSrc();	
		// array contains the coordinates of all nodes; is indexed with the node index from the graph 
		short[][] coordinates = new short[nodes.length][];  	
		
		// root has an empty coordinate vector 
		coordinates[rootIndex] = new short[]{};
		
		Queue<Integer> nodequeue = new LinkedList<Integer>();
		nodequeue.add(rootIndex);
		
		// while currentNode has some child node which has not been visited
		while(!nodequeue.isEmpty()){
			int parent = nodequeue.poll(); 
			short[] parentC = coordinates[parent];
			
			int[] outgoing = st.getChildren(parent);
			if (outgoing.length == 0){
				continue;
			}
			// along the spanning tree assign the children indices
			
			// index of the current child
			int child_index = 0;
			
			// maximum nr of additional coords
			int maxNrCoord = (int) Math.ceil(Math.log(outgoing.length)/Math.log(2));
			// number of nodes on lowest level
			int levelChange = outgoing.length - ((int)Math.pow(2, maxNrCoord)-outgoing.length);
			// handle the special case of a node with only one child
			if (maxNrCoord == 0){
				maxNrCoord = 1;
				levelChange = 1;
			}
			
			for (int n: outgoing){			
				// the new coordinates are based on the coords from the parent plus the additional coordinates
				int nr_of_additional_coordinates = child_index<levelChange? maxNrCoord:(maxNrCoord-1);
				coordinates[n] = new short[parentC.length + nr_of_additional_coordinates];
				System.arraycopy(parentC, 0, coordinates[n], 0, parentC.length);
				
				//compute 0-1 array of bit coordinates
				short[] newCoord;
				if (child_index < levelChange){
					newCoord = this.intToBitSTring(child_index, maxNrCoord);
				}else {
					int newIndex = levelChange/2 + (child_index-levelChange);
					newCoord = this.intToBitSTring(newIndex, maxNrCoord-1);
				}
				
				for (int i = parentC.length; i < coordinates[n].length; i++){
					coordinates[n][i] = newCoord[i-parentC.length];
				}
				
				child_index++;
				
				if (debug){
					System.out.print("node: " + n + " :");
					for (int i=0; i < coordinates[n].length; i++)
						System.out.print(" " + coordinates[n][i]);
					System.out.println();
				}
				nodequeue.add(n);
			}					
		}
		
		// create the id space, the partitions and assign each partition its identifier/coordinates
		PrefixSIdentiferSpaceSimple idSpace = new PrefixSIdentiferSpaceSimple(this.bitsPerCoord,this.idSpaceSize,
				true);
		PrefixSPartitionSimple[] partitions = new PrefixSPartitionSimple[nodes.length];
		for (int n=0; n < nodes.length; n++){
			partitions[n] = new PrefixSPartitionSimple(new PrefixSIdentifier(coordinates[n]));
		}
		idSpace.setPartitions(partitions);
		

		g.addProperty(g.getNextKey("ID_SPACE"), idSpace);
		return g;
	}

	/* 
	 * Dummy method
	 */
	@Override
	public boolean applicable(Graph g) {
		return true;
	}

	private short[] intToBitSTring(int index, int nrOfBits){
		// compute binary representation of child index
		short[] binary_index = new short[nrOfBits];
		int temp = index;
		// the binary conversion computation is done from lowest digit to the highest digit
		for (int i=0; i < nrOfBits; i++){
			binary_index[nrOfBits - i -1] = (temp % 2 == 1) ? (short)1 : 0;
			temp = temp / 2;
			if (temp == 0) break;
		}
		return binary_index;
	}
	
	/*
	 * Activate fat tree spanning tree instead of normal BFS spanning tree
	 */
	public void setFatTree(){
		fatTree = true;
	}

	/*
	 * find one node of the 1 % nodes with highest degree
	 */
	private Node randomHighDegreeNode(Node[] nodes){
		int onePercent = nodes.length / 100;
		Node[] sortedNodes = nodes.clone();
		// sort the array ascending
		Arrays.sort(sortedNodes, 0, nodes.length);
		Node[] onePercentHD = new Node[onePercent];
		// take the last one percent of the nodes as the array is sorted ascending but we need it descending
		for (int i= 0; i < onePercent; i++){
			onePercentHD[i] = sortedNodes[nodes.length - 1 - i]; 
			// System.out.println("OnePercentHD[" + i + "]: " + onePercentHD[i]);
		}	
		return onePercentHD[(int) Math.floor(onePercent * Math.random())];
	}
}
