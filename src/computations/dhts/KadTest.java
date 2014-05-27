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
import gtna.networks.p2p.kademlia.KAD;
import gtna.networks.p2p.kademlia.Kademlia;
import gtna.routing.p2p.KademliaRouting;
import gtna.routing.p2p.KademliaRoutingFailure;
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
		int mode = Integer.parseInt(args[0]);
		int n = Integer.parseInt(args[1]);
		int start = Integer.parseInt(args[2]);
		int end = Integer.parseInt(args[3]);
		//double p = Double.parseDouble(args[4]);
//		//Config.overwrite("MAIN_DATA_FOLDER", "/home/stef/svns/drafts/p2pmodel/results/simu/");
//		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
////		Network kad = new KademliaEclipse(1000,128,8,IDSelection.RANDOM,10, AttackerSelection.TARGET, 0,
////				null);
//		Network kad;
//		switch (mode){
//		case 1: kad = new Kademlia(n,128,8,IDSelection.RANDOM,
//				null); break;
//		case 2: kad = new BittorrentKademlia(n,128,IDSelection.RANDOM,
//				null); break;
//		case 3: kad = new KAD(100000,128,10,IDSelection.RANDOM,
//				null); break;	
//		default: throw new IllegalArgumentException("unknown mode");		
//		}
		//Config.overwrite(key, value)
		 Network kad = new Kademlia(n,128,8,IDSelection.RANDOM,
					null);
		Metric[] m = new Metric[]{new DegreeDistribution(), 
				new Routing(new KademliaRoutingFailure(100,3,2,0.0)),new Routing(new KademliaRoutingFailure(100,4,1, 0.0)),
				new Routing(new KademliaRoutingFailure(100,3,2,0.1)),new Routing(new KademliaRoutingFailure(100,4,1, 0.1)),
				new Routing(new KademliaRoutingFailure(100,3,2,0.2)),new Routing(new KademliaRoutingFailure(100,4,1, 0.2)),
				new Routing(new KademliaRoutingFailure(100,3,2,0.3)),new Routing(new KademliaRoutingFailure(100,4,1, 0.3))};
		Series.generate(kad, m, start, end);

	}

}
