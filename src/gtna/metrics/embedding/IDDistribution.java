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
 * IDDistribution.java
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

import java.util.Arrays;
import java.util.HashMap;

import gtna.data.Value;
import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.id.DIdentifierSpace;
import gtna.id.ring.RingIdentifier;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.metrics.MetricImpl;
import gtna.networks.Network;
import gtna.util.Timer;

/**
 * @author stef
 *
 */
public class IDDistribution extends MetricImpl {
	
 double[] ids;
 Timer timer;
	/**
	 * @param key
	 */
	public IDDistribution() {
		super("ID_DISTRIBUTION");
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
		this.ids = new double[g.getNodes().length];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = ((RingIdentifier)idSpaceD.getPartitions()[i]
					.getRepresentativeID()).getPosition();
		}
        Arrays.sort(ids);
		timer.end();
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#writeData(java.lang.String)
	 */
	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithIndex(
				this.ids,
				"ID_DISTRIBUTION_DISTRIBUTION", folder);
		return success;
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#getValues()
	 */
	@Override
	public Value[] getValues() {
		Value runtime = new Value("ID_DISTRIBUTION_RUNTIME",
				this.timer.getRuntime());

		return new Value[] {runtime};
	}

}
