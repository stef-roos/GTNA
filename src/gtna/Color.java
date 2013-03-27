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
 * Color.java
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

import gtna.drawing.DegreeColorizer;
import gtna.drawing.GephiDecorator;
import gtna.drawing.GraphPlotter;
import gtna.graph.Graph;
import gtna.id.IdentifierSpace;
import gtna.networks.Network;
import gtna.networks.canonical.Star;
import gtna.transformation.Transformation;
import gtna.transformation.id.RandomRingIDSpace;

/**
 * @author stef
 *
 */
public class Color {
	
	public static void main(String[] args){
		Transformation t = new RandomRingIDSpace();
		Network net = new Star(6,new Transformation[]{new RandomRingIDSpace()});
		Graph g = t.transform(net.generate());
		//System.out.println(g.hasProperty("ID_SPACE"));
		GephiDecorator[] gd = new GephiDecorator[]{new DegreeColorizer()}; 
		GraphPlotter gp = new GraphPlotter("Test", "Test", gd, -1);
		gp.plot(g, (IdentifierSpace) g.getProperty("ID_SPACE_0"), "test");
	}

}
