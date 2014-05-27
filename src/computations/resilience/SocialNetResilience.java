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
 * SocialNetResilience.java
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
package computations.resilience;

import gtna.data.Series;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.sorting.DegreeNodeSorter;
import gtna.graph.sorting.NodeSorter.NodeSorterMode;
import gtna.graph.sorting.RandomNodeSorter;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.fragmentation.CriticalPointsTheory;
import gtna.metrics.fragmentation.Fragmentation.Resolution;
import gtna.metrics.fragmentation.StrongFragmentation;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.util.ReadableFile;



/**
 * @author stef
 *
 */
public class SocialNetResilience {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
//		int[] n = {1000,10000,100000,1000000};
//		double[] fail= {0.01, 0.1, 0.3, 0.5, 0.9, 0.99};
//		double[] alpha = {2.2,2.3,2.4};
//		(new File("FailureDeg/")).mkdir();
//		for (int a = 0; a < alpha.length; a++){
//		 for (int i = 0; i < n.length; i++){
//			 System.out.println(n[i]+"-"+alpha[a] + new Date());
//			double[] dist = DegreeRemoval.getScaleFree(n[i], alpha[a], 1);
//			DegreeRemoval.writeLog(dist, "FailureDeg/"+n[i]+"-"+alpha[a]+"-0.0.txt");
//			for (int j = 0; j < fail.length; j++){
//				double[] distN = DegreeRemoval.removeRandom(dist, fail[j]);
//				DegreeRemoval.writeLog(distN, "FailureDeg/"+n[i]+"-"+alpha[a]+"-"+fail[j]+".txt");
//			}
//		 }
//		}
		int n = 10000;
		Network nw = new ErdosRenyi(n,5,true,null);
		Metric[] m = new Metric[]{new DegreeDistribution(),
				new StrongFragmentation(new RandomNodeSorter(),Resolution.PERCENT),
				new StrongFragmentation(new DegreeNodeSorter(NodeSorterMode.DESC),Resolution.PERCENT),
				new CriticalPointsTheory(false,CriticalPointsTheory.Selection.RANDOM),
				new CriticalPointsTheory(false,CriticalPointsTheory.Selection.LARGEST)};
		Series.generate(nw, m, 5);
//		String folder = "/home/stef/svns/drafts/osnResilience/data/undir/";
//		String[] files = {"astroPh.txt", "brightkite.txt", "facebook-infocom.txt", "facebook-wosn.txt",
//				"gowalla.txt"};
//		int[] max = {1010,1134,4960,2331, 14730};
//		for (int i = 0; i < files.length; i++){
//			double[] dist = DegreeRemoval.getDegreeFile(folder+files[i], max[i]);
//			DegreeRemoval.writeLog(dist, folder + "log" + files[i]);
//		}
//		System.out.println("Begin " + new Date());
//		int n = Integer.parseInt(args[1]);
//		try {
//			BufferedWriter bw = new BufferedWriter(new FileWriter(args[0] + ".txt"));
//			bw.write("#Random, largest, MaxDegree");
//			for (int i = 1; i< 11; i++){
//				double[][] cps = scaleFree(Double.parseDouble(args[0]), i, n);
//				String line = ""+i;
//				for (int j = 0; j < 3; j++){
//					line = line + " " + cps[j][0] + " " +cps[j][1];
//				}
//				bw.newLine();
//				bw.write(line);
//			}
//			bw.flush();
//			bw.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println("Done " + new Date());
	}
	
	public static double[][] scaleFree(double alpha, int min, int n){
		double[] dist = new double[n+1];
		double s = 0;
		for (int i = min; i < dist.length; i++){
			dist[i] = Math.pow(i, -alpha);
			s = s + dist[i];
		}
		for (int i = min; i < dist.length; i++){
			dist[i] = dist[i]/s;
		}
		double[][] re = new double[3][];
		re[0] = CriticalPointsTheory.getCPUndirectedRandom(dist);
		re[1] = CriticalPointsTheory.getCPUndirectedLargest(dist);
		re[2] = CriticalPointsTheory.getCPUndirectedMaxDegree(dist);
		return re;
	}

	
	public static void testMetric(){
		String file = "/home/stef/workspace/graphs/WOT-BI-GCC/2005-02-25.wot.txt";
		Network net = new ReadableFile("WOT", "WOT", file,null);
		Graph g = net.generate();
        Node[] nodes = g.getNodes();
		int maxIn = 0;
		int maxOut = 0;
		for (int i = 0; i < nodes.length; i++){
			if (nodes[i].getInDegree() > maxIn){
				maxIn = nodes[i].getInDegree();
			}
			if (nodes[i].getOutDegree() > maxOut){
				maxOut = nodes[i].getOutDegree();
			}
		}
//		int max = 0;
//		for (int i = 0; i < nodes.length; i++){
//			if (nodes[i].getInDegree() > max){
//				max = nodes[i].getInDegree();
//			}
//		}
//		for (int i = 0; i < dist.length; i++){
//			dist[i] = dist[i]/(double)nodes.length;
//		}
//		double[] dist = new double[max+1];
//		for (int i = 0; i < nodes.length; i++){
//			dist[nodes[i].getInDegree()]++;
//		}
		double[][] dist = new double[maxOut+1][maxIn+1];
		for (int i = 0; i < nodes.length; i++){
			dist[nodes[i].getOutDegree()][nodes[i].getInDegree()]++;
		}
		for (int i = 0; i < dist.length; i++){
			for (int j = 0; j < dist[i].length; j++){
				dist[i][j] = dist[i][j]/(double)nodes.length;
			}
		}


		double[] f = CriticalPointsTheory.getCPDirectedLargestIn(dist);
        System.out.println("Max In " + f[0] + " , " + f[1]);
//		double[] fL = CriticalPointsTheory.getCPUndirectedLargest(dist);
//		System.out.println("Largest In " + fL[0] + " , " + fL[1]);
//		double sum = 0;
//		for (int i = 0; i < dist.length; i++){
//			for (int j = 0; j < dist[i].length; j++){
//				sum = sum + dist[i][j];
//			}
//		}
//		System.out.println(sum);
		
	}
}
