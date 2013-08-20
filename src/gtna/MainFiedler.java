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
 * MainFiedler.java
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
import gtna.graph.sorting.DegreeNodeSorter;
import gtna.graph.sorting.FiedlerNodeSorter;
import gtna.graph.sorting.NodeSorter;
import gtna.graph.sorting.NodeSorter.NodeSorterMode;
import gtna.graph.sorting.RandomNodeSorter;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.fragmentation.Fragmentation;
import gtna.metrics.fragmentation.WeakFragmentation;
import gtna.metrics.fragmentation.WriteFiedler;
import gtna.networks.Network;
import gtna.networks.canonical.Complete;
import gtna.networks.etc.NetworkConnector;
import gtna.networks.model.BarabasiAlbert;
import gtna.networks.model.CondonAndKarp;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.util.ReadableFile;
import gtna.networks.util.ReadableList;
import gtna.plot.Plotting;
import gtna.transformation.Transformation;
import gtna.transformation.edges.Bidirectional;
import gtna.transformation.eigenvector.StoreFiedler;
import gtna.transformation.partition.LargestWeaklyConnectedComponent;
import gtna.util.Config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author stef
 *
 */
public class MainFiedler {
	public static boolean PLOT = true; 

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config.overwrite("GNUPLOT_TERMINAL", "png");
		Config.overwrite("PLOT_EXTENSION", ".png");
		
		Network net = new ReadableFile("KARATE", "KARATE", "../graphs/karate.txt", new Transformation[]
				{new StoreFiedler()});
		Series.generate(net, new Metric[0], 1);
		//int nodes, int degree, int groups, int edges, int times, String folder
//        CK();
//        BA(3000,5,10,"");
//        ER(3000,5,10,"");
//		BA(5000,5,6,"");
//        ER(5000,5,6,"");
//		BANC(3000,5,2,1,10, "BAC/");
//		BANC(3000,5,10,20,10, "BAC/");
//		BANC(5000,5,2,1,10, "BAC");
//		BANC(5000,5,10,20,10, "BAC");
//		CNC(3000,5,2,1,10, "CC/");
		
//		addFiedler("data/BARABASI_ALBERT-3000-5--LARGEST_WEAKLY_CONNECTED_COMPONENT--STOREFIEDLER/", 10);
//		addFiedler("data/BARABASI_ALBERT-5000-5--LARGEST_WEAKLY_CONNECTED_COMPONENT--STOREFIEDLER/", 6);
//		addFiedler("data/ERDOS_RENYI-3000-10.0-true--LARGEST_WEAKLY_CONNECTED_COMPONENT--STOREFIEDLER/", 10);
//		addFiedler("data/ERDOS_RENYI-5000-10.0-true--LARGEST_WEAKLY_CONNECTED_COMPONENT--STOREFIEDLER/", 6);
//		Config.overwrite("GNUPLOT_TERMINAL", "png");
//		Config.overwrite("PLOT_EXTENSION", ".png");
//		if (args[0].equals("CK")){
//			CK(Integer.parseInt(args[1]), Integer.parseInt(args[2])/2, Integer.parseInt(args[3]), 
//					Integer.parseInt(args[4]), Integer.parseInt(args[5]), args[6]);
//			
//		}
//		if (args[0].equals("BAC")){
//			BANC(Integer.parseInt(args[1]), Integer.parseInt(args[2])/2, Integer.parseInt(args[3]), 
//					Integer.parseInt(args[4]), Integer.parseInt(args[5]), args[6]);
//			
//		}
//		if (args[0].equals("CC")){
//			CNC(Integer.parseInt(args[1]), Integer.parseInt(args[2])/2, Integer.parseInt(args[3]), 
//					Integer.parseInt(args[4]), Integer.parseInt(args[5]), args[6]);
//			
//		}
//		if (args[0].equals("ERC")){
//			ERNC(Integer.parseInt(args[1]), Integer.parseInt(args[2])/2, Integer.parseInt(args[3]), 
//					Integer.parseInt(args[4]), Integer.parseInt(args[5]), args[6]);
//			
//		}
  
	}
	
	private static void CK(int nodes, int degree, int groups, int factor, int times, String folder){
		 double eout = (double)degree/(double)(1+factor);
		 double pout = eout/(double)(nodes - nodes/groups);
		 double pin = factor*eout/(double)(nodes/groups);
		 Config.overwrite("SERIES_GRAPH_WRITE", "true");
			Config.overwrite("MAIN_DATA_FOLDER", "data/"+folder);
			Config.overwrite("MAIN_PLOT_FOLDER", "plot/"+folder);
			Network net = new CondonAndKarp(nodes, groups, pin, pout,
					new Transformation[] { new LargestWeaklyConnectedComponent(),
					new Bidirectional(), new StoreFiedler() });
			Series.generate(net, new Metric[0], times);
			
			if (PLOT){
				Config.overwrite("SERIES_GRAPH_WRITE", "false");
			Metric[] m = {new WeakFragmentation(new FiedlerNodeSorter(
					FiedlerNodeSorter.Selection.SUM,
					NodeSorter.NodeSorterMode.DESC, 1),
					Fragmentation.Resolution.SINGLE),
					new WeakFragmentation(
							new DegreeNodeSorter(NodeSorterMode.DESC),
							Fragmentation.Resolution.SINGLE),
					new WeakFragmentation(new RandomNodeSorter(),
							Fragmentation.Resolution.SINGLE),
					new DegreeDistribution()		
					};
			

				String[] graphs = new String[times];
				for (int j = 0; j < times; j++){
					
					graphs[j] = "data/"+ folder+ net.getFolderName()+"/"+j+"/graph.txt";
					
					
				}
				try {
				Network net2 = new ReadableList(net.getFolderName(), net.getFolderName(), 
						graphs,null);
				Series s =Series.generate(net2, m, times);
				Plotting.multi(s, m, folder+"multi/"+net.getFolderName()+"/");
				//Plotting.multi(s, m, "single/" +files[i]+"/");
				} catch (Exception e){
					
				}
				
			
			Metric[] m2 = {new WriteFiedler()
			};

		for (int j = 0; j < times; j++){
			
			graphs[j] = "data/"+folder+ net.getFolderName()+"/"+j+"/graph.txt";
			try {
				Network net3 = new ReadableFile(net.getFolderName()+j, net.getFolderName()+"/"+ j+"/", 
						graphs[j],null);
				Series s =Series.generate(net3, m2, 1);
				Plotting.multi(s, m2, folder+"multi/"+net.getFolderName()+"/"+j + "/");
				//Plotting.multi(s, m, "single/" +files[i]+"/");
				} catch (Exception e){
					
				}
				
	          }
			}
		}
	
	private static void CK(){
		int times = 10;
		
			
			if (PLOT){
				Config.overwrite("SERIES_GRAPH_WRITE", "false");
			Metric[] m = {new WeakFragmentation(new FiedlerNodeSorter(
					FiedlerNodeSorter.Selection.SUM,
					NodeSorter.NodeSorterMode.DESC, 1),
					Fragmentation.Resolution.SINGLE),
					new WeakFragmentation(
							new DegreeNodeSorter(NodeSorterMode.DESC),
							Fragmentation.Resolution.SINGLE),
					new WeakFragmentation(new RandomNodeSorter(),
							Fragmentation.Resolution.SINGLE),
					new DegreeDistribution()		
					};
			
           String folder = "CK";
           String[] nets = (new File("data/")).list();
           for (int i = 0; i < nets.length; i++){
        	   if (nets[i].startsWith(folder)){
				String[] graphs = new String[times];
				for (int j = 0; j < times; j++){
					
					graphs[j] = "data/"+ nets[i]+"/"+j+"/graph.txt";
					
					
				}
				try {
				Network net2 = new ReadableList(nets[i], nets[i], 
						graphs,null);
				Series s =Series.generate(net2, m, times);
				Plotting.multi(s, m, folder+"multi/"+nets[i]+"/");
				//Plotting.multi(s, m, "single/" +files[i]+"/");
				} catch (Exception e){
					
				}
				
			
			Metric[] m2 = {new WriteFiedler()
			};

		for (int j = 0; j < times; j++){
			
			graphs[j] = "data/"+nets[i]+"/"+j+"/graph.txt";
			try {
				Network net3 = new ReadableFile(nets[i]+j, nets[i]+"/"+ j+"/", 
						graphs[j],null);
				Series s =Series.generate(net3, m2, 1);
				Plotting.multi(s, m2, folder+"multi/"+nets[i]+"/"+j + "/");
				//Plotting.multi(s, m, "single/" +files[i]+"/");
				} catch (Exception e){
					
				}
				
	          }
        	   }
			}
			}  
		}
	
	private static void BA(int nodes, int degree, int times, String folder){
	Config.overwrite("SERIES_GRAPH_WRITE", "true");
		Config.overwrite("MAIN_DATA_FOLDER", "data/"+folder);
		Config.overwrite("MAIN_PLOT_FOLDER", "plot/"+folder);
		Network net = new BarabasiAlbert(nodes, degree,
				new Transformation[] { new LargestWeaklyConnectedComponent(),
				new StoreFiedler() });
		Series.generate(net, new Metric[0], times);
		
		if (PLOT){
			Config.overwrite("SERIES_GRAPH_WRITE", "false");
		Metric[] m = {new WeakFragmentation(new FiedlerNodeSorter(
				FiedlerNodeSorter.Selection.SUM,
				NodeSorter.NodeSorterMode.DESC, 1),
				Fragmentation.Resolution.SINGLE),
				new WeakFragmentation(
						new DegreeNodeSorter(NodeSorterMode.DESC),
						Fragmentation.Resolution.SINGLE),
				new WeakFragmentation(new RandomNodeSorter(),
						Fragmentation.Resolution.SINGLE),
				new DegreeDistribution()		
				};
		

			String[] graphs = new String[times];
			for (int j = 0; j < times; j++){
				
				graphs[j] = "data/"+ folder+ net.getFolderName()+"/"+j+"/graph.txt";
				
				
			}
			try {
			Network net2 = new ReadableList(net.getFolderName(), net.getFolderName(), 
					graphs,null);
			Series s =Series.generate(net2, m, times);
			Plotting.multi(s, m, folder+"multi/"+net.getFolderName()+"/");
			//Plotting.multi(s, m, "single/" +files[i]+"/");
			} catch (Exception e){
				
			}
			
		
		Metric[] m2 = {new WriteFiedler()
		};

	for (int j = 0; j < times; j++){
		
		graphs[j] = "data/"+folder+ net.getFolderName()+"/"+j+"/graph.txt";
		try {
			Network net3 = new ReadableFile(net.getFolderName()+j, net.getFolderName()+"/"+ j+"/", 
					graphs[j],null);
			Series s =Series.generate(net3, m2, 1);
			Plotting.multi(s, m2, folder+"multi/"+net.getFolderName()+"/"+j + "/");
			//Plotting.multi(s, m, "single/" +files[i]+"/");
			} catch (Exception e){
				
			}
			
          }
		}
	}
	
	private static void ER(int nodes, int degree, int times, String folder){
		Config.overwrite("SERIES_GRAPH_WRITE", "true");
			Config.overwrite("MAIN_DATA_FOLDER", "data/"+folder);
			Config.overwrite("MAIN_PLOT_FOLDER", "plot/"+folder);
			Network net = new ErdosRenyi(nodes, degree*2, true,
					new Transformation[] { new LargestWeaklyConnectedComponent(),
					new StoreFiedler() });
			Series.generate(net, new Metric[0], times);
			
			if (PLOT){
				Config.overwrite("SERIES_GRAPH_WRITE", "false");
			Metric[] m = {new WeakFragmentation(new FiedlerNodeSorter(
					FiedlerNodeSorter.Selection.SUM,
					NodeSorter.NodeSorterMode.DESC, 1),
					Fragmentation.Resolution.SINGLE),
					new WeakFragmentation(
							new DegreeNodeSorter(NodeSorterMode.DESC),
							Fragmentation.Resolution.SINGLE),
					new WeakFragmentation(new RandomNodeSorter(),
							Fragmentation.Resolution.SINGLE),
					new DegreeDistribution()		
					};
			

				String[] graphs = new String[times];
				for (int j = 0; j < times; j++){
					
					graphs[j] = "data/"+ folder+ net.getFolderName()+"/"+j+"/graph.txt";
					
					
				}
				try {
				Network net2 = new ReadableList(net.getFolderName(), net.getFolderName(), 
						graphs,null);
				Series s =Series.generate(net2, m, times);
				Plotting.multi(s, m, folder+"multi/"+net.getFolderName()+"/");
				//Plotting.multi(s, m, "single/" +files[i]+"/");
				} catch (Exception e){
					
				}
				
			
			Metric[] m2 = {new WriteFiedler()
			};

		for (int j = 0; j < times; j++){
			
			graphs[j] = "data/"+folder+ net.getFolderName()+"/"+j+"/graph.txt";
			try {
				Network net3 = new ReadableFile(net.getFolderName()+j, net.getFolderName()+"/"+ j+"/", 
						graphs[j],null);
				Series s =Series.generate(net3, m2, 1);
				Plotting.multi(s, m2, folder+"multi/"+net.getFolderName()+"/"+j + "/");
				//Plotting.multi(s, m, "single/" +files[i]+"/");
				} catch (Exception e){
					
				}
				
	          }
			}
		}
	
	private static void BANC(int nodes, int degree, int groups, int edges, int times, String folder){
		    Config.overwrite("SERIES_GRAPH_WRITE", "true");
			Config.overwrite("MAIN_DATA_FOLDER", "data/"+folder);
			Config.overwrite("MAIN_PLOT_FOLDER", "plot/"+folder);
			Network[] nws = new Network[groups];
			for (int i = 0; i < groups; i++){
				nws[i] = new BarabasiAlbert(nodes/groups, degree - edges/(nodes/groups),null);
			}
			Network net = new NetworkConnector(nws, edges, false,
					new Transformation[] {  new LargestWeaklyConnectedComponent(), new StoreFiedler() });
			Series.generate(net, new Metric[0], times);
			
			if (PLOT){
				Config.overwrite("SERIES_GRAPH_WRITE", "false");
			Metric[] m = {new WeakFragmentation(new FiedlerNodeSorter(
					FiedlerNodeSorter.Selection.SUM,
					NodeSorter.NodeSorterMode.DESC, 1),
					Fragmentation.Resolution.SINGLE),
					new WeakFragmentation(
							new DegreeNodeSorter(NodeSorterMode.DESC),
							Fragmentation.Resolution.SINGLE),
					new WeakFragmentation(new RandomNodeSorter(),
							Fragmentation.Resolution.SINGLE),
					new DegreeDistribution()		
					};
			

				String[] graphs = new String[times];
				for (int j = 0; j < times; j++){
					
					graphs[j] = "data/"+ folder+ net.getFolderName()+"/"+j+"/graph.txt";
					
					
				}
				try {
				Network net2 = new ReadableList(net.getFolderName(), net.getFolderName(), 
						graphs,null);
				Series s =Series.generate(net2, m, times);
				Plotting.multi(s, m, folder+"multi/"+net.getFolderName()+"/");
				//Plotting.multi(s, m, "single/" +files[i]+"/");
				} catch (Exception e){
					
				}
				
			
			Metric[] m2 = {new WriteFiedler()
			};

		for (int j = 0; j < times; j++){
			
			graphs[j] = "data/"+folder+ net.getFolderName()+"/"+j+"/graph.txt";
			try {
				Network net3 = new ReadableFile(net.getFolderName()+j, net.getFolderName()+"/"+ j+"/", 
						graphs[j],null);
				Series s =Series.generate(net3, m2, 1);
				Plotting.multi(s, m2, folder+"multi/"+net.getFolderName()+"/"+j + "/");
				//Plotting.multi(s, m, "single/" +files[i]+"/");
				} catch (Exception e){
					
				}
				
	          }
			}
		}
	
	private static void CNC(int nodes, int degree, int groups, int edges, int times, String folder){
	    Config.overwrite("SERIES_GRAPH_WRITE", "true");
		Config.overwrite("MAIN_DATA_FOLDER", "data/"+folder);
		Config.overwrite("MAIN_PLOT_FOLDER", "plot/"+folder);
		Network[] nws = new Network[groups];
		for (int i = 0; i < groups; i++){
			nws[i] = new Complete(nodes/groups,null);
		}
		Network net = new NetworkConnector(nws, edges, false,
				new Transformation[] {  new LargestWeaklyConnectedComponent(), new StoreFiedler() });
		Series.generate(net, new Metric[0], times);
		
		if (PLOT){
			Config.overwrite("SERIES_GRAPH_WRITE", "false");
		Metric[] m = {new WeakFragmentation(new FiedlerNodeSorter(
				FiedlerNodeSorter.Selection.SUM,
				NodeSorter.NodeSorterMode.DESC, 1),
				Fragmentation.Resolution.SINGLE),
				new WeakFragmentation(
						new DegreeNodeSorter(NodeSorterMode.DESC),
						Fragmentation.Resolution.SINGLE),
				new WeakFragmentation(new RandomNodeSorter(),
						Fragmentation.Resolution.SINGLE),
				new DegreeDistribution()		
				};
		

			String[] graphs = new String[times];
			for (int j = 0; j < times; j++){
				
				graphs[j] = "data/"+ folder+ net.getFolderName()+"/"+j+"/graph.txt";
				
				
			}
			try {
			Network net2 = new ReadableList(net.getFolderName(), net.getFolderName(), 
					graphs,null);
			Series s =Series.generate(net2, m, times);
			Plotting.multi(s, m, folder+"multi/"+net.getFolderName()+"/");
			//Plotting.multi(s, m, "single/" +files[i]+"/");
			} catch (Exception e){
				
			}
			
		
		Metric[] m2 = {new WriteFiedler()
		};

	for (int j = 0; j < times; j++){
		
		graphs[j] = "data/"+folder+ net.getFolderName()+"/"+j+"/graph.txt";
		try {
			Network net3 = new ReadableFile(net.getFolderName()+j, net.getFolderName()+"/"+ j+"/", 
					graphs[j],null);
			Series s =Series.generate(net3, m2, 1);
			Plotting.multi(s, m2, folder+"multi/"+net.getFolderName()+"/"+j + "/");
			//Plotting.multi(s, m, "single/" +files[i]+"/");
			} catch (Exception e){
				
			}
			
          }
		}
	}
	
	private static void ERNC(int nodes, int degree, int groups, int edges, int times, String folder){
	    Config.overwrite("SERIES_GRAPH_WRITE", "true");
		Config.overwrite("MAIN_DATA_FOLDER", "data/"+folder);
		Config.overwrite("MAIN_PLOT_FOLDER", "plot/"+folder);
		Network[] nws = new Network[groups];
		for (int i = 0; i < groups; i++){
			nws[i] = new ErdosRenyi(nodes/groups, degree,true, null);
		}
		Network net = new NetworkConnector(nws, edges, false,
				new Transformation[] {  new LargestWeaklyConnectedComponent(), new StoreFiedler() });
		Series.generate(net, new Metric[0], times);
		
		if (PLOT){
			Config.overwrite("SERIES_GRAPH_WRITE", "false");
		Metric[] m = {new WeakFragmentation(new FiedlerNodeSorter(
				FiedlerNodeSorter.Selection.SUM,
				NodeSorter.NodeSorterMode.DESC, 1),
				Fragmentation.Resolution.SINGLE),
				new WeakFragmentation(
						new DegreeNodeSorter(NodeSorterMode.DESC),
						Fragmentation.Resolution.SINGLE),
				new WeakFragmentation(new RandomNodeSorter(),
						Fragmentation.Resolution.SINGLE),
				new DegreeDistribution()		
				};
		

			String[] graphs = new String[times];
			for (int j = 0; j < times; j++){
				
				graphs[j] = "data/"+ folder+ net.getFolderName()+"/"+j+"/graph.txt";
				
				
			}
			try {
			Network net2 = new ReadableList(net.getFolderName(), net.getFolderName(), 
					graphs,null);
			Series s =Series.generate(net2, m, times);
			Plotting.multi(s, m, folder+"multi/"+net.getFolderName()+"/");
			//Plotting.multi(s, m, "single/" +files[i]+"/");
			} catch (Exception e){
				
			}
			
		
		Metric[] m2 = {new WriteFiedler()
		};

	for (int j = 0; j < times; j++){
		
		graphs[j] = "data/"+folder+ net.getFolderName()+"/"+j+"/graph.txt";
		try {
			Network net3 = new ReadableFile(net.getFolderName()+j, net.getFolderName()+"/"+ j+"/", 
					graphs[j],null);
			Series s =Series.generate(net3, m2, 1);
			Plotting.multi(s, m2, folder+"multi/"+net.getFolderName()+"/"+j + "/");
			//Plotting.multi(s, m, "single/" +files[i]+"/");
			} catch (Exception e){
				
			}
			
          }
		}
	}
	
	private static void smallTest(){
		Config.overwrite("SERIES_GRAPH_WRITE", "true");
		Config.overwrite("MAIN_DATA_FOLDER", "data/test-fiedler/");
		Network net = new Complete(7, new Transformation[]{new StoreFiedler()});
		Series.generate(net, new Metric[0], 2);
		
		Metric[] m = {new WeakFragmentation(new FiedlerNodeSorter(
				FiedlerNodeSorter.Selection.SUM,
				NodeSorter.NodeSorterMode.DESC, 1),
				Fragmentation.Resolution.SINGLE),
				new WeakFragmentation(
						new DegreeNodeSorter(NodeSorterMode.DESC),
						Fragmentation.Resolution.SINGLE),
				new WeakFragmentation(new RandomNodeSorter(),
						Fragmentation.Resolution.SINGLE) 
				};
		
		String[] files = (new File("data/test-fiedler/")).list();
		for (int i = 0; i < files.length; i++){
			String[] graphs = new String[2];
			for (int j = 0; j < 2; j++){
				
				graphs[j] = "data/test-fiedler/"+ files[i]+"/"+j+"/graph.txt";
				
				
			}
			try {
			Network net2 = new ReadableList(files[i], files[i], 
					graphs,null);
			Series s =Series.generate(net2, m, 2);
			Plotting.multi(s, m, "multi/"+files[i]+"/");
			//Plotting.multi(s, m, "single/" +files[i]+"/");
			} catch (Exception e){
				
			}
			
		}
		
		Metric[] m2 = {new DegreeDistribution()
		};

for (int i = 0; i < files.length; i++){
	//if (!files[i].contains("CONDON")){
	String[] graphs = new String[2];
	for (int j = 0; j < 2; j++){
		
		graphs[j] = "data/test-fiedler/"+ files[i]+"/"+j+"/graph.txt";
		try {
			Network net3 = new ReadableFile(files[i]+j, files[i]+"/"+ j+"/", 
					graphs[j],null);
			Series s =Series.generate(net3, m2, 1);
			Plotting.multi(s, m2, "multi/"+files[i]+"/"+j + "/");
			//Plotting.multi(s, m, "single/" +files[i]+"/");
			} catch (Exception e){
				
			}
		
	}
	
	//}
}
	}
	
	public static void addFiedler(String folder, int times){
		for (int i = 0; i < times; i++){
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(folder + i + "/graph.txt_FIEDLER_VECTOR_0"));
			bw.write("# Graph Property Class");
			bw.newLine();
			bw.write("gtna.graph.sorting.FiedlerVector");
			bw.newLine();
			bw.write("# Key");
			bw.newLine();
			bw.write("FIEDLER_VECTOR_0");
			bw.newLine();
			
			BufferedReader br = new BufferedReader(new FileReader(folder + i + "/graph.txt_FIEDLER_VECTOR_0_key"));
			String line;
			while ((line = br.readLine()) != null){
				bw.newLine();
				bw.write(line.replace("	", ":"));
			}
			bw.flush();
			bw.close();
			br.close();
			(new File(folder + i + "/graph.txt_FIEDLER_VECTOR_0_key")).delete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		}		
	}

}
