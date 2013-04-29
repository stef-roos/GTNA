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
 * LookaheadMain.java
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

import gtna.data.Series;
import gtna.metrics.Metric;
import gtna.metrics.routing.Routing;
import gtna.networks.Network;
import gtna.networks.model.smallWorld.Kleinberg1D;
import gtna.networks.model.smallWorld.ScaleFreeUndirected;
import gtna.networks.util.DescriptionWrapper;
import gtna.networks.util.ReadableFile;
import gtna.networks.util.ReadableList;
import gtna.plot.Plotting;
import gtna.routing.greedy.Greedy;
import gtna.routing.greedyVariations.DepthFirstGreedy;
import gtna.routing.lookahead.Lookahead;
import gtna.routing.lookahead.Lookahead.ViaSelection;
import gtna.routing.lookahead.LookaheadGBack;
import gtna.routing.lookahead.LookaheadGDFS;
import gtna.routing.lookahead.LookaheadGNB;
import gtna.routing.lookahead.LookaheadGreedy;
import gtna.transformation.Transformation;
import gtna.transformation.attackableEmbedding.lmc.LMC;
import gtna.transformation.attackableEmbedding.swapping.Swapping;
import gtna.transformation.id.RandomRingIDSpaceSimple;
import gtna.transformation.lookahead.DeviationLookaheadList;
import gtna.transformation.lookahead.PartialLookaheadList.Deviation;
import gtna.util.Config;

/**
 * @author stef
 *
 */
public class LookaheadMain {
	
	public static void main(String[] args) {
//		int[] nodes = {1000,5000,10000};
//		for (int j = 0; j < nodes.length; j++){
//		int n = nodes[j];
//		double[] sigma = {0,10*1/(double)n,1/(double)n,0.1*1/(double)n};
//		double[] greedy = {0, Math.log(n)/(double)n};
//		int ttl = (int)Math.ceil(Math.pow(Math.log(n)/Math.log(2),2));
//		int times = 20;
//		for (int i = 0; i < sigma.length; i++){
//				ScaleFreeUndirected(n,sigma[i], greedy, ttl, times, n+"/",10);
//			
//		}
//		}
		Config.overwrite("SERIES_GRAPH_WRITE", "true");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
//	 	Network nw = new Kleinberg1D(1000,1,1,1,true,true, new Transformation[]{
//	 			new DeviationLookaheadList(0.01, Deviation.UNIFORM ,true)});
//	 	Series.generate(nw, new Metric[]{new Routing(new LookaheadGDFS(1000,ViaSelection.sequential,0))}, 1);
	 	ReadableFile nw1 = new ReadableFile("TEST", "TEST",
	 			"data/KLEINBERG_1D-1000-1-1-1.0-true-true--DEVIATION_LOOKAHEAD_LIST-0.01-UNIFORM-true/0/graph.txt",
	 			null);
	 	Series.generate(nw1, new Metric[]{new Routing(new LookaheadGreedy(Integer.MAX_VALUE,ViaSelection.sequential,0,true,true)),
	 	new Routing(new LookaheadGBack(Integer.MAX_VALUE,ViaSelection.sequential,0,true,true)),
	 	new Routing(new LookaheadGDFS(Integer.MAX_VALUE,ViaSelection.sequential,0,true,true)),
	 	new Routing(new LookaheadGNB(Integer.MAX_VALUE,ViaSelection.sequential,0,true,true))}, 1);
	}
	
	public static void Kleinberg(int nodes, double sigma, double[] greedy, int ttl, int times, String folder){
		Config.overwrite("MAIN_DATA_FOLDER", "data/"+folder);
		double f = Math.sqrt(2*Math.PI)/4.0;
		
		 Config.overwrite("SERIES_GRAPH_WRITE", "true");
		 	Network nw = new Kleinberg1D(nodes,1,1,1,true,true, null);
		Series.generate(nw, new Metric[0], times);
		
		 Config.overwrite("SERIES_GRAPH_WRITE", "false"); 
		 Metric[] m =  new Metric[4*greedy.length+2];
		 for (int i = 0; i < greedy.length; i++){
		m[i*4] = new Routing(new LookaheadGreedy(ttl,Lookahead.ViaSelection.sequential, greedy[i]));
		m[i*4+1] = new Routing(new LookaheadGBack(ttl,Lookahead.ViaSelection.sequential, greedy[i]));
		m[i*4+2] = new Routing(new LookaheadGDFS(ttl,Lookahead.ViaSelection.sequential, greedy[i]));
		m[i*4+3] = new Routing(new LookaheadGNB(ttl,Lookahead.ViaSelection.sequential, greedy[i]));
		 }
		 m[m.length-2] = new Routing(new Greedy(ttl));
		 m[m.length-1] = new Routing(new DepthFirstGreedy(ttl));
		String[] graphs = new String[times];
		for (int j = 0; j < times; j++){
			graphs[j] = "data/"+ folder+ nw.getFolderName()+"/"+j+"/graph.txt";
		}
		
		Network modelUni = new DescriptionWrapper(new ReadableList(nw.getFolderName(), nw.getFolderName(), 
				graphs,new Transformation[]{
			new DeviationLookaheadList(0.01, Deviation.UNIFORM ,true)}), "ModelUni");
		Series s =Series.generate(modelUni, m, times);
		Plotting.multi(s, m, folder+"multi/"+nw.getFolderName()+"/ModelUni/");
		
		Network modelNormal = new DescriptionWrapper(new ReadableList(nw.getFolderName(), nw.getFolderName(), 
				graphs,new Transformation[]{
			new DeviationLookaheadList(f*0.01, Deviation.NORMAL ,true)}), "ModelNormal");
		s =Series.generate(modelNormal, m, times);
		Plotting.multi(s, m, folder+"multi/"+nw.getFolderName()+"/ModelNormal/");
		
		Network randomUni = new DescriptionWrapper(new ReadableList(nw.getFolderName(), nw.getFolderName(), 
				graphs,new Transformation[]{new RandomRingIDSpaceSimple(),
			new DeviationLookaheadList(0.01, Deviation.UNIFORM ,true)}), "RandomUni");
		s =Series.generate(randomUni, m, times);
		Plotting.multi(s, m, folder+"multi/"+nw.getFolderName()+"/RandomUni/");
		
		Network randomNormal = new DescriptionWrapper(new ReadableList(nw.getFolderName(), nw.getFolderName(), 
				graphs,new Transformation[]{new RandomRingIDSpaceSimple(),
			new DeviationLookaheadList(f*0.01, Deviation.NORMAL ,true)}), "RandomNormal");
		s =Series.generate(randomNormal, m, times);
		Plotting.multi(s, m, folder+"multi/"+nw.getFolderName()+"/RandomNormal/");
		
		Network swappingUni = new DescriptionWrapper(new ReadableList(nw.getFolderName(), nw.getFolderName(), 
				graphs,new Transformation[]{new RandomRingIDSpaceSimple(),
			new Swapping(1000),
			new DeviationLookaheadList(0.01, Deviation.UNIFORM ,true)}), "SwappingUni");
		s =Series.generate(swappingUni, m, times);
		Plotting.multi(s, m, folder+"multi/"+nw.getFolderName()+"/SwappingUni/");
		
		Network swappingNormal = new DescriptionWrapper(new ReadableList(nw.getFolderName(), nw.getFolderName(), 
				graphs,new Transformation[]{new RandomRingIDSpaceSimple(),
			new Swapping(1000),
			new DeviationLookaheadList(f*0.01, Deviation.NORMAL ,true)}), "SwappingNormal");
		s =Series.generate(swappingNormal, m, times);
		Plotting.multi(s, m, folder+"multi/"+nw.getFolderName()+"/SwappingNormal/");
		
		Network lmcUni = new DescriptionWrapper(new ReadableList(nw.getFolderName(), nw.getFolderName(), 
				graphs,new Transformation[]{new RandomRingIDSpaceSimple(),
			new LMC(1000, LMC.MODE_UNRESTRICTED, 0, LMC.DELTA_1_N, 0),
			new DeviationLookaheadList(f*0.01, Deviation.UNIFORM ,true)}), "LMCUni");
		s =Series.generate(lmcUni, m, times);
		Plotting.multi(s, m, folder+"multi/"+nw.getFolderName()+"/LMCUni/");
		
		Network lmcNormal =  new DescriptionWrapper(new ReadableList(nw.getFolderName(), nw.getFolderName(), 
				graphs,new Transformation[]{new RandomRingIDSpaceSimple(),
			new LMC(1000, LMC.MODE_UNRESTRICTED, 0, LMC.DELTA_1_N, 0),
			new DeviationLookaheadList(f*0.01, Deviation.NORMAL ,true)}), "LMCNormal");
		s =Series.generate(lmcNormal, m, times);
		Plotting.multi(s, m, folder+"multi/"+nw.getFolderName()+"/LMCNormal/");
		 
	}
	
	public static void ScaleFreeUndirected(int nodes, double sigma, double[] greedy, int ttl, int times, String folder, int C){
		Config.overwrite("MAIN_DATA_FOLDER", "data/"+folder);
		double f = Math.sqrt(2*Math.PI)/4.0;
		
		 Config.overwrite("SERIES_GRAPH_WRITE", "true");
		 Network nw = new ScaleFreeUndirected(nodes,2.3,C,nodes, null);
		Series.generate(nw, new Metric[0], times);
		
		 Config.overwrite("SERIES_GRAPH_WRITE", "false"); 
		 Metric[] m =  new Metric[4*greedy.length+2];
		 for (int i = 0; i < greedy.length; i++){
		m[i*4] = new Routing(new LookaheadGreedy(ttl,Lookahead.ViaSelection.sequential, greedy[i]));
		m[i*4+1] = new Routing(new LookaheadGBack(ttl,Lookahead.ViaSelection.sequential, greedy[i]));
		m[i*4+2] = new Routing(new LookaheadGDFS(ttl,Lookahead.ViaSelection.sequential, greedy[i]));
		m[i*4+3] = new Routing(new LookaheadGNB(ttl,Lookahead.ViaSelection.sequential, greedy[i]));
		 }
		 m[m.length-2] = new Routing(new Greedy(ttl));
		 m[m.length-1] = new Routing(new DepthFirstGreedy(ttl));
		String[] graphs = new String[times];
		for (int j = 0; j < times; j++){
			graphs[j] = "data/"+ folder+ nw.getFolderName()+"/"+j+"/graph.txt";
		}
		
		Network modelUni = new DescriptionWrapper(new ReadableList(nw.getFolderName(), nw.getFolderName(), 
				graphs,new Transformation[]{
			new DeviationLookaheadList(0.01, Deviation.UNIFORM ,true)}), "ModelUni");
		Series s =Series.generate(modelUni, m, times);
		Plotting.multi(s, m, folder+"multi/"+nw.getFolderName()+"/ModelUni/");
		
		Network modelNormal = new DescriptionWrapper(new ReadableList(nw.getFolderName(), nw.getFolderName(), 
				graphs,new Transformation[]{
			new DeviationLookaheadList(f*0.01, Deviation.NORMAL ,true)}), "ModelNormal");
		s =Series.generate(modelNormal, m, times);
		Plotting.multi(s, m, folder+"multi/"+nw.getFolderName()+"/ModelNormal/");
		
		Network randomUni = new DescriptionWrapper(new ReadableList(nw.getFolderName(), nw.getFolderName(), 
				graphs,new Transformation[]{new RandomRingIDSpaceSimple(),
			new DeviationLookaheadList(0.01, Deviation.UNIFORM ,true)}), "RandomUni");
		s =Series.generate(randomUni, m, times);
		Plotting.multi(s, m, folder+"multi/"+nw.getFolderName()+"/RandomUni/");
		
		Network randomNormal = new DescriptionWrapper(new ReadableList(nw.getFolderName(), nw.getFolderName(), 
				graphs,new Transformation[]{new RandomRingIDSpaceSimple(),
			new DeviationLookaheadList(f*0.01, Deviation.NORMAL ,true)}), "RandomNormal");
		s =Series.generate(randomNormal, m, times);
		Plotting.multi(s, m, folder+"multi/"+nw.getFolderName()+"/RandomNormal/");
		
		Network swappingUni = new DescriptionWrapper(new ReadableList(nw.getFolderName(), nw.getFolderName(), 
				graphs,new Transformation[]{new RandomRingIDSpaceSimple(),
			new Swapping(1000),
			new DeviationLookaheadList(0.01, Deviation.UNIFORM ,true)}), "SwappingUni");
		s =Series.generate(swappingUni, m, times);
		Plotting.multi(s, m, folder+"multi/"+nw.getFolderName()+"/SwappingUni/");
		
		Network swappingNormal = new DescriptionWrapper(new ReadableList(nw.getFolderName(), nw.getFolderName(), 
				graphs,new Transformation[]{new RandomRingIDSpaceSimple(),
			new Swapping(1000),
			new DeviationLookaheadList(f*0.01, Deviation.NORMAL ,true)}), "SwappingNormal");
		s =Series.generate(swappingNormal, m, times);
		Plotting.multi(s, m, folder+"multi/"+nw.getFolderName()+"/SwappingNormal/");
		
		Network lmcUni = new DescriptionWrapper(new ReadableList(nw.getFolderName(), nw.getFolderName(), 
				graphs,new Transformation[]{new RandomRingIDSpaceSimple(),
			new LMC(1000, LMC.MODE_UNRESTRICTED, 0, LMC.DELTA_1_N, 0),
			new DeviationLookaheadList(f*0.01, Deviation.UNIFORM ,true)}), "LMCUni");
		s =Series.generate(lmcUni, m, times);
		Plotting.multi(s, m, folder+"multi/"+nw.getFolderName()+"/LMCUni/");
		
		Network lmcNormal =  new DescriptionWrapper(new ReadableList(nw.getFolderName(), nw.getFolderName(), 
				graphs,new Transformation[]{new RandomRingIDSpaceSimple(),
			new LMC(1000, LMC.MODE_UNRESTRICTED, 0, LMC.DELTA_1_N, 0),
			new DeviationLookaheadList(f*0.01, Deviation.NORMAL ,true)}), "LMCNormal");
		s =Series.generate(lmcNormal, m, times);
		Plotting.multi(s, m, folder+"multi/"+nw.getFolderName()+"/LMCNormal/");
		 
	}

}
