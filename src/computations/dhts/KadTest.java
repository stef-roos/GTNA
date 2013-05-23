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
		Config.overwrite("MAIN_DATA_FOLDER", "data/kad/");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
       Network kad;
       int mode = Integer.parseInt(args[2]);
       if (mode == 1){
		kad = new Kademlia(100000,128,8,IDSelection.RANDOM,
				null);
       } else {
    	   if (mode == 2){
    		   kad = new KAD(100000,128,10,IDSelection.RANDOM,
    					null);
    	   } else {
    		   kad = new BittorrentKademlia(100000,128,IDSelection.RANDOM,
    					null); 
    	   }
       }

		Metric[] m = new Metric[]{new DegreeDistribution(), new Routing(new KademliaRouting(16,3,2)),
				new Routing(new KademliaRouting(16,4,1))};
		Series.generate(kad, m, Integer.parseInt(args[0]), Integer.parseInt(args[1]));

	}

}
