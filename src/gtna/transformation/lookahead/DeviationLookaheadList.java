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
 * DeviationLookahaedList.java
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
package gtna.transformation.lookahead;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.graph.Node;
import gtna.id.IdentifierSpace;
import gtna.id.Partition;
import gtna.id.lookahead.LookaheadElement;
import gtna.id.lookahead.LookaheadList;
import gtna.id.lookahead.LookaheadLists;
import gtna.id.plane.PlaneIdentifier;
import gtna.id.plane.PlanePartitionSimple;
import gtna.id.ring.RingIdentifier;
import gtna.id.ring.RingPartition;
import gtna.id.ring.RingPartitionSimple;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * @author stef
 *
 */
public class DeviationLookaheadList extends ObfuscatedLookaheadList {
	
	protected Deviation deviation;
	protected double sigma;
	protected boolean randomizeOrder;
	
	public static enum Deviation{
		UNIFORM, NORMAL
	}

	/**
	 * @param key
	 * @param minEpsilon
	 * @param maxEpsilon
	 */
	public DeviationLookaheadList(String key, double minEpsilon,
			double maxEpsilon) {
		super(key, minEpsilon, maxEpsilon);
		this.deviation = Deviation.UNIFORM;
	}

	public DeviationLookaheadList(String key, int minBits, int maxBits) {
		super(key, minBits, maxBits);
		this.deviation = Deviation.UNIFORM;
	}
	
	/**
	 * @param key
	 * @param minEpsilon
	 * @param maxEpsilon
	 */
	public DeviationLookaheadList(String key, double sigma, Deviation devi, boolean randomize) {
		super(key, new Parameter[]{new DoubleParameter("SIGMA", sigma),
				new StringParameter("DIST", devi.toString()),
				new BooleanParameter("R", randomize)});
		this.sigma = sigma; 
		this.deviation = devi;
		this.randomizeOrder = randomize;
	}
	
	protected Partition obfuscatePartition(Partition partition, Random rand) {
		if (this.deviation == Deviation.UNIFORM){
			return obfuscatePartition(partition,rand);
		}
		if (this.deviation == Deviation.NORMAL){
			if (partition instanceof RingPartitionSimple) {
				RingPartitionSimple p = (RingPartitionSimple) partition;
				double sign = rand.nextBoolean() ? 1.0 : -1.0;
				double epsilon = minEpsilon + rand.nextGaussian() * this.sigma;
				double position = p.getId().getPosition() + sign * epsilon;
				return new RingPartitionSimple(new RingIdentifier(position, p
						.getId().getIdSpace()));
			} else if (partition instanceof RingPartition) {
				RingPartition p = (RingPartition) partition;
				double sign1 = rand.nextBoolean() ? 1.0 : -1.0;
				double epsilon1 = minEpsilon + rand.nextGaussian() * this.sigma;
				double position1 = p.getStart().getPosition() + sign1 * epsilon1;
				double sign2 = rand.nextBoolean() ? 1.0 : -1.0;
				double epsilon2 = minEpsilon + rand.nextGaussian() * this.sigma;
				double position2 = p.getEnd().getPosition() + sign2 * epsilon2;
				return new RingPartition(new RingIdentifier(position1, p.getStart()
						.getIdSpace()), new RingIdentifier(position2, p.getEnd()
						.getIdSpace()));
			} else if (partition instanceof PlanePartitionSimple) {
				PlanePartitionSimple p = (PlanePartitionSimple) partition;
				double sign1 = rand.nextBoolean() ? 1.0 : -1.0;
				double epsilon1 = minEpsilon + rand.nextGaussian() * this.sigma;
				double position1 = p.getId().getX() + sign1 * epsilon1;
				double sign2 = rand.nextBoolean() ? 1.0 : -1.0;
				double epsilon2 = minEpsilon + rand.nextGaussian() * this.sigma;
				double position2 = p.getId().getY() + sign2 * epsilon2;
				return new PlanePartitionSimple(new PlaneIdentifier(position1,
						position2, p.getId().getIdSpace()));
			} else {
				throw new RuntimeException(
						"Cannot create obfuscated partition for "
								+ partition.getClass());
			}
		} throw new RuntimeException(
				"Cannot create obfuscated partition for " +this.deviation);
	}
	
	@Override
	public Graph transform(Graph g) {
		Random rand = new Random();
		GraphProperty[] gps = g.getProperties("ID_SPACE");
		for (GraphProperty p : gps) {
			@SuppressWarnings("rawtypes")
			IdentifierSpace ids = (IdentifierSpace) p;
			ArrayList<LookaheadList> lists = new ArrayList<LookaheadList>();
			for (Node n : g.getNodes()) {
				ArrayList<LookaheadElement> list = new ArrayList<LookaheadElement>();

				// add neighbors
				ArrayList<LookaheadElement> neighbors = new ArrayList<LookaheadElement>();
				for (int outIndex : n.getOutgoingEdges()) {
					neighbors.add(new LookaheadElement(
							ids.getPartitions()[outIndex], outIndex));
				}

				// add neighbors' neighbors
				for (LookaheadElement neighbor : neighbors) {
					Node out = g.getNode(neighbor.getVia());
					ArrayList<LookaheadElement> lookahead = new ArrayList<LookaheadElement>();
					for (int lookaheadIndex : out.getOutgoingEdges()) {
						if (lookaheadIndex == n.getIndex()) {
							continue;
						}
						lookahead.add(new LookaheadElement(this
								.obfuscatePartition(
										ids.getPartitions()[lookaheadIndex],
										rand), neighbor.getVia()));
					}
					if (this.randomizeOrder) {
						Collections.shuffle(lookahead);
					}
					list.addAll(lookahead);
				}
				lists.add(new LookaheadList(n.getIndex(), list));
			}
			g.addProperty(g.getNextKey("LOOKAHEAD_LIST"), new LookaheadLists(
					lists));
		}
		return g;
	}

}
