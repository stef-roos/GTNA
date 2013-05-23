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
 * FreenettestGraphs.java
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
package computations.embedding;

import gtna.data.Series;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.networks.canonical.Complete;
import gtna.networks.model.BarabasiAlbert;
import gtna.networks.model.smallWorld.Kleinberg1D;
import gtna.util.Config;

/**
 * @author stef
 *
 */
public class FreenettestGraphs {
	public static void main(String[] args){
		Config.overwrite("SERIES_GRAPH_WRITE", "true");
//		Network nw = new Kleinberg1D(32,1,1,1,true,false,null);
//		Series.generate(nw, new Metric[0], 1);
//		nw = new Kleinberg1D(16,1,1,1,true,false,null);
//		Series.generate(nw, new Metric[0], 1);
//		
//		nw = new Complete(16,null);
//		Series.generate(nw, new Metric[0], 1);
//		nw = new Complete(32,null);
//		Series.generate(nw, new Metric[0], 1);
		
//		nw = new BarabasiAlbert(16,1,null);
//		Series.generate(nw, new Metric[0], 1);
		Network nw = new BarabasiAlbert(200,2,null);
		Series.generate(nw, new Metric[0], 1);
	}

}
