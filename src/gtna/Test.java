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
 * Test.java
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
import gtna.networks.Network;
import gtna.networks.canonical.Ring;
import gtna.networks.model.BarabasiAlbert;
import gtna.networks.model.randomGraphs.ScaleFreeRandomGraph;
import gtna.networks.model.smallWorld.Kleinberg;
import gtna.networks.model.smallWorld.Kleinberg1D;
import gtna.networks.model.smallWorld.Kleinberg1DC;
import gtna.networks.model.smallWorld.ScaleFreeUndirected;
import gtna.networks.util.ReadableFile;
import gtna.plot.Plot;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.greedyVariations.BacktrackGreedy;
import gtna.routing.greedyVariations.DepthFirstEdgeGreedy;
import gtna.routing.greedyVariations.DepthFirstGreedy;
import gtna.routing.greedyVariations.DistRestrictEdgeGreedy;
import gtna.routing.greedyVariations.DistRestrictGreedy;
import gtna.routing.greedyVariations.FactorRestrictEdgeGreedy;
import gtna.routing.greedyVariations.FactorRestrictGreedy;
import gtna.routing.greedyVariations.GravityPressureRouting;
import gtna.routing.greedyVariations.KWorseGreedy;
import gtna.routing.greedyVariations.OneWorseGreedy;
import gtna.routing.greedyVariations.PureGreedy;
import gtna.transformation.Transformation;
import gtna.transformation.attackableEmbedding.ADHT.ADHT;
import gtna.transformation.attackableEmbedding.powertwo.PowerTwoVar;
import gtna.transformation.failure.node.LargestFailure;
import gtna.transformation.failure.node.RandomFailure;
import gtna.transformation.id.RandomRingIDSpaceSimple;
import gtna.util.Config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author stef
 *
 */
public class Test {
	
	public static void main(String[] args){
		//testingADHT();
		double[] outs = {2,2.5,3,3.5,4,4.5,5,100};
		for (int i = 0; i < outs.length; i++){
		   testingADHT(1000, outs[i], 13,18, ADHT.NODE_TYPE_OUTLIER,20);
		}
//		testingP2(1000,new double[] {0.5,1},13,18, PowerTwoVar.NODE_TYPE_CEN,10);
		//Config.overwrite("MAIN_DATA_FOLDER", "ADHT2");
//		Config.overwrite("METRICS", "DEGREE_DISTRIBUTION, ROUTING, ID_DISTRIBUTION, SUCC_DISTRIBUTION");
//		int[] max = {14,15,16,17,18,Integer.MAX_VALUE};
//		int[] iter ={10,100,200,500,1000,2000,6000};
//		for (int i = max.length-1; i > -1; i--){
//			for (int j = 0; j < iter.length; j++){
//		Transformation[][] adht = {new Transformation[]{new RandomRingIDSpaceSimple(), new ADHT(iter[j],ADHT.NODE_TYPE_DIVERSE,13,max[i])}};
//		RoutingAlgorithm ra =  new DepthFirstGreedy(174);
//		for (int k = 0; k < adht.length; k++){
//		Network net = new ReadableFile("SPI", "SPI", "graphs/spi.txt",ra,adht[k]);
//		Series ser = Series.generate(net, 25);
//		//Plot.allMulti(ser, "multiDi");
//		}
//		}
//		}
//		int[] succValues = {5,6,7,8,9,10,11,12};
//		for (int i = 0; i < succValues.length; i++){
//		Transformation[][] adht = {new Transformation[]{new RandomRingIDSpaceSimple(), new ADHT(1000,ADHT.NODE_TYPE_SUCC, succValues[i])},
//				new Transformation[]{new RandomRingIDSpaceSimple(), new ADHT(1000,ADHT.NODE_TYPE_DEF, succValues[i])}};
//		RoutingAlgorithm ra =  new DepthFirstGreedy(174);
//		for (int j = 0; j < adht.length; j++){
//		Network net = new ReadableFile("SPI", "SPI", "graphs/spi.txt",ra,adht[j]);
//		Series.generate(net, 10);
//		}
//		}
//	Config.overwrite("METRICS", "DEGREE_DISTRIBUTION, ROUTING");
//	int[] iter = {10,100,200,500,1000,2000,6000};
//	for (int i = 0; i < iter.length; i++){
//	Transformation[] adht = {new RandomRingIDSpaceSimple(), new ADHT(iter[i],ADHT.NODE_TYPE_SUCC)};
//	RoutingAlgorithm ra =  new DepthFirstGreedy(174);
//	Network net = new ReadableFile("SPI", "SPI", "graphs/spi.txt",ra,adht);
//	Series.generate(net, 20);
//	
//	Transformation[] adht2 = {new RandomRingIDSpaceSimple(), new ADHT(iter[i],ADHT.NODE_TYPE_DEF)};
//	Network net2 = new ReadableFile("SPI", "SPI", "graphs/spi.txt",ra,adht2);
//	Series.generate(net2, 20);
//	}
//	try{
//	BufferedWriter bw = new BufferedWriter(new FileWriter("table.txt"));
// int[] k = {1,2,3,4,5};
//	for (int i = 0; i < k.length; i++){
//		Transformation[][] t = {new Transformation[]{new MaxMinBound(k[i],2*k[i])}, 
//				new Transformation[]{new MaxMinBound(0,2*k[i])}, new Transformation[]{new MaxMinBound(k[i],Integer.MAX_VALUE)}};
//		int[] min = {k[i],0,k[i]};
//		int[] max = {2*k[i],2*k[i],Integer.MAX_VALUE};
//		for (int j = 0; j < t.length; j++){
//		 Network net = new ReadableFile("SPI", "SPI", "graphs/spi.txt", null,t[j]);
//		 //Series.generate(net, 5);
//		 double[] res = getSizes("data/9222/"+net.folder()+ "_average.txt");
//		 bw.write(min[j] + " & " + max[j] + " & "  + res[0] + " & " + res[1] + "\\\\");
//		 bw.newLine();
//		}
//	}
//	bw.flush();
//	bw.close();
//	} catch (IOException e){
//		e.printStackTrace();
//	}
//	try{
//		BufferedWriter bw = new BufferedWriter(new FileWriter("table.txt"));
//		int[] k = {1,2,3,4,5};
//	double[] alpha = {2.2,2.3,2.4};
//	int[] size = {5000,10000,20000,50000};
//	int[] minDeg = {1,2,3,4,5};
//	for (int b = 0; b < size.length; b++){
//	for (int i = 0; i < k.length; i++){
//		for (int a = 0; a < alpha.length; a++){
//			
//				for (int c = 0; c < minDeg.length; c++){
//					Transformation[][] t = {new Transformation[]{new MaxMinBound(k[i],2*k[i])}, 
//							new Transformation[]{new MaxMinBound(0,2*k[i])}, new Transformation[]{new MaxMinBound(k[i],Integer.MAX_VALUE)}};
//					for (int j = 0; j < t.length; j++){
//					 Network net = new ScaleFreeRandomGraph(size[b],alpha[a],minDeg[c],size[b],null,t[j]);
//					 Series.generate(net, 5);
//					}
//				}
//			}
//		}
//	}
//		bw.flush();
//		bw.close();
//		} catch (IOException e){
//			e.printStackTrace();
//		}
	
	//Config.overwrite("METRICS", "DEGREE_DISTRIBUTION, CLUSTERING_COEFFICIENT");	
	//testRandomDeg();
//	for (int i=4; i < 6; i++){
//		testFailures(i*20000,30,2);
//	}
//	ScaleFreeRandomGraph net = new ScaleFreeRandomGraph(10000,2.5,1,10000, null,null);
//	Series.generate(net, 5);
//	String[] kadGraphs = ((new File("KAD/"))).list();
//	for (int i = 0; i < kadGraphs.length; i++){
//		 ReadableFile net = new ReadableFile(kadGraphs[i], kadGraphs[i], "KAD/"+kadGraphs[i],null,null );
//       		Series s = Series.generate(net, 100);
//       		Plot.allMulti(s, "KAD");
//	}
//	int size = Integer.parseInt(args[0]);
//	int C = Integer.parseInt(args[1]);
//	int iter = Integer.parseInt(args[2]);
//	testKleinberg1CRouting(size, C,iter);
//		int[] sizes = {100000};
//		int[] C = {10};
//		for (int i = 0; i < sizes.length; i++){
//			for (int j = 0; j < C.length; j++){
//				testKleinberg1C(sizes[i], C[j],1);
//			}
//		}
	
		
	}
	
	private static void testEmbedding(int iter){
//		Transformation[] t = {new Swapping(1000), new Swapping(1000,0.001, Swapping.ATTACK_KLEINBERG, Swapping.ATTACKER_SELECTION_LARGEST, 1)};
//		Network barabasi = new BarabasiAlbert(200,2,null,t);
//		Series.generate(barabasi, 1);
		
		
		//Transformation[] adht = {new RandomRingIDSpaceSimple(), new ADHT(iter,1.0,ADHT.NODE_TYPE_SIMPLE)};
//		RoutingAlgorithm ra =  new DepthFirstGreedy(174);
//		Network net = new ReadableFile("SPI", "SPI", "graphs/spi.txt",ra,adht);
//		Series.generate(net, 20);
	}
	
	private static void testRouting(){
		RoutingAlgorithm[] ra = {new PureGreedy(50), new BacktrackGreedy(50), new DepthFirstEdgeGreedy(50), new DepthFirstGreedy(50), new DistRestrictEdgeGreedy(50),
				new DistRestrictGreedy(50), new FactorRestrictEdgeGreedy(50), new FactorRestrictGreedy(50), new GravityPressureRouting(50), new OneWorseGreedy(50)};
		for (int i = 0; i < ra.length; i++){
			Network barabasi = new BarabasiAlbert(200,2,ra[i],null);
			Series.generate(barabasi, 1);
		}
	}
	
	private static void testKWorse(int size, int C, int iter){
		RoutingAlgorithm ra = new KWorseGreedy(2);
		Network kleinbergC = new Kleinberg1DC(size,1,1,1,true,true,C,ra,null);
		Series.generate(kleinbergC, iter);
	}

	private static void testSmallWorld(){
		Network kleinberg = new Kleinberg(10,3,1,1,3,true,true,null,null);
		Series.generate(kleinberg, 1);
		Network kleinberg1 = new Kleinberg1D(10,1,1,1,true,true,null,null);
		Series.generate(kleinberg1, 1);
		Network kleinbergC = new Kleinberg1DC(10,1,1,3,true,true,2,null,null);
		Series.generate(kleinbergC, 1);
		Network scalefree = new ScaleFreeUndirected(10,3,1,10,null,null);
		Series.generate(scalefree, 1);
	}
	
	private static void testKleinberg1C(int size, int C, int k){
		//RoutingAlgorithm ra = new DepthFirstGreedy(500*size);
		Network kleinbergC = new Kleinberg1DC(size,1,1,1,false,false,C,null,null);
		Series.generate(kleinbergC, 5);
		
	}
	
	private static void testKleinberg1CRouting(int size, int C, int iter){
//		RoutingAlgorithm ra = new OneWorseGreedy((int)Math.ceil(2*Math.log(size)*Math.log(size)));
//		Network kleinbergC = new Kleinberg1DC(size,1,1,1,false,false,C,ra,null);
//		Series.generate(kleinbergC, iter);
		
		RoutingAlgorithm ra = new DepthFirstGreedy((int)Math.ceil(2*Math.log(size)*Math.log(size)));
		Network kleinbergC = new Kleinberg1DC(size,1,1,1,false,false,C,ra,null);
		Series.generate(kleinbergC, iter);
		
	}
	
	private static void testMotifs(){
		Network network = new Ring(4, null, null);
		Series.generate(network, 1);
	}
	
	private static void testFailures(int nr, int iter, int av){
		Transformation[] t = {new LargestFailure(5), new RandomFailure(100)};
		Network net = new BarabasiAlbert(nr,av,null,t);
		Series.generate(net, iter);
	}
	
	private static void testRandomDeg(){
		//double[] cdf = {0.0, 0.5, 1};
		ScaleFreeRandomGraph seq = new ScaleFreeRandomGraph(100,2.5,1,16,null,null);
		Series.generate(seq,1);
	}
	
	private static double[] getSizes(String file){
		double[] res = new double[2];
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null){
				if (line.contains("DEGREE_DISTRIBUTION_NODES")){
					res[0] = Double.parseDouble(line.split(":  ")[1]);
				}
				if (line.contains("STRONG_CONNECTIVITY_LARGEST_COMPONENT_FRACTION")){
					res[1] = Double.parseDouble(line.split(":  ")[1]);
				}
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	private static void testingADHT(int iter, double out, int minSucc, int up, String nodeType, int runs){
		Config.overwrite("MAIN_DATA_FOLDER", "ADHTNew");
		Config.overwrite("METRICS", "ROUTING, ID_DISTRIBUTION, SUCC_DISTRIBUTION");
		String[] name = {nodeType};
		RoutingAlgorithm ra =  new DepthFirstGreedy(174);
		
		
		Transformation[] adht = {new RandomRingIDSpaceSimple(), new ADHT(iter,name[0], out, minSucc, up)};
		Network net = new ReadableFile("SPI", "SPI", "graphs/spi.txt",ra,adht);
		Series.generate(net, runs);
		
		}
	
	private static void testingP2(int iter, double[] exponent, int minSucc, int up, String nodeType, int runs){
		Config.overwrite("MAIN_DATA_FOLDER", "P2EXPO");
		Config.overwrite("METRICS", "ROUTING, ID_DISTRIBUTION, SUCC_DISTRIBUTION");
		String[] name = {nodeType};
		RoutingAlgorithm ra =  new DepthFirstGreedy(174);
		
		for (int i = 0; i < exponent.length; i++){
		Transformation[] adht = {new RandomRingIDSpaceSimple(), new PowerTwoVar(iter,exponent[i], minSucc, up, name[0])};
		Network net = new ReadableFile("SPI", "SPI", "graphs/spi.txt",ra,adht);
		Series.generate(net, runs);
		}
		}
	   

}
