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
import gtna.networks.Network;
import gtna.networks.util.ReadableFile;
import gtna.transformation.Transformation;
import gtna.transformation.attackableEmbedding.lmc.LMC;
import gtna.transformation.attackableEmbedding.swapping.Swapping;
import gtna.transformation.id.RandomRingIDSpaceSimple;
import gtna.transformation.partition.LargestWeaklyConnectedComponent;
import gtna.transformation.remove.RemoveSmallest;
import gtna.util.Config;

/**
 * @author stef
 *
 */
public class CEmbeddings {
	
	public static void main(String[] args){
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
	
//	private static void swapping(int it1,int it2){
//		Network nw = new ReadableFile("SWAP", "SWAP", "data/WOT.txt",new Transformation[]{new RandomRingIDSpaceSimple(),
//			new Swapping(6000)});
//		//Metric[] m = {new CCalculator()};
//		Series.generate(nw, m, it1,it2);
//	}
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
