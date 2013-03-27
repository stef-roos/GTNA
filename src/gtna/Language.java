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
 * Language.java
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

import gtna.metrics.Metric;
import gtna.metrics.basic.ClusteringCoefficient;
import gtna.metrics.basic.DegreeDistribution;
import gtna.networks.Network;
import gtna.networks.util.ReadableFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author stef
 *
 */
public class Language {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String[] graphs = new File("POS_3lang_try3/").list();
		String[] lang = {"de", "en", "fr"};
		String[] type = {"ADJ-NAME", "ADJ-NOUN", "ADJ-VERB", "NOUN-NAME", "NOUN-VERB", "VERB-NAME"};
		String[] sig = {""}; 
        try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("transitivityPOS_3lang_try3.txt"));
			bw.write("#Graph, nodes, edges, average degree, transitivity");
			for (int i = 0; i < lang.length; i++){
				for (int j = 0; j < type.length; j++){
					for (int k = 0; k < sig.length; k++){
						Network net = new ReadableFile("L2", "L2","POS_3lang_try3/"+lang[i]+"1M-POS6K."+type[j],null);
						Metric m = new ClusteringCoefficient();
						m.computeData(net.generate(), net, null);
						Metric m2 = new DegreeDistribution();
						m2.computeData(net.generate(), net, null);
						bw.newLine();
						bw.write(lang[i]+"1M_POS6K"+type[j]+ ":	"
						          +m2.getSingles()[0].value+ "	" 
						          +m2.getSingles()[1].value+ "	"
						          +m2.getSingles()[4].value+ "	"
						          +m.getSingles()[1].value);
						
					}
				}
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
