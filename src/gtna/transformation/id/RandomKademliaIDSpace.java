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
 * RandomKademliaIDSpace.java
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
package gtna.transformation.id;

import gtna.graph.Graph;
import gtna.networks.p2p.kademlia.KademliaIdentifier;
import gtna.networks.p2p.kademlia.KademliaIdentifierSpace;
import gtna.networks.p2p.kademlia.KademliaPartition;
import gtna.transformation.Transformation;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

/**
 * @author stef
 *
 */
public class RandomKademliaIDSpace extends Transformation {

	private int bits;

	private boolean uniform;
	
	//private int k;

	public RandomKademliaIDSpace(int bits, boolean uniform) {
		super("RANDOM_KADEMLIA_ID_SPACE", new Parameter[] {
				new IntParameter("BITS", bits), 
				new BooleanParameter("ID_SELECTION", uniform) });
		this.bits = bits;
		this.uniform = uniform;
		//this.k = k;
	}

	

	@Override
	public Graph transform(Graph graph) {
		Random rand = new Random();
		
			KademliaIdentifierSpace idSpace = new KademliaIdentifierSpace(this.bits);
			KademliaIdentifier[] ids = new KademliaIdentifier[graph.getNodes().length];
			if (this.uniform) {
				BigInteger stepSize = idSpace.getModulus().divide(
						new BigInteger("" + graph.getNodes().length));
				for (int i = 0; i < ids.length; i++) {
					ids[i] = new KademliaIdentifier(idSpace,
							stepSize.multiply(new BigInteger("" + i)));
				}
			} else {
				HashSet<String> idSet = new HashSet<String>();
				for (int i = 0; i < ids.length; i++) {
					KademliaIdentifier id = (KademliaIdentifier) idSpace
							.randomID(rand);
					while (idSet.contains(id.toString())) {
						id = (KademliaIdentifier) idSpace.randomID(rand);
					}
					ids[i] = id;
					idSet.add(id.toString());
				}
			}
			Arrays.sort(ids);

			KademliaPartition[] partitions = new KademliaPartition[ids.length];
			for (int i = 0; i < partitions.length; i++) {
				partitions[i] = new KademliaPartition(ids[i]);
			}

			// Util.randomize(partitions, rand);
			idSpace.setPartitions(partitions);

			graph.addProperty(graph.getNextKey("ID_SPACE"), idSpace);
		
		return graph;
	}

	@Override
	public boolean applicable(Graph g) {
		return true;
	}

}
