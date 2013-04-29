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
 * DHTs.java
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
package gtna;

import gtna.data.Series;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.routing.Routing;
import gtna.networks.Network;
import gtna.networks.p2p.chord.Chord;
import gtna.networks.p2p.chord.Chord.IDSelection;
import gtna.routing.greedy.GreedyNode;
import gtna.util.Config;

/**
 * @author stef
 *
 */
public class DHTs {
	
	public static void main(String[] args) {
		Config.overwrite("MAIN_DATA_FOLDER", "data/example/");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		Config.overwrite("SERIES_GRAPH_WRITE", "true");
		//Network pastry = new Pastry(100,128,4,16,IDSelection.RANDOM,null);
		Network chord = new Chord(1000,128,IDSelection.RANDOM,null);
//		Metric[] m = new Metric[]{new Routing(new PastryRouting())};
//		Series.generate(pastry, m, 10);
		Metric[] m = new Metric[]{new DegreeDistribution(), new Routing(new GreedyNode())};
		Series.generate(chord, m, 30);
	}

}
