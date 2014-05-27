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
 * CEmbeddings.java
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
package computations.embedding;

import gtna.data.Series;
import gtna.metrics.Metric;
import gtna.metrics.routing.Routing;
import gtna.networks.Network;
import gtna.networks.util.ReadableFile;
import gtna.routing.greedyVariations.DepthFirstGreedy;
import gtna.transformation.Transformation;
import gtna.transformation.attackableEmbedding.swapping.Swapping;
import gtna.transformation.id.RandomRingIDSpaceSimple;
import gtna.util.Config;

/**
 * @author stef
 *
 */
public class CEmbeddings {
	
	public static void main(String[] args){
		//System.out.println(Math.pow(Math.log(41688)/Math.log(2), 2));
        Config.overwrite("MAIN_DATA_FOLDER", "../VirtualOverlays/data/churnDarknet/");
        swappingAll("WOT","/home/stef/workspace/graphs/WOT-ROD-GCC/2011-01-01.wot.txt",30,235);
		//swappingAll("FBUn","/home/stef/workspace/graphs/facebook-wosn-links/facebook-wosn-GCC.txt", 30);
//		String path = args[0];
//		String name = args[1];
//		int start = Integer.parseInt(args[2]);
//		int end = Integer.parseInt(args[3]);
//		int ttl = (int) Math.pow(Math.log(Integer.parseInt(args[4]))/Math.log(2),2);
//		swapping(path,name,start,end,ttl);
//		Network nw = new ReadableFile("LMC", "LMC", "data/WOT.txt",new Transformation[]{new RandomRingIDSpaceSimple(),
//				new LMC(6000, LMC.MODE_UNRESTRICTED,0, LMC.DELTA_1_N,0)});
//			Metric[] m = {new CCalculator()};
//			Series.generate(nw, m, 20);
		//swapping(20);
		//if (args[0] == "lmc"){
		//	lmc(20);
		//}else {
			//swapping(it1,it2);
		//}
	}
	
	private static void swapping(String name, String path,int it1,int it2,int ttl){
		Network nw = new ReadableFile(name, name, path,new Transformation[]{new RandomRingIDSpaceSimple(),
			new Swapping(6000)});
		Metric[] m = {new Routing(new DepthFirstGreedy(ttl))};
		Series.generate(nw, m, it1,it2);
	}
	
	private static void swappingAll(String name, String path,int nr,int ttl){
		Network nw = new ReadableFile(name, name, path,new Transformation[]{new RandomRingIDSpaceSimple(),
			new Swapping(6000)});
		Metric[] m = {new Routing(new DepthFirstGreedy(ttl))};
		Series.generate(nw, m, nr);
	}
	
	private static void swapping(String name, String path,int it1,int it2){
		Network nw = new ReadableFile(name, name, path,new Transformation[]{new RandomRingIDSpaceSimple(),
			new Swapping(6000)});
		Metric[] m = {new Routing(new DepthFirstGreedy(),1)};
		Series.generate(nw, m, it1,it2);
	}
	
	private static void swappingAll(String name, String path,int nr){
		Network nw = new ReadableFile(name, name, path,new Transformation[]{new RandomRingIDSpaceSimple(),
			new Swapping(6000)});
		Metric[] m = {new Routing(new DepthFirstGreedy(),1)};
		Series.generate(nw, m, nr);
	}
//	
//	private static void lmc(int it1,int it2){
//		Network nw = new ReadableFile("LMC", "LMC", "data/WOT.txt",new Transformation[]{new RandomRingIDSpaceSimple(),
//			new LMC(6000, LMC.MODE_UNRESTRICTED,0, LMC.DELTA_1_N,0)});
//		Metric[] m = {new CCalculator()};
//		Series.generate(nw, m, it1,it2);
//	}
//	
//	private static void swapping(int it1){
//		Network nw = new ReadableFile("SWAP", "SWAP", "data/WOT.txt",new Transformation[]{new RandomRingIDSpaceSimple(),
//			new Swapping(6000)});
//		Metric[] m = {new CCalculator()};
//		Series.generate(nw, m, it1);
//	}
//	
//	private static void lmc(int it1){
//		Network nw = new ReadableFile("LMC", "LMC", "data/WOT.txt",new Transformation[]{new RandomRingIDSpaceSimple(),
//			new LMC(6000, LMC.MODE_UNRESTRICTED,0, LMC.DELTA_1_N,0)});
//		Metric[] m = {new CCalculator()};
//		Series.generate(nw, m, it1);
//	}
//	
//	private static void makeReducedGraph(String name, String file){
//		Config.overwrite("SERIES_GRAPH_WRITE", "true");
//		Network nw = new ReadableFile(name,name,file,new Transformation[]{new LargestWeaklyConnectedComponent(),new RemoveSmallest(2,RemoveSmallest.Type.IN,true)});
//		Series.generate(nw, new Metric[0], 1);
//	}

}
