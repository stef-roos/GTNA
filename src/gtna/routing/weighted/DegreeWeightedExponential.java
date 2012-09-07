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
 * DegreeWeightedExponential.java
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
package gtna.routing.weighted;

import gtna.graph.Node;
import gtna.id.BIIdentifier;
import gtna.id.DIdentifier;
import gtna.routing.greedyVariations.NodeGreedy;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author stef
 *
 */
public class DegreeWeightedExponential  extends NodeGreedy {

		public DegreeWeightedExponential() {
			super("DEGREE_WEIGHTED_EXPONENTIAL");
		}

		public DegreeWeightedExponential(int ttl) {
			super(ttl, "DEGREE_WEIGHTED_EXPONENTIAL");
		}

		@Override
		public int getNextD(int current, DIdentifier target, Random rand,
				Node[] nodes) {
			double currentDist = this.idSpaceD.getPartitions()[current]
					.distance(target);
			double minDist = this.idSpaceD.getMaxDistance();
			int minNode = -1;
			for (int out : nodes[current].getOutgoingEdges()) {
				double dist = this.pD[out].distance(target);
				double distE = Math.pow(dist, 1/(double)nodes[out].getDegree());
				if (distE < minDist && dist < currentDist) {
					minDist = distE;
					minNode = out;
				}
			}
			return minNode;
		}

		@Override
		public int getNextBI(int current, BIIdentifier target, Random rand,
				Node[] nodes) {
			BigInteger currentDist = this.idSpaceBI.getPartitions()[current]
					.distance(target);
			BigInteger minDist = this.idSpaceBI.getMaxDistance();
			int minNode = -1;
			for (int out : nodes[current].getOutgoingEdges()) {
				BigInteger dist = this.pBI[out].distance(target);
				if (dist.compareTo(minDist) == -1
						&& dist.compareTo(currentDist) == -1) {
					minDist = dist;
					minNode = out;
				}
			}
			return minNode;
		}

		@Override
		public void setSets(int nr) {
			// TODO Auto-generated method stub

		}

}


