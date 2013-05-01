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
 * PartialLookahaed.java
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
import gtna.networks.util.ReadableList;
import gtna.plot.Plotting;
import gtna.routing.greedy.Greedy;
import gtna.routing.greedyVariations.DepthFirstGreedy;
import gtna.routing.lookahead.Lookahead.ViaSelection;
import gtna.routing.lookahead.LookaheadGDFS;
import gtna.routing.lookahead.LookaheadGreedy;
import gtna.transformation.Transformation;
import gtna.transformation.attackableEmbedding.lmc.LMC;
import gtna.transformation.attackableEmbedding.swapping.Swapping;
import gtna.transformation.id.RandomRingIDSpaceSimple;
import gtna.transformation.util.RemoveGraphProperty;
import gtna.util.Config;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;



/**
 * @author stef
 *
 */
public class PartialLookahaed {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		allPlots();
//		Config.overwrite("PLOT_EXTENSION", ".png");;
//		Config.overwrite("GNUPLOT_TERMINAL", "png");
//		cluster("Kleinberg1000/random/", "Kleinberg1000-random");
//		cluster("Kleinberg1000/original/", "Kleinberg1000-original");
//		cluster("Kleinberg1000/lmc/", "Kleinberg1000-lmc");
//		cluster("Kleinberg1000/swapping/", "Kleinberg1000-swapping");
//		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
//		Network nw = new Kleinberg1D(100,1,1,1,true,true, new Transformation[]{
//				new DeviationLookaheadList(0.01, Deviation.UNIFORM ,true)});
//		Metric[] m = new Metric[]{new Routing(new LookaheadGBack(ViaSelection.sequential)),
//				new Routing(new LookaheadGBack(Integer.MAX_VALUE,ViaSelection.sequential,0.0,true))};
//		Series.generate(nw, m, 1);
//		makeGraphsScalefree(1000, 30, "ScaleFree1000-10-2.3/", 10, 2.3);
//		makeGraphsScalefree(5000, 30, "ScaleFree5000-10-2.3/", 10, 2.3);
//		makeGraphsScalefree(10000, 30, "ScaleFree10000-10-2.3/", 10, 2.3);
//		makeGraphsScalefree(50000, 30, "ScaleFree50000-10-2.3/", 10, 2.3);
//		int[] n = {1000,5000,10000,50000};
//		String[] subfolders = {"original/", "random/", "swapping/", "lmc/"};
//		for (int i = 0; i < n.length; i++){
//			double[] sigma = {(double)1/Math.sqrt(n[i]), (double)10/(double)(n[i]), (double)2/(double)(n[i]),
//					(double)1/(double)(n[i]), (double)1/(double)(2*n[i]), (double)1/(double)(10*n[i]),
//					(double)1/(double)Math.pow(n[i], 2)};
//			PartialLookaheadList[] listDe = new PartialLookaheadList[sigma.length];
//			PartialLookaheadList[] listPair = new PartialLookaheadList[sigma.length];
//			for (int k = 0; k < sigma.length; k++){
//				listDe[k] = new DeviationLookaheadList(sigma[k], Deviation.UNIFORM, true);
//				listPair[k] = new PairwiseLookaheadList(sigma[k], Deviation.UNIFORM, true);
//			}
//			for (int j = 0; j < subfolders.length; j++){
//				for (int k = 0; k < listDe.length; k++){
//					writeLookahead("Kleinberg"+n[i]+"/"+subfolders[j], "LALists/Kleinberg"+n[i]+"/"+subfolders[j],
//							"Kleinberg"+n[i]+subfolders[j]+"Devi"+sigma[k],30, listDe[k] );
//					writeLookahead("Kleinberg"+n[i]+"/"+subfolders[j], "LALists/Kleinberg"+n[i]+"/"+subfolders[j],
//							"Kleinberg"+n[i]+subfolders[j]+"Pair"+sigma[k],30, listPair[k] );
//					writeLookahead("ScaleFree"+n[i]+"-10-2.3/"+subfolders[j], "LALists/ScaleFree"+n[i]+"-10-2.3/"+subfolders[j],
//							"ScaleFree"+n[i]+"-10-2.3"+subfolders[j]+"Devi"+sigma[k],30, listDe[k] );
//					writeLookahead("ScaleFree"+n[i]+"-10-2.3/"+subfolders[j], "LALists/ScaleFree"+n[i]+"-10-2.3/"+subfolders[j],
//							"ScaleFree"+n[i]+"-10-2.3"+subfolders[j]+"Pair"+sigma[k],30, listPair[k] );
//				}
//			}
//		}
		
//		String[] sub1 = (new File("data/LALists")).list();
//		for (int i1 = 0; i1 < sub1.length; i1++){
//			String name = sub1[i1];
//			int nodes = Integer.parseInt(name.split("-")[0].replace("Kleinberg", "").replace("ScaleFree", ""));
//			int ttl = (int)Math.ceil(Math.pow(Math.log(nodes)/Math.log(2),2));
//			String[] sub2 = (new File("data/LALists/"+sub1[i1])).list();
//			for (int i2 = 0; i2 < sub2.length; i2++){
//				name = name + sub2[i2];
//				String[] sub3 = (new File("data/LALists/"+sub1[i1] + "/" + sub2[i2])).list();
//				for (int i3 = 0; i3 < sub3.length; i3++){
//					String[] sub4 = (new File("data/LALists/"+sub1[i1] + "/" + sub2[i2] + "/" + sub3[i3])).list();
//					for (int i4 = 0; i4 < sub4.length; i4++){
//						String list = sub4[i4].split("--")[0];
//						name = name + list;
//						double g = Double.parseDouble(list.replace("Devi", "").replace("Pair", ""));
//						g = Math.log(1/g)/Math.log(2)*g;
//						Metric[] m = new Metric[]{new Routing(new LookaheadGreedy(ttl, ViaSelection.sequential, 0, false, false)),
//								new Routing(new LookaheadGreedy(ttl, ViaSelection.sequential, g, false, false)),
//								new Routing(new LookaheadGreedy(ttl, ViaSelection.sequential, 0, true, false)),
//								new Routing(new LookaheadGreedy(ttl, ViaSelection.sequential, g, true, false)),
//								new Routing(new LookaheadGDFS(ttl, ViaSelection.sequential, 0, false, false)),
//								new Routing(new LookaheadGDFS(ttl, ViaSelection.sequential, g, false, false)),
//								new Routing(new LookaheadGDFS(ttl, ViaSelection.sequential, 0, true, false)),
//								new Routing(new LookaheadGDFS(ttl, ViaSelection.sequential, g, true, false)),
//								new Routing(new LookaheadGreedy(ttl, ViaSelection.sequential, 0, false, true)),
//								new Routing(new LookaheadGreedy(ttl, ViaSelection.sequential, g, false, true)),
//								new Routing(new LookaheadGreedy(ttl, ViaSelection.sequential, 0, true, true)),
//								new Routing(new LookaheadGreedy(ttl, ViaSelection.sequential, g, true, true)),
//								new Routing(new LookaheadGDFS(ttl, ViaSelection.sequential, 0, false, true)),
//								new Routing(new LookaheadGDFS(ttl, ViaSelection.sequential, g, false, true)),
//								new Routing(new LookaheadGDFS(ttl, ViaSelection.sequential, 0, true, true)),
//								new Routing(new LookaheadGDFS(ttl, ViaSelection.sequential, g, true, true)),
//								new Routing(new Greedy(ttl)),
//								new Routing(new DepthFirstGreedy(ttl))};
//						performMetric("LALists/"+sub1[i1]+ "/"+sub2[i2]+ "/"+sub3[i3]+ "/"+sub4[i4]+ "/", m,
//								"Routing/"+sub1[i1]+ "/"+sub2[i2]+ "/"+sub3[i3]+ "/"+sub4[i4]+ "/", name, 30);
//						name = sub1[i1] + sub2[i2];
//					}
//				}
//				name = sub1[i1];
//			}
//		}
	}
	
	public static void cluster(String folder, String name){
		int nodes = Integer.parseInt(name.split("-")[0].replace("Kleinberg", "").replace("ScaleFree", ""));
		int ttl = (int)Math.ceil(Math.pow(Math.log(nodes)/Math.log(2),2));
		String[] sub3 = (new File("data/LALists/" +folder)).list();
		for (int i3 = 0; i3 < sub3.length; i3++){
			String[] sub4 = (new File("data/LALists/" +folder + sub3[i3])).list();
			for (int i4 = 0; i4 < sub4.length; i4++){
				String list = sub4[i4].split("--")[0];
				double g = Double.parseDouble(list.replace("Devi", "").replace("Pair", ""));
				g = Math.log(1/g)/Math.log(2)*g;
				Metric[] m = new Metric[]{new Routing(new LookaheadGreedy(ttl, ViaSelection.sequential, 0, false, false)),
						new Routing(new LookaheadGreedy(ttl, ViaSelection.sequential, g, false, false)),
						new Routing(new LookaheadGreedy(ttl, ViaSelection.sequential, 0, true, false)),
						new Routing(new LookaheadGreedy(ttl, ViaSelection.sequential, g, true, false)),
						new Routing(new LookaheadGDFS(ttl, ViaSelection.sequential, 0, false, false)),
						new Routing(new LookaheadGDFS(ttl, ViaSelection.sequential, g, false, false)),
						new Routing(new LookaheadGDFS(ttl, ViaSelection.sequential, 0, true, false)),
						new Routing(new LookaheadGDFS(ttl, ViaSelection.sequential, g, true, false)),
						new Routing(new LookaheadGreedy(ttl, ViaSelection.sequential, 0, false, true)),
						new Routing(new LookaheadGreedy(ttl, ViaSelection.sequential, g, false, true)),
						new Routing(new LookaheadGreedy(ttl, ViaSelection.sequential, 0, true, true)),
						new Routing(new LookaheadGreedy(ttl, ViaSelection.sequential, g, true, true)),
						new Routing(new LookaheadGDFS(ttl, ViaSelection.sequential, 0, false, true)),
						new Routing(new LookaheadGDFS(ttl, ViaSelection.sequential, g, false, true)),
						new Routing(new LookaheadGDFS(ttl, ViaSelection.sequential, 0, true, true)),
						new Routing(new LookaheadGDFS(ttl, ViaSelection.sequential, g, true, true)),
						new Routing(new Greedy(ttl)),
						new Routing(new DepthFirstGreedy(ttl))};
				performMetric("LALists/"+folder+sub3[i3]+ "/"+sub4[i4]+ "/", m,
						"Routing/"+folder+sub3[i3]+ "/"+sub4[i4]+ "/", name+list, 30);
			}
		}
	}
	
	public static void makeGraphsKleinberg(int nodes, int times, String folder){ 
	Config.overwrite("MAIN_DATA_FOLDER", "data/"+folder + "original/");
	double f = Math.sqrt(2*Math.PI)/4.0;
	
	 Config.overwrite("SERIES_GRAPH_WRITE", "true");
	 	Network nw = new DescriptionWrapper(new Kleinberg1D(nodes,1,1,1,true,true, null), "Kleinberg"+nodes );
	Series.generate(nw, new Metric[0], times);
	
	String[] graphs = new String[times];
	for (int j = 0; j < times; j++){
		graphs[j] = "data/"+ folder + "original/"+ nw.getFolderName()+"/"+j+"/graph.txt";
	}
	
	Config.overwrite("MAIN_DATA_FOLDER", "data/"+folder + "random/");
	Network randomUni = new DescriptionWrapper(new ReadableList(nw.getFolderName(), nw.getFolderName(), 
			graphs,new Transformation[]{new RemoveGraphProperty(), new RandomRingIDSpaceSimple()}), "Kleinberg"+nodes +"Random");
	Series s =Series.generate(randomUni, new Metric[0], times);
	
	
	Config.overwrite("MAIN_DATA_FOLDER", "data/"+folder + "swapping/");
	Network swappingUni = new DescriptionWrapper(new ReadableList(nw.getFolderName(), nw.getFolderName(), 
			graphs,new Transformation[]{new RemoveGraphProperty(), new RandomRingIDSpaceSimple(),
		new Swapping(1000)}), "Kleinberg"+nodes +"Swapping");
	s =Series.generate(swappingUni, new Metric[0], times);
	
	
	
	Config.overwrite("MAIN_DATA_FOLDER", "data/"+folder + "lmc/");
	Network lmcUni = new DescriptionWrapper(new ReadableList(nw.getFolderName(), nw.getFolderName(), 
			graphs,new Transformation[]{new RemoveGraphProperty(), new RandomRingIDSpaceSimple(),
		new LMC(1000, LMC.MODE_UNRESTRICTED, 0, LMC.DELTA_1_N, 0)}), "Kleinberg"+nodes +"LMC");
	s =Series.generate(lmcUni, new Metric[0], times);
	
	}
	
	public static void makeGraphsScalefree(int nodes, int times, String folder, int C, double alpha){ 
		Config.overwrite("MAIN_DATA_FOLDER", "data/"+folder + "original/");
		double f = Math.sqrt(2*Math.PI)/4.0;
		
		 Config.overwrite("SERIES_GRAPH_WRITE", "true");
		 	Network nw = new DescriptionWrapper(new ScaleFreeUndirected(nodes,alpha,C,nodes, null), "ScaleFree"+nodes );
		Series.generate(nw, new Metric[0], times);
		
		String[] graphs = new String[times];
		for (int j = 0; j < times; j++){
			graphs[j] = "data/"+ folder + "original/"+ nw.getFolderName()+"/"+j+"/graph.txt";
		}
		
		Config.overwrite("MAIN_DATA_FOLDER", "data/"+folder + "random/");
		Network randomUni = new DescriptionWrapper(new ReadableList(nw.getFolderName(), nw.getFolderName(), 
				graphs,new Transformation[]{new RemoveGraphProperty(), new RandomRingIDSpaceSimple()}), "ScaleFree"+nodes +"Random");
		Series s =Series.generate(randomUni, new Metric[0], times);
		
		
		Config.overwrite("MAIN_DATA_FOLDER", "data/"+folder + "swapping/");
		Network swappingUni = new DescriptionWrapper(new ReadableList(nw.getFolderName(), nw.getFolderName(), 
				graphs,new Transformation[]{new RemoveGraphProperty(), new RandomRingIDSpaceSimple(),
			new Swapping(1000)}), "ScaleFree"+nodes +"Swapping");
		s =Series.generate(swappingUni, new Metric[0], times);
		
		
		
		Config.overwrite("MAIN_DATA_FOLDER", "data/"+folder + "lmc/");
		Network lmcUni = new DescriptionWrapper(new ReadableList(nw.getFolderName(), nw.getFolderName(), 
				graphs,new Transformation[]{new RemoveGraphProperty(),new RandomRingIDSpaceSimple(),
			new LMC(1000, LMC.MODE_UNRESTRICTED, 0, LMC.DELTA_1_N, 0)}), "ScaleFree"+nodes +"LMC");
		s =Series.generate(lmcUni, new Metric[0], times);
		
		}
	
	//public static void all
	
	public static void performMetric(String folder, Metric[] metric, String resultFolder, String name,
			int times){
		Config.overwrite("SERIES_GRAPH_WRITE", "false"); 
		String[] graphs = new String[times];
		for (int j = 0; j < times; j++){
			graphs[j] = "data/"+ folder  +j+"/graph.txt";
		}
		Config.overwrite("MAIN_DATA_FOLDER", "data/"+resultFolder);
		
		Network net = new DescriptionWrapper(new ReadableList(name, name, 
				graphs,null), name);
		Series s = Series.generate(net, metric, times);
		Plotting.multi(s,metric, "multi/"+resultFolder);
	}
	
	public static void performMetricNoPlot(String folder, Metric[] metric, String resultFolder, String name,
			int times){
		Config.overwrite("SERIES_GRAPH_WRITE", "false"); 
		String[] graphs = new String[times];
		for (int j = 0; j < times; j++){
			graphs[j] = "data/"+ folder  +j+"/graph.txt";
		}
		Config.overwrite("MAIN_DATA_FOLDER", "data/"+resultFolder);
		
		Network net = new DescriptionWrapper(new ReadableList(name, name, 
				graphs,null), name);
		Series s = Series.generate(net, metric, times);
		
	}
	
	public static void writeLookahead(String folder, String resultFolder, String name,
			int times, Transformation lookaheadList){
		Config.overwrite("SERIES_GRAPH_WRITE", "true"); 
		String[] graphs = new String[times];
		String[] subs = (new File("data/"+ folder)).list();
		for (int j = 0; j < times; j++){
			graphs[j] = "data/"+ folder + subs[0] + "/" +j+"/graph.txt";
		}
		Config.overwrite("MAIN_DATA_FOLDER", "data/"+resultFolder);
		
		Network net = new DescriptionWrapper(new ReadableList(name, name, 
				graphs,new Transformation[]{lookaheadList}), name);
		Series s = Series.generate(net, new Metric[0], times);
	}
	
	public static void allPlots(){
		String path = "plots/multi/Routing/Kleinberg1000/";
		String[] sub = (new File(path)).list();
		for (int i = 0; i < sub.length; i++){
			String[] sub2 = (new File(path+sub[i])).list();
			String[] sub3 = (new File(path+sub[i]+"/"+sub2[0])).list();
			for (int j = 0; j < sub3.length; j++){
				String[] sub4 = (new File(path+sub[i]+"/"+sub2[0]+ "/"+sub3[j])).list();
				for (int k = 0; k < sub4.length; k++){
//				File source=new File(path+sub[i]+"/"+sub2[0]+ "/" +sub3[j] + "/"+sub4[k] + "/r-hopDistributionAbsolute-cdf.pdf");
//				File destination=new File("/plots/Kleinberg1000/"+sub[i]+"/"+sub3[j]+"-"+sub4[k] + ".pdf");
//				FileUtil.copy(path+sub[i]+"/"+sub2[0]+ "/" +sub3[j] + "/"+sub4[k] + "/r-hopDistributionAbsolute-cdf.pdf", 
//						"/plots/Kleinberg1000/"+sub[i]+"/"+sub3[j]+"-"+sub4[k] + ".pdf");  
					try {
						Process p = Runtime.getRuntime().exec("cp " + path+sub[i]+"/"+sub2[0]+ "/" +sub3[j] + "/"+sub4[k] + 
								"/r-hopDistributionAbsolute-cdf.png   plots/Kleinberg1000/"+sub[i]+"/"+sub3[j]+"-"+sub4[k]  +
										".png" , new String[] {"PATH=/usr/local/bin"});
						InputStream stderr = p.getErrorStream();
						InputStreamReader isr = new InputStreamReader(stderr);
						BufferedReader br = new BufferedReader(isr);
						String line = null;
						while ((line = br.readLine()) != null) {
							System.out.println(line);
						}
			            p.waitFor();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}
	}

}
