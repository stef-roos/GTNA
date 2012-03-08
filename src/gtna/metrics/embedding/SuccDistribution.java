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
 * SuccDistribution.java
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
package gtna.metrics.embedding;

import gtna.data.Value;
import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.graph.Node;
import gtna.id.DIdentifierSpace;
import gtna.id.ring.RingIdentifier;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.metrics.MetricImpl;
import gtna.networks.Network;
import gtna.util.Distribution;
import gtna.util.Timer;

import java.util.HashMap;

/**
 * @author stef
 *
 */
public class SuccDistribution extends MetricImpl {

	Timer timer;
	Distribution counts;
	/**
	 * @param key
	 */
	public SuccDistribution() {
		super("SUCC_DISTRIBUTION");
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#computeData(gtna.graph.Graph, gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		timer = new Timer();
		GraphProperty[] gp = g.getProperties("ID_SPACE");
		if (gp.length == 0){
			return;
		}
		GraphProperty p = gp[gp.length - 1];
		DIdentifierSpace idSpaceD = (DIdentifierSpace) p;
		double[] ids = new double[g.getNodes().length];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = ((RingIdentifier)idSpaceD.getPartitions()[i]
					.getRepresentativeID()).getPosition();
		}
		double log2 = Math.log(2);
        double[] minDist = new double[(int)Math.ceil(Math.log(g.getNodes().length)/Math.log(2))];
        int[] out;
        Node[] nodes = g.getNodes();
        for (int j = 0; j < ids.length; j++){
        	out = nodes[j].getOutgoingEdges();
        	int max = 0;
        	int cur;
        	for (int k = 0; k < out.length; k++){
        		cur = (int)Math.floor(Math.log(1/dist(ids[j],ids[out[k]]))/log2);
        		if (cur > max){
        			max = cur;
        		}
        	}
        	if (max >= minDist.length){
        		minDist = expand(minDist,max+1);
        	}
        	minDist[max]++;
        	
        }
        this.counts = new Distribution(minDist);
		timer.end();

	}
	
	private double dist(double a, double b){
		return Math.min(1-a+b,Math.min(1-b+a,Math.abs(a-b)));
	}
	
	public static double[] expand(double[] array, int size) {
	    double[] temp = new double[size];
	    System.arraycopy(array, 0, temp, 0, array.length);
	    for(int j = array.length; j < size; j++)
	        temp[j] = 0;
	    return temp;
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#writeData(java.lang.String)
	 */
	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithIndex(
				this.counts.getDistribution(),
				"SUCC_DISTRIBUTION_DISTRIBUTION", folder);
		success &= DataWriter.writeWithIndex(this.counts.getCdf(),
				"SUCC_DISTRIBUTION_DISTRIBUTION_CDF", folder);
		return success;
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#getValues()
	 */
	@Override
	public Value[] getValues() {
		Value runtime = new Value("SUCC_DISTRIBUTION_RUNTIME",
				this.timer.getRuntime());

		return new Value[] {runtime};
	}

}
