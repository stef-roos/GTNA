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
 * RandomWalksTest.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: stefanie;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package tests;

import gtna.data.Series;
import gtna.metrics.Metric;
import gtna.metrics.randomwalk.SimpleRandomWalk;
import gtna.networks.Network;
import gtna.networks.canonical.Complete;
import gtna.util.Config;

/**
 * @author stefanie
 *
 */
public class RandomWalksTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		Network complete = new Complete(16,null);
		Series.generate(complete, new Metric[] {new SimpleRandomWalk(true,1,false,getProb(4))}, 5);

	}
	
	public static double getProb(int exp){
		//exp = 1/(1-p) (geometric series)=> p = 1-1/exp
		return 1 - 1/(double)exp; 
	}

}
