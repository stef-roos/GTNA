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
 * KadTest.java
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
package computations.dhts;

import gtna.data.Series;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.routing.Routing;
import gtna.networks.Network;
import gtna.networks.p2p.chord.Chord.IDSelection;
import gtna.networks.p2p.kademlia.BittorrentKademlia;
import gtna.routing.p2p.KademliaRouting;
import gtna.util.Config;

/**
 * @author stef
 *
 */
public class KadTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config.overwrite("MAIN_DATA_FOLDER", "/home/stef/svns/drafts/p2pmodel/results/simu/");
		//Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
//		Network kad = new KademliaEclipse(1000,128,8,IDSelection.RANDOM,10, AttackerSelection.TARGET, 0,
//				null);
		Network kad = new BittorrentKademlia(100000,128,IDSelection.RANDOM,
				null);
//		Metric[] m = new Metric[]{new Routing(new PastryRouting())};
//		Series.generate(pastry, m, 10);
//		Metric[] m = new Metric[]{new DegreeDistribution(), 
//				new RoutingAttack(new KademliaRoutingFailure(30,3,2,0.1))};
		Metric[] m = new Metric[]{new DegreeDistribution(), 
				new Routing(new KademliaRouting(16,3,2)), new Routing(new KademliaRouting(16,4,1))};
		Series.generate(kad, m, 20);

	}

}
