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
 * RandomPastryIDSpace.java
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
package gtna.transformation.id;

import gtna.graph.Graph;
import gtna.networks.p2p.pastry.PastryIdentifier;
import gtna.networks.p2p.pastry.PastryIdentifierSpace;
import gtna.networks.p2p.pastry.PastryPartition;
import gtna.transformation.Transformation;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

/**
 * @author stefanie
 *
 */
public class RandomPastryIDSpace extends Transformation {

	private int bits;

	private boolean uniform;
	
	private int L;
	
	private int prefix;

	public RandomPastryIDSpace(int bits, boolean uniform, int L, int prefix) {
		super("RANDOM_PASTRY_ID_SPACE", new Parameter[] {
				new IntParameter("BITS", bits),
				new BooleanParameter("ID_SELECTION", uniform) });
		this.bits = bits;
		this.uniform = uniform;
		this.L = L;
		this.prefix = prefix;
	}

	

	@Override
	public Graph transform(Graph graph) {
		Random rand = new Random();
		
			PastryIdentifierSpace idSpace = new PastryIdentifierSpace(this.bits,this.L,this.prefix);
			PastryIdentifier[] ids = new PastryIdentifier[graph.getNodes().length];
			if (this.uniform) {
				BigInteger stepSize = idSpace.getModulus().divide(
						new BigInteger("" + graph.getNodes().length));
				for (int i = 0; i < ids.length; i++) {
					ids[i] = new PastryIdentifier(idSpace,
							stepSize.multiply(new BigInteger("" + i)));
				}
			} else {
				HashSet<String> idSet = new HashSet<String>();
				for (int i = 0; i < ids.length; i++) {
					PastryIdentifier id = (PastryIdentifier) idSpace
							.randomID(rand);
					while (idSet.contains(id.toString())) {
						id = (PastryIdentifier) idSpace.randomID(rand);
					}
					ids[i] = id;
					idSet.add(id.toString());
				}
			}
			Arrays.sort(ids);

			PastryPartition[] partitions = new PastryPartition[ids.length];
			partitions[0] = new PastryPartition(ids[ids.length - 1], ids[0]);
			for (int i = 1; i < partitions.length; i++) {
				partitions[i] = new PastryPartition(ids[i - 1], ids[i]);
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
