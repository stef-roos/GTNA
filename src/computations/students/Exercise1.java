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
 * Exercise1.java
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
import gtna.graph.sorting.RandomNodeSorter;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.metrics.Metric;
import gtna.metrics.basic.ClusteringCoefficient;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.basic.ShortestPaths;
import gtna.metrics.fragmentation.Fragmentation;
import gtna.metrics.fragmentation.WeakFragmentation;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.model.smallWorld.Kleinberg;
import gtna.networks.util.ReadableFile;
import gtna.plot.Plotting;
import gtna.util.Config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author stef
 *
 */
public class Exercise1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config.overwrite("SERIES_GRAPH_WRITE", ""+true);
		Network nw1 = new Kleinberg(45, 2, 1,1,2,true,true, null);
		//Graph g = nw1.generate();
		Metric[] m = new Metric[]{new DegreeDistribution(), new ShortestPaths()};
		Series s = Series.generate(nw1, m, 1);
		//new GtnaGraphWriter().write(g, "./data/firstExample-graph.txt");
//		Config.overwrite("MAIN_DATA_FOLDER", "data/ex9/");
//		//Config.overwrite("MAIN_PLOT_FOLDER", "data/ex1/");
//		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
//		res();
		//cc();
	}
	
	private static void sn(){
		Network nw = new ReadableFile("SN", "SN", "data/ex1/graphs/social", null);
		Metric[] m = new Metric[]{new DegreeDistribution(), new ShortestPaths()};
		Series s = Series.generate(nw, m, 1);
		Plotting.multi(s, m, "plots/");
	}
	
	private static void cc(){
		Network nw = new ReadableFile("G", "G", "data/ex1/graphs/G", null);
		Metric[] m = new Metric[]{new ClusteringCoefficient()};
		Series s = Series.generate(nw, m, 1);
		Plotting.multi(s, m, "plots/");
		
		nw = new ReadableFile("H", "H", "data/ex1/graphs/H", null);
		s = Series.generate(nw, m, 1);
		Plotting.multi(s, m, "plots/");
	}
	
	public static void res(){
		//Network ba = new PowerLawRandomGraph(10000,2.5,1,10000,false,null);
		Network er = new ErdosRenyi(10000,3.68,true,null);
		//Network er2 = new ErdosRenyi(10000,4,true,null);
		Metric[] m = new Metric[]{new DegreeDistribution(),new WeakFragmentation(new RandomNodeSorter(),Fragmentation.Resolution.PERCENT)};
		//Series.generate(ba, m, 1);
		Series.generate(er, m, 1);
		//Series.generate(er2, m, 1);
	}
	
	public static double markov(int k){
		double[][] T = {new double[]{0.85, 0.05}, new double[]{0.15, 0.95}};
		double[] init = {1,0};
		double[] next = new double[2];
		for (int i = 0; i < k; i++){
			next[0] = 0;
			next[1] = 0;
			for (int j = 0; j < 2; j++){
				for (int m = 0; m < 2; m++){
					next[j] = next[j] + T[j][m]*init[m];
				}
			}
			init[0] = next[0];
			init[1] = next[1];
		}
		return next[0];
	}
	
	public static void ex12(){
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("data.txt"));
			for (int i = 0; i <= 1000; i++){
				double val = Math.abs(i*0.001-0.5);
				if (val == 0){
					bw.write(i*0.001 + " " + 0);
				} else {
					bw.write(i*0.001 + " " + (1-Math.exp(-10*val)));
				}
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void ex12markov(){
		double old = 1;
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("data.txt"));
			bw.write(0 + " " + old);
			for (int i = 1; i <= 20; i++){
				double val = 0.1*old+0.7*(1-old);
				old = val;
				bw.newLine();
				bw.write(i + " " + val);
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void bonus(){
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("bonus.txt"));
			for (int i = 0; i < 238; i++){
				if (i < 94){
					bw.write(i + " " + 0);
				} else {
					double b = (i-93)*0.125;
					int p = (int)Math.floor(b);
					if (b-p > 0.6){
						bw.write(i + " " + (p+1)); 
					} else{
						if (b-p > 0.1){
							bw.write(i + " " + (p+0.5));
						}else {
							bw.write(i + " " + p);
						}
					}
					
				}
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void compareMat(){
		try {
			BufferedReader bw = new BufferedReader(new FileReader("/home/stef/svns/p2pWS12/theory"));
			BufferedReader bw2 = new BufferedReader(new FileReader("/home/stef/svns/p2pWS12/practical"));
			String line,line2;
			int counter = 0;
			while ((line=bw.readLine())!= null && (line2=bw2.readLine()) != null){
				counter++;
				int mat1 = Integer.parseInt(line.split("	")[0]);
				int mat2 = Integer.parseInt(line2.split("	")[2]);
				if (mat1 != mat2){
					System.out.println("No agreement " + mat1 + " " + mat2 + " " + counter);
					break;
				}
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void getPointTable(){
		try {
			BufferedReader bw = new BufferedReader(new FileReader("/home/stef/svns/p2pWS12/theory"));
			BufferedReader bw2 = new BufferedReader(new FileReader("/home/stef/svns/p2pWS12/practical"));
			String line,line2;
			int counter = 0;
			String[] parts,parts2;
			while ((line=bw.readLine())!= null && (line2=bw2.readLine()) != null){
				counter++;
				parts = line.split("	");
				parts2 = line2.split("	");
				String entry = parts[0];
				String[] points = {parts.length>5?parts[5]:"",parts2.length>4?parts2[4]:"",parts.length>6?parts[6]:"",
						parts2.length>5?parts2[5]:"", parts.length>7?parts[7]:"",parts2.length>6?parts2[6]:"",
						parts.length>8?parts[8]:"",parts2.length>7?parts2[7]:"",parts.length>9?parts[9]:"",parts.length>10?parts[10]:"",
								parts2.length>8?parts2[8]:"",parts.length>11?parts[11]:""};
				double sum = 0;
				for (int i = 0; i < points.length; i++){
					if (points[i].length() == 0){
						entry = entry + " & 0"; 
					} else {
						entry = entry + " & " + points[i];
						sum = sum + Double.parseDouble(points[i]);
					}
				}
				entry = entry + " & \\textbf{"+sum+"}";
				entry = entry + " & "+(double)Math.round(sum/237.00*10000)/100;
				entry = entry + " & \\textbf{"+getBonus(sum)+"}";
				System.out.println(entry + " \\\\");
			}
			bw.close();
			bw2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static double getBonus(double sum){
		
			double b;
				if (sum < 94){
					b=0;
				} else {
					double r = (sum-93)*0.125;
					int p = (int)Math.floor(r);
					if (r-p > 0.50){
						b=p+1; 
					} else{
						if (r-p > 0.0){
							b=p+0.5;
						}else {
							b=p;
						}
					}
					
				}
		return b;
	}
}
