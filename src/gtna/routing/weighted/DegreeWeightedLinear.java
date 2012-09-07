package gtna.routing.weighted;

import gtna.graph.Node;
import gtna.id.BIIdentifier;
import gtna.id.DIdentifier;
import gtna.routing.greedyVariations.NodeGreedy;

import java.math.BigInteger;
import java.util.Random;

public class DegreeWeightedLinear  extends NodeGreedy {

			public DegreeWeightedLinear() {
				super("DEGREE_WEIGHTED_LINEAR");
			}

			public DegreeWeightedLinear(int ttl) {
				super(ttl, "DEGREE_WEIGHTED_LINEAR");
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
					if (dist/(double)nodes[out].getDegree() < minDist && dist < currentDist) {
						minDist = dist/(double)nodes[out].getDegree();
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
