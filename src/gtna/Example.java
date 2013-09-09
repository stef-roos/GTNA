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
 * Example.java
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

import gtna.data.Series;
import gtna.graph.sorting.CCSorterUpdate;
import gtna.graph.sorting.CCSorterUpdate.Computation;
import gtna.graph.sorting.DegreeNodeSorterUpdate;
import gtna.graph.sorting.NodeSorter.NodeSorterMode;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.fragmentation.FragmentationRecompute;
import gtna.metrics.fragmentation.StrongFragmentationRecompute;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.model.randomGraphs.PowerLawRandomGraph;
import gtna.networks.util.DescriptionWrapper;
import gtna.networks.util.ReadableFile;
import gtna.plot.Plotting;
import gtna.util.Config;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

/**
 * @author benni
 * 
 */
public class Example {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config.overwrite("MAIN_DATA_FOLDER", "./data/"+args[0]+"/");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "true");
		int start = Integer.parseInt(args[1]);
		int end = Integer.parseInt(args[2]);
		double a1 = Double.parseDouble(args[3]);
		double a2 = Double.parseDouble(args[4]);
		int times = Integer.parseInt(args[5]);

		for (int i = start; i <=end; i++){
			int n = (int)Math.pow(2, i);
		Metric[] metrics = new Metric[] { new DegreeDistribution(), new StrongFragmentationRecompute(
				new DegreeNodeSorterUpdate(NodeSorterMode.DESC), FragmentationRecompute.Resolution.SINGLE,
				false),
				new StrongFragmentationRecompute(
				new CCSorterUpdate(false,Computation.DEGREEBASED), FragmentationRecompute.Resolution.SINGLE,
				false), new StrongFragmentationRecompute(
						new CCSorterUpdate(false,Computation.NODEBASED), FragmentationRecompute.Resolution.SINGLE,
						false)
				 };
		
		
		//Network er1 = new ErdosRenyi(n,deg,false,null);
		Network net = new PowerLawRandomGraph(n,a1,a2,1,1,n,n,null);
		Series.generate(net, metrics, times);
		}
//		er(metrics, times);
//		as(metrics, times);

		

	}

	public static void er(Metric[] metrics, int times) {
		Network er1 = new ErdosRenyi(1000, 5, false, null);
		Network er2 = new ErdosRenyi(1000, 10, false, null);
		Network er3 = new ErdosRenyi(1000, 15, false, null);
		Network er4 = new ErdosRenyi(1000, 20, false, null);
		Network er5 = new ErdosRenyi(2000, 5, false, null);
		Network er6 = new ErdosRenyi(2000, 10, false, null);
		Network er7 = new ErdosRenyi(2000, 15, false, null);
		Network er8 = new ErdosRenyi(2000, 20, false, null);
		Network er9 = new ErdosRenyi(3000, 5, false, null);
		Network er10 = new ErdosRenyi(3000, 10, false, null);
		Network er11 = new ErdosRenyi(3000, 15, false, null);
		Network er12 = new ErdosRenyi(3000, 20, false, null);

		Network[] nw1 = new Network[] { er1, er2, er3, er4 };
		Network[] nw2 = new Network[] { er5, er6, er7, er8 };
		Network[] nw3 = new Network[] { er9, er10, er11, er12 };

		Network[][] nw = new Network[][] { nw1, nw2, nw3 };

		Series[][] s = Series.generate(nw, metrics, times);
		Series[] s1 = Series.get(nw1, metrics);
		Series[] s2 = Series.get(nw2, metrics);
		Series[] s3 = Series.get(nw3, metrics);

		Plotting.multi(s, metrics, "ER-multi/");
		Plotting.multi(s1, metrics, "ER-multi-1/");
		Plotting.multi(s2, metrics, "ER-multi-2/");
		Plotting.multi(s3, metrics, "ER-multi-3/");
		Plotting.single(s, metrics, "ER-single/");
	}

	public static void as(Metric[] metrics, int times) {
		Network caida1 = getAS("cycle-aslinks.l7.t1.c000027.20070913.txt.gtna",
				2007);
		Network caida2 = getAS("cycle-aslinks.l7.t1.c000065.20080102.txt.gtna",
				2008);
		Network caida3 = getAS("cycle-aslinks.l7.t1.c000359.20090103.txt.gtna",
				2009);
		Network caida4 = getAS("cycle-aslinks.l7.t1.c000770.20100101.txt.gtna",
				2010);
		Network caida5 = getAS("cycle-aslinks.l7.t1.c001255.20110102.txt.gtna",
				2011);
		Network caida6 = getAS("cycle-aslinks.l7.t1.c001784.20120102.txt.gtna",
				2012);

		Network[] nw1 = new Network[] { caida1, caida2, caida3, caida4, caida5,
				caida6 };

		Series[] s1 = Series.generate(nw1, metrics, times);

		Plotting.multi(s1, metrics, "AS-multi/");
		Plotting.single(s1, metrics, "AS-single/");
	}

	private static Network getAS(String name, int year) {
		return new DescriptionWrapper(new ReadableFile("caida", "caida",
				"resources/caida/original/" + name, null), "",
				new Parameter[] { new IntParameter("YEAR", year) });
	}
}
