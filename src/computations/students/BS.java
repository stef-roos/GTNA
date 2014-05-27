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
 * BS.java
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
package computations.students;

import gtna.data.Series;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.networks.Network;
import gtna.networks.model.smallWorld.ScaleFreeUndirected;
import gtna.util.Config;

/**
 * @author stef
 *
 */
public class BS {
	
	public static void main(String[] args) {
        Config.overwrite("SERIES_GRAPH_WRITE", ""+true);
//		double[] dd = new double[401];
//		dd[5] = 0.6;
//		dd[10] = 0.85;
//		dd[40] = 0.95;
//		dd[400] = 1;
//		Network nw = new BubblestormGraph(2025,dd, "Hetero",null);
//		Series s = Series.generate(nw, new Metric[]{new DegreeDistribution(), new ShortestPaths()}, 1);
        for (int i = 1; i < 6; i++){
		Network nw = new ScaleFreeUndirected(i*2000,2.3,1,i*2000,null);
		Series s = Series.generate(nw, new Metric[]{new DegreeDistribution()}, 10);
        }
		
	}

}
