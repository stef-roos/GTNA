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
 * NodesTest.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: benni;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna;

import gtna.graph.Graph;
import gtna.io.GraphWriter;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.transformation.Transformation;
import gtna.transformation.id.RandomRingIDSpace;
import gtna.transformation.lookahead.RandomLookaheadList;
import gtna.transformation.lookahead.RandomObfuscatedLookaheadList;
import gtna.util.Stats;

/**
 * @author benni
 * 
 */
public class NodesTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Stats stats = new Stats();

		Network nw = new ErdosRenyi(3, 2, false, null, null);
		Graph g = nw.generate();

		Transformation t1 = new RandomRingIDSpace(1, 1.0, true);
		Transformation t2 = new RandomLookaheadList();
		Transformation t3 = new RandomObfuscatedLookaheadList(0.001, 0.002);

		System.out.println(t1.name());
		System.out.println(t2.name());
		System.out.println(t3.name());

		g = t1.transform(g);
		g = t2.transform(g);
		g = t3.transform(g);
		
		GraphWriter.writeWithProperties(g, "./temp/test/randomObfuscated.txt");

		stats.end();
	}

}
