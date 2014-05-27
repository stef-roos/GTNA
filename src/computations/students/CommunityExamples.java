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
 * CommunityExamples.java
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
import gtna.metrics.basic.ClusteringCoefficient;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.basic.ShortestPaths;
import gtna.networks.Network;
import gtna.networks.util.ReadableFile;
import gtna.util.Config;

/**
 * @author stef
 *
 */
public class CommunityExamples {
	
	public static void main(String[] args) {
//		Config.overwrite("MAIN_DATA_FOLDER", "data/community/");
//		Metric c = new Communities();
//		Metric[] metrics = new Metric[] { c };
//
//		Transformation[] t1 = new Transformation[] { new CDLPA(100) };
////		int[] n = {1000,5000,10000,20000};
////		int[] d = {1,2,3,5};
////		for (int i = 0; i < n.length; i++){
////			for (int j = 0; j < d.length; j++){
////		Network nw = new BarabasiAlbert(n[i],d[j],t1);
////        Series s = Series.generate(nw, metrics, 10);
////			}
////		}
//		Network nw = new ReadableFile("WOT-2005", "WOT-2005", "../graphs/WOT-BI-GCC/2005-02-25.wot.txt", t1);
//		Series s = Series.generate(nw, metrics, 10);
		Config.overwrite("SERIES_GRAPH_WRITE", ""+true);
		Config.overwrite("MAIN_DATA_FOLDER", "data/testgraphen-darknet/");
//		int[] n = {50,100,200,500,1000,2000,4000,8000,16000,24000,32000};
//		for (int i = 0; i < n.length; i++){
//		Network net = new ScaleFreeUndirected(n[i], 2.3, 2, (int)Math.sqrt(n[i]), null);
//		Series.generate(net, new Metric[]{new DegreeDistribution(), new ShortestPaths(), new ClusteringCoefficient()}, 10);
//		}
		String[] names = {"Karate", "SPI", "WOT-2005", "WOT-2011"};
		String[] files = {"../graphs/karate.txt", "../graphs/spi.txt", "../graphs/WOT-BI-GCC/2005-02-25.wot.txt", 
				"../graphs/WOT-BI-GCC/2011-01-01.wot.txt"};
		for (int i = 0; i < names.length; i++){
		Network net = new ReadableFile(names[i], names[i], files[i], null);
		Series.generate(net, new Metric[]{new DegreeDistribution(), new ShortestPaths(), new ClusteringCoefficient()}, 1);
		}
//		Plotting.multi(s, metrics, "spi-multi/");
//		Plotting.single(s, metrics, "spi-single/");

	}

}
