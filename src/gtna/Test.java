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
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.networks.Network;
import gtna.networks.model.smallWorld.ScaleFreeUndirected;
import gtna.util.Config;

/**
 * @author stef
 *
 */
public class Test {
	
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		String[] routings = new String[18];
//		String [][] keys = new String[routings.length][2];
//		for (int i = 0; i < routings.length; i++){
//			keys[i] = new String[]{"ROUTING_SUCCESS_RATE", "ROUTING_HOPS_AVG"};
//		}
//		try {
//			BufferedWriter sr = new BufferedWriter(new FileWriter("sr.txt"));
//			BufferedWriter apl = new BufferedWriter(new FileWriter("apl.txt"));
//		String path = "data/Routing/Kleinberg1000/";
//		String[] sub = (new File(path)).list();
//		for (int i = 0; i < sub.length; i++){
//			String[] sub2 = (new File(path+sub[i])).list();
//			String[] sub3 = (new File(path+sub[i]+"/"+sub2[0])).list();
//			for (int j = 0; j < sub3.length; j++){
//				String[] sub4 = (new File(path+sub[i]+"/"+sub2[0]+ "/"+sub3[j])).list();
//				for (int k = 0; k < sub4.length; k++){
//					String[] files = (new File(path+sub[i]+"/"+sub2[0]+ "/"+sub3[j]+"/"+sub4[k])).list();
//					int count = 0;
//					for (int f = 0; f < files.length; f++){
//						if (files[f].contains("ROUTING")){
//							routings[count] = files[f];
//							count++;
//						}
//					}
//					double[] res = getSingleValue(path+sub[i]+"/"+sub2[0]+ "/"+sub3[j]+"/"+sub4[k]+"/", routings,keys);
//					for (int r = 0; r < res.length; r=r+2){
//					sr.write(sub[i]+"-"+sub3[j]+"-"+sub4[k] + "	" + routings[r/2] + "	" + res[r]);
//					sr.newLine();
//					apl.write(sub[i]+"-"+sub3[j]+"-"+sub4[k] + "	"+routings[r/2] + "	" + res[r+1]);
//					apl.newLine();
//					}
//				}
//
//			}
//		}
//		sr.flush();
//		sr.close();
//		apl.flush();
//		apl.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
////		String[] routings = { "ROUTING-DEPTH_FIRST_GREEDY",
////				"ROUTING-ONE_WORSE_GREEDY", "ROUTING-ONE_WORSE_GREEDY_NOINFO",
////				"ROUTING-DEPTH_FIRST_GREEDY_NOINFO" };
////		int[] C = { 1, 2, 3, 4,5,6,7, 8,9,10, 16, 32 };
////		int[] size = { 1000, 5000, 10000, 20000, 30000, 40000, 50000, 60000,
////				70000, 80000, 90000, 100000 };
////		// int[] size = {1000,5000};
////		double[] alpha = { 2.1, 2.15, 2.2, 2.25, 2.3, 2.35, 2.4, 2.45, 2.5 };
////		String folder = args[0];
////		String datafolder = args[1];
////		String single = args[2];
////		(new File(folder)).mkdir();
////		
////			(new File(folder + "/SizeVsAlpha/")).mkdir();
////			plotSizevsAlpha(size, alpha, C, folder + "/SizeVsAlpha/", datafolder, routings,
////					single);
////		
////		(new File(folder + "/SizeVsC/")).mkdir();
////			plotSizevsC(size, alpha, C, folder + "/SizeVsC/", datafolder, routings,
////					single);
////		
////		(new File(folder + "/SizeVsRouting/")).mkdir();
////		plotSizevsRouting(size, alpha, C, folder + "/SizeVsRouting"
////				 + "/", datafolder, routings,
////				single);
////		
////		(new File(folder + "/CVsAlpha/")).mkdir();
////		plotCvsAlpha(size, alpha, C, folder + "/CVsAlpha/", datafolder, routings,
////				single);
////	
////	(new File(folder + "/CVsSize/")).mkdir();
////		plotCvsSize(size, alpha, C, folder + "/CVsSize/", datafolder, routings,
////				single);
////	
////	(new File(folder + "/CVsRouting/")).mkdir();
////	plotCvsRouting(size, alpha, C, folder + "/CVsRouting"
////			 + "/", datafolder, routings,
////			single);
////	
////	(new File(folder + "/AlphaVsSize/")).mkdir();
////	plotAlphavsSize(size, alpha, C, folder + "/AlphaVsSize/", datafolder, routings,
////			single);
////
////(new File(folder + "/AlphaVsC/")).mkdir();
////	plotAlphavsC(size, alpha, C, folder + "/AlphaVsC/", datafolder, routings,
////			single);
////
////(new File(folder + "/AlphaVsRouting/")).mkdir();
////plotAlphavsRouting(size, alpha, C, folder + "/AlphaVsRouting"
////		 + "/", datafolder, routings,
////		single);
////		System.out.println("Done" + new Date());
//	}
//
//	private static void plotSizevsAlpha(int[] size, double[] alpha, int[] C,
//			String resultfolder, String datafolder, String[] routings,
//			String single) {
//		for (int i = 0; i < routings.length; i++){
//			String routing = routings[i];
//		String[] metrics = { routing };
//		String[][] keys = { new String[] { single } };
//		// double[][] vals = new double[size.length][alpha.length];
//		// step 1: get data
//		double[][] val;
//		for (int c = 0; c < C.length; c++) {
//			try {
//				BufferedWriter bwData = new BufferedWriter(new FileWriter(
//						resultfolder + "C=" + C[c] + "RA=" + routing + ".txt"));
//				for (int s = 0; s < size.length; s++) {
//					String line = "" + size[s];
//					for (int a = 0; a < alpha.length; a++) {
//						val = getSingleValueVariance(
//								datafolder
//										+ size[s]
//										+ "/READABLE_LIST_SCALE_FREE_UNDIRECTED_LD-"
//										+ size[s]
//										+ "-"
//										+ alpha[a]
//										+ "-"
//										+ (int) Math.sqrt(size[s])
//										+ "--2147483648--SCALE_FREE_UNDIRECTED_SD-"
//										+ C[c] + "/", metrics, keys);
//						if (val != null) {
//							// System.out.println(vals[0][0]);
//							line = line + "	" + val[0][0] + "	"
//									+ Math.sqrt(val[0][1]);
//						} else {
//							line = line + "	" + 0 + " " + 0;
//						}
//					}
//					// System.out.println(line);
//					bwData.write(line);
//					bwData.newLine();
//				}
//				bwData.flush();
//				bwData.close();
//				BufferedWriter bwPlot = new BufferedWriter(new FileWriter(
//						resultfolder + "C=" + C[c] + "RA=" + routing
//								+ "Plot.txt"));
//				bwPlot.write("reset");
//				bwPlot.newLine();
//				bwPlot.write("set terminal postscript eps color enhanced");
//				bwPlot.newLine();
//				bwPlot.write("set output " + "'" + resultfolder + "plots/C="
//						+ C[c] + "RA=" + routing + ".eps'");
//				bwPlot.newLine();
//				bwPlot.write("set title " + "'C=" + C[c] + " RA="
//						+ routing.replace("_", "") + "'");
//				bwPlot.newLine();
//				bwPlot.write("set xlabel  'size'");
//				bwPlot.newLine();
//				bwPlot.write("set ylabel 'Routing length'");
//				bwPlot.newLine();
//				bwPlot.write("set pointsize 2");
//				bwPlot.newLine();
//				String plot = "plot ";
//				for (int j = 0; j < alpha.length; j++) {
//					plot = plot + "'" + resultfolder + "C=" + C[c] + "RA="
//							+ routing + ".txt' using 1:" + (2 * j + 2) + ":"
//							+ (2 * j + 3) + " title 'alpha=" + alpha[j]
//							+ "' with errorbars";
//					if (j < alpha.length - 1) {
//						plot = plot + ", ";
//					}
//				}
//				bwPlot.write(plot);
//				(new File(resultfolder + "plots/")).mkdir();
////				Process p = Runtime.getRuntime().exec(
////						"/usr/bin/gnuplot" + " " + resultfolder + "C=" + C[c]
////								+ "RA=" + routing + "Plot.txt",
////						new String[] { "PATH=/usr/local/bin" });
//				bwPlot.flush();
//				bwPlot.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		}
//	}
//	
//	private static void plotSizevsC(int[] size, double[] alpha, int[] C,
//			String resultfolder, String datafolder, String[] routings,
//			String single) {
//		for (int i = 0; i < routings.length; i++){
//		String routing = routings[i];
//			String[] metrics = { routing };
//		String[][] keys = { new String[] { single } };
//		// double[][] vals = new double[size.length][alpha.length];
//		// step 1: get data
//		double[][] val;
//		for (int a = 0; a < alpha.length; a++) {
//			try {
//				BufferedWriter bwData = new BufferedWriter(new FileWriter(
//						resultfolder + "Alpha=" + alpha[a] + "RA=" + routing + ".txt"));
//				for (int s = 0; s < size.length; s++) {
//					String line = "" + size[s];
//					for (int c = 0; c < C.length; c++) {
//						val = getSingleValueVariance(
//								datafolder
//										+ size[s]
//										+ "/READABLE_LIST_SCALE_FREE_UNDIRECTED_LD-"
//										+ size[s]
//										+ "-"
//										+ alpha[a]
//										+ "-"
//										+ (int) Math.sqrt(size[s])
//										+ "--2147483648--SCALE_FREE_UNDIRECTED_SD-"
//										+ C[c] + "/", metrics, keys);
//						if (val != null) {
//							// System.out.println(vals[0][0]);
//							line = line + "	" + val[0][0] + "	"
//									+ Math.sqrt(val[0][1]);
//						} else {
//							line = line + "	" + 0 + " " + 0;
//						}
//					}
//					// System.out.println(line);
//					bwData.write(line);
//					bwData.newLine();
//				}
//				bwData.flush();
//				bwData.close();
//				BufferedWriter bwPlot = new BufferedWriter(new FileWriter(
//						resultfolder + "Alpha=" + alpha[a] + "RA=" + routing
//								+ "Plot.txt"));
//				bwPlot.write("reset");
//				bwPlot.newLine();
//				bwPlot.write("set terminal postscript eps color enhanced");
//				bwPlot.newLine();
//				bwPlot.write("set output " + "'" + resultfolder + "plots/Alpha=" + alpha[a] + "RA=" + routing + ".eps'");
//				bwPlot.newLine();
//				bwPlot.write("set title " + "'Alpha=" + alpha[a] + " RA="
//						+ routing.replace("_", "") + "'");
//				bwPlot.newLine();
//				bwPlot.write("set xlabel  'size'");
//				bwPlot.newLine();
//				bwPlot.write("set ylabel 'Routing length'");
//				bwPlot.newLine();
//				bwPlot.write("set pointsize 2");
//				bwPlot.newLine();
//				String plot = "plot ";
//				for (int j = 0; j < C.length; j++) {
//					plot = plot + "'" + resultfolder + "Alpha=" + alpha[a] + "RA="
//							+ routing + ".txt' using 1:" + (2 * j + 2) + ":"
//							+ (2 * j + 3) + " title 'C=" + C[j]
//							+ "' with errorbars";
//					if (j < C.length - 1) {
//						plot = plot + ", ";
//					}
//				}
//				bwPlot.write(plot);
//				(new File(resultfolder + "plots/")).mkdir();
////				Process p = Runtime.getRuntime().exec(
////						"/usr/bin/gnuplot" + " " + resultfolder + "Alpha=" + alpha[a]
////								+ "RA=" + routing + "Plot.txt",
////						new String[] { "PATH=/usr/local/bin" });
//				bwPlot.flush();
//				bwPlot.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		}
//	}
//	
//	private static void plotSizevsRouting(int[] size, double[] alpha, int[] C,
//			String resultfolder, String datafolder, String[] routing,
//			String single) {
//		
//		String[][] keys = new String[routing.length][1];
//		for (int i = 0; i < keys.length; i++){
//			keys[i][0] = single;
//		}
//		// double[][] vals = new double[size.length][alpha.length];
//		// step 1: get data
//		double[][] val;
//		for (int c = 0; c < C.length; c++){
//			for (int a = 0; a < alpha.length; a++){
//				try {
//					BufferedWriter bwData = new BufferedWriter(new FileWriter(
//							resultfolder + "C=" + C[c] + "Alpha=" + alpha[a] + ".txt"));
//					for (int s = 0; s < size.length; s++) {
//						String line = "" + size[s];
//						val = val = getSingleValueVariance(
//								datafolder
//								+ size[s]
//								+ "/READABLE_LIST_SCALE_FREE_UNDIRECTED_LD-"
//								+ size[s]
//								+ "-"
//								+ alpha[a]
//								+ "-"
//								+ (int) Math.sqrt(size[s])
//								+ "--2147483648--SCALE_FREE_UNDIRECTED_SD-"
//								+ C[c] + "/", routing, keys);
//						for (int j = 0; j < val.length; j++){
//							line = line + "	" + val[j][0] + "	" + Math.sqrt(val[j][1]);
//						}
//						bwData.write(line);
//						bwData.newLine();
//					}	
//					bwData.flush();
//					bwData.close();
//					BufferedWriter bwPlot = new BufferedWriter(new FileWriter(
//							resultfolder + "C=" + C[c] 
//									+ "Alpha="+alpha[a]+"Plot.txt"));
//					bwPlot.write("reset");
//					bwPlot.newLine();
//					bwPlot.write("set terminal postscript eps color enhanced");
//					bwPlot.newLine();
//					bwPlot.write("set output " + "'" + resultfolder + "plots/C="
//							+ C[c] + "Alpha="+alpha[a] + ".eps'");
//					bwPlot.newLine();
//					bwPlot.write("set title " + "'C=" + C[c] + " Alpha="+alpha[a] + "'");
//					bwPlot.newLine();
//					bwPlot.write("set xlabel  'size'");
//					bwPlot.newLine();
//					bwPlot.write("set ylabel 'Routing length'");
//					bwPlot.newLine();
//					bwPlot.write("set pointsize 2");
//					bwPlot.newLine();
//					String plot = "plot ";
//					for (int j = 0; j < routing.length; j++) {
//						plot = plot + "'" + resultfolder + "C=" + C[c] + "Alpha="+alpha[a] + ".txt' using 1:" + (2 * j + 2) + ":"
//								+ (2 * j + 3) + " title 'routing=" + routing[j].replace("_", "")
//								+ "' with errorbars";
//						if (j < routing.length - 1) {
//							plot = plot + ", ";
//						}
//					}
//					bwPlot.write(plot);
//					(new File(resultfolder + "plots/")).mkdir();
////					Process p = Runtime.getRuntime().exec(
////							"/usr/bin/gnuplot" + " " + resultfolder + "C=" + C[c]
////									+ "Alpha="+alpha[a] + "Plot.txt",
////							new String[] { "PATH=/usr/local/bin" });
//					bwPlot.flush();
//					bwPlot.close();	
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		}
//	}
//	
//	private static void plotCvsRouting(int[] size, double[] alpha, int[] C,
//			String resultfolder, String datafolder, String[] routing,
//			String single) {
//		
//		String[][] keys = new String[routing.length][1];
//		for (int i = 0; i < keys.length; i++){
//			keys[i][0] = single;
//		}
//		// double[][] vals = new double[size.length][alpha.length];
//		// step 1: get data
//		double[][] val;
//		for (int s = 0; s < size.length; s++){
//			for (int a = 0; a < alpha.length; a++){
//				try {
//					BufferedWriter bwData = new BufferedWriter(new FileWriter(
//							resultfolder + "N=" + size[s] + "Alpha=" + alpha[a] + ".txt"));
//					for (int c = 0; c < C.length; c++) {
//						String line = "" + C[c];
//						val = val = getSingleValueVariance(
//								datafolder
//								+ size[s]
//								+ "/READABLE_LIST_SCALE_FREE_UNDIRECTED_LD-"
//								+ size[s]
//								+ "-"
//								+ alpha[a]
//								+ "-"
//								+ (int) Math.sqrt(size[s])
//								+ "--2147483648--SCALE_FREE_UNDIRECTED_SD-"
//								+ C[c] + "/", routing, keys);
//						for (int j = 0; j < val.length; j++){
//							line = line + "	" + val[j][0] + "	" + Math.sqrt(val[j][1]);
//						}
//						bwData.write(line);
//						bwData.newLine();
//					}	
//					bwData.flush();
//					bwData.close();
//					BufferedWriter bwPlot = new BufferedWriter(new FileWriter(
//							resultfolder + "N=" + size[s] 
//									+ "Alpha="+alpha[a]+"Plot.txt"));
//					bwPlot.write("reset");
//					bwPlot.newLine();
//					bwPlot.write("set terminal postscript eps color enhanced");
//					bwPlot.newLine();
//					bwPlot.write("set output " + "'" + resultfolder + "plots/N=" + size[s] + "Alpha="+alpha[a] + ".eps'");
//					bwPlot.newLine();
//					bwPlot.write("set title " + "'N=" + size[s] + " Alpha="+alpha[a] + "'");
//					bwPlot.newLine();
//					bwPlot.write("set xlabel  'C'");
//					bwPlot.newLine();
//					bwPlot.write("set ylabel 'Routing length'");
//					bwPlot.newLine();
//					bwPlot.write("set pointsize 2");
//					bwPlot.newLine();
//					String plot = "plot ";
//					for (int j = 0; j < routing.length; j++) {
//						plot = plot + "'" + resultfolder + "N=" + size[s] + "Alpha="+alpha[a] + ".txt' using 1:" + (2 * j + 2) + ":"
//								+ (2 * j + 3) + " title 'routing=" + routing[j].replace("_", "")
//								+ "' with errorbars";
//						if (j < routing.length - 1) {
//							plot = plot + ", ";
//						}
//					}
//					bwPlot.write(plot);
//					(new File(resultfolder + "plots/")).mkdir();
////					Process p = Runtime.getRuntime().exec(
////							"/usr/bin/gnuplot" + " " + resultfolder + "N=" + size[s]
////									+ "Alpha="+alpha[a] + "Plot.txt",
////							new String[] { "PATH=/usr/local/bin" });
//					bwPlot.flush();
//					bwPlot.close();	
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		}
//	}
//	
//	private static void plotCvsAlpha(int[] size, double[] alpha, int[] C,
//			String resultfolder, String datafolder, String[] routings,
//			String single) {
//		for (int i = 0; i < routings.length; i++){
//			String routing = routings[i];
//		String[] metrics = { routing };
//		String[][] keys = { new String[] { single } };
//		// double[][] vals = new double[size.length][alpha.length];
//		// step 1: get data
//		double[][] val;
//		for (int s = 0; s < size.length; s++) {
//			try {
//				BufferedWriter bwData = new BufferedWriter(new FileWriter(
//						resultfolder + "N=" + size[s] + "RA=" + routing + ".txt"));
//				for (int c = 0; c < C.length; c++) {
//					String line = "" + C[c];
//					for (int a = 0; a < alpha.length; a++) {
//						val = getSingleValueVariance(
//								datafolder
//										+ size[s]
//										+ "/READABLE_LIST_SCALE_FREE_UNDIRECTED_LD-"
//										+ size[s]
//										+ "-"
//										+ alpha[a]
//										+ "-"
//										+ (int) Math.sqrt(size[s])
//										+ "--2147483648--SCALE_FREE_UNDIRECTED_SD-"
//										+ C[c] + "/", metrics, keys);
//						if (val != null) {
//							// System.out.println(vals[0][0]);
//							line = line + "	" + val[0][0] + "	"
//									+ Math.sqrt(val[0][1]);
//						} else {
//							line = line + "	" + 0 + " " + 0;
//						}
//					}
//					// System.out.println(line);
//					bwData.write(line);
//					bwData.newLine();
//				}
//				bwData.flush();
//				bwData.close();
//				BufferedWriter bwPlot = new BufferedWriter(new FileWriter(
//						resultfolder + "N=" + size[s] + "RA=" + routing
//								+ "Plot.txt"));
//				bwPlot.write("reset");
//				bwPlot.newLine();
//				bwPlot.write("set terminal postscript eps color enhanced");
//				bwPlot.newLine();
//				bwPlot.write("set output " + "'" + resultfolder + "plots/N=" + size[s] + "RA=" + routing + ".eps'");
//				bwPlot.newLine();
//				bwPlot.write("set title " + "'N=" + size[s] + " RA="
//						+ routing.replace("_", "") + "'");
//				bwPlot.newLine();
//				bwPlot.write("set xlabel  'C'");
//				bwPlot.newLine();
//				bwPlot.write("set ylabel 'Routing length'");
//				bwPlot.newLine();
//				bwPlot.write("set pointsize 2");
//				bwPlot.newLine();
//				String plot = "plot ";
//				for (int j = 0; j < alpha.length; j++) {
//					plot = plot + "'" + resultfolder + "N=" + size[s] + "RA="
//							+ routing + ".txt' using 1:" + (2 * j + 2) + ":"
//							+ (2 * j + 3) + " title 'alpha=" + alpha[j]
//							+ "' with errorbars";
//					if (j < alpha.length - 1) {
//						plot = plot + ", ";
//					}
//				}
//				bwPlot.write(plot);
//				(new File(resultfolder + "plots/")).mkdir();
////				Process p = Runtime.getRuntime().exec(
////						"/usr/bin/gnuplot" + " " + resultfolder + "N=" + size[s]
////								+ "RA=" + routing + "Plot.txt",
////						new String[] { "PATH=/usr/local/bin" });
//				bwPlot.flush();
//				bwPlot.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		}
//	}
//	
//	private static void plotCvsSize(int[] size, double[] alpha, int[] C,
//			String resultfolder, String datafolder, String[] routings,
//			String single) {
//		for (int i = 0; i < routings.length; i++){
//		String routing = routings[i];
//			String[] metrics = { routing };
//		String[][] keys = { new String[] { single } };
//		// double[][] vals = new double[size.length][alpha.length];
//		// step 1: get data
//		double[][] val;
//		for (int a = 0; a < alpha.length; a++) {
//			try {
//				BufferedWriter bwData = new BufferedWriter(new FileWriter(
//						resultfolder + "Alpha=" + alpha[a] + "RA=" + routing + ".txt"));
//				for (int c = 0; c < C.length; c++) {
//					String line = "" + C[c];
//					for (int s = 0; s < size.length; s++) {
//						val = getSingleValueVariance(
//								datafolder
//										+ size[s]
//										+ "/READABLE_LIST_SCALE_FREE_UNDIRECTED_LD-"
//										+ size[s]
//										+ "-"
//										+ alpha[a]
//										+ "-"
//										+ (int) Math.sqrt(size[s])
//										+ "--2147483648--SCALE_FREE_UNDIRECTED_SD-"
//										+ C[c] + "/", metrics, keys);
//						if (val != null) {
//							// System.out.println(vals[0][0]);
//							line = line + "	" + val[0][0] + "	"
//									+ Math.sqrt(val[0][1]);
//						} else {
//							line = line + "	" + 0 + " " + 0;
//						}
//					}
//					// System.out.println(line);
//					bwData.write(line);
//					bwData.newLine();
//				}
//				bwData.flush();
//				bwData.close();
//				BufferedWriter bwPlot = new BufferedWriter(new FileWriter(
//						resultfolder + "Alpha=" + alpha[a] + "RA=" + routing
//								+ "Plot.txt"));
//				bwPlot.write("reset");
//				bwPlot.newLine();
//				bwPlot.write("set terminal postscript eps color enhanced");
//				bwPlot.newLine();
//				bwPlot.write("set output " + "'" + resultfolder + "plots/Alpha=" + alpha[a] + "RA=" + routing + ".eps'");
//				bwPlot.newLine();
//				bwPlot.write("set title " + "'Alpha=" + alpha[a] + " RA="
//						+ routing.replace("_", "") + "'");
//				bwPlot.newLine();
//				bwPlot.write("set xlabel  'C'");
//				bwPlot.newLine();
//				bwPlot.write("set ylabel 'Routing length'");
//				bwPlot.newLine();
//				bwPlot.write("set pointsize 2");
//				bwPlot.newLine();
//				String plot = "plot ";
//				for (int j = 0; j < size.length; j++) {
//					plot = plot + "'" + resultfolder + "Alpha=" + alpha[a] + "RA="
//							+ routing + ".txt' using 1:" + (2 * j + 2) + ":"
//							+ (2 * j + 3) + " title 'N=" + size[j]
//							+ "' with errorbars";
//					if (j < size.length - 1) {
//						plot = plot + ", ";
//					}
//				}
//				bwPlot.write(plot);
//				(new File(resultfolder + "plots/")).mkdir();
////				Process p = Runtime.getRuntime().exec(
////						"/usr/bin/gnuplot" + " " + resultfolder + "Alpha=" + alpha[a]
////								+ "RA=" + routing + "Plot.txt",
////						new String[] { "PATH=/usr/local/bin" });
//				bwPlot.flush();
//				bwPlot.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		}
//	}
//	
//	private static void plotAlphavsRouting(int[] size, double[] alpha, int[] C,
//			String resultfolder, String datafolder, String[] routing,
//			String single) {
//		
//		String[][] keys = new String[routing.length][1];
//		for (int i = 0; i < keys.length; i++){
//			keys[i][0] = single;
//		}
//		// double[][] vals = new double[size.length][alpha.length];
//		// step 1: get data
//		double[][] val;
//		for (int s = 0; s < size.length; s++){
//			for (int c = 0; c < C.length; c++){
//				try {
//					BufferedWriter bwData = new BufferedWriter(new FileWriter(
//							resultfolder + "N=" + size[s] + "C=" + C[c] + ".txt"));
//					for (int a = 0; a < alpha.length; a++) {
//						String line = "" + alpha[a];
//						val = val = getSingleValueVariance(
//								datafolder
//								+ size[s]
//								+ "/READABLE_LIST_SCALE_FREE_UNDIRECTED_LD-"
//								+ size[s]
//								+ "-"
//								+ alpha[a]
//								+ "-"
//								+ (int) Math.sqrt(size[s])
//								+ "--2147483648--SCALE_FREE_UNDIRECTED_SD-"
//								+ C[c] + "/", routing, keys);
//						for (int j = 0; j < val.length; j++){
//							line = line + "	" + val[j][0] + "	" + Math.sqrt(val[j][1]);
//						}
//						bwData.write(line);
//						bwData.newLine();
//					}	
//					bwData.flush();
//					bwData.close();
//					BufferedWriter bwPlot = new BufferedWriter(new FileWriter(
//							resultfolder + "N=" + size[s] 
//									+ "C=" + C[c] +"Plot.txt"));
//					bwPlot.write("reset");
//					bwPlot.newLine();
//					bwPlot.write("set terminal postscript eps color enhanced");
//					bwPlot.newLine();
//					bwPlot.write("set output " + "'" + resultfolder + "plots/N=" + size[s] + "C=" + C[c] + "eps'");
//					bwPlot.newLine();
//					bwPlot.write("set title " + "'N=" + size[s] + " C=" + C[c] + "'");
//					bwPlot.newLine();
//					bwPlot.write("set xlabel  'alpha'");
//					bwPlot.newLine();
//					bwPlot.write("set ylabel 'Routing length'");
//					bwPlot.newLine();
//					bwPlot.write("set pointsize 2");
//					bwPlot.newLine();
//					String plot = "plot ";
//					for (int j = 0; j < routing.length; j++) {
//						plot = plot + "'" + resultfolder + "N=" + size[s] + "C=" + C[c] + ".txt' using 1:" + (2 * j + 2) + ":"
//								+ (2 * j + 3) + " title 'routing=" + routing[j].replace("_","")
//								+ "' with errorbars";
//						if (j < routing.length - 1) {
//							plot = plot + ", ";
//						}
//					}
//					bwPlot.write(plot);
//					(new File(resultfolder + "plots/")).mkdir();
////					Process p = Runtime.getRuntime().exec(
////							"/usr/bin/gnuplot" + " " + resultfolder + "N=" + size[s]
////									+ "C=" + C[c] + "Plot.txt",
////							new String[] { "PATH=/usr/local/bin" });
//					bwPlot.flush();
//					bwPlot.close();	
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		}
//	}
//	
//	private static void plotAlphavsSize(int[] size, double[] alpha, int[] C,
//			String resultfolder, String datafolder, String[] routings,
//			String single) {
//		for (int i = 0; i < routings.length; i++){
//			String routing = routings[i];
//		String[] metrics = { routing };
//		String[][] keys = { new String[] { single } };
//		// double[][] vals = new double[size.length][alpha.length];
//		// step 1: get data
//		double[][] val;
//		for (int c = 0; c < C.length; c++) {
//			try {
//				BufferedWriter bwData = new BufferedWriter(new FileWriter(
//						resultfolder + "C=" + C[c] + "RA=" + routing + ".txt"));
//				for (int a = 0; a < alpha.length; a++) {
//					String line = "" + alpha[a];
//					for (int s = 0; s < size.length; s++) {
//						val = getSingleValueVariance(
//								datafolder
//										+ size[s]
//										+ "/READABLE_LIST_SCALE_FREE_UNDIRECTED_LD-"
//										+ size[s]
//										+ "-"
//										+ alpha[a]
//										+ "-"
//										+ (int) Math.sqrt(size[s])
//										+ "--2147483648--SCALE_FREE_UNDIRECTED_SD-"
//										+ C[c] + "/", metrics, keys);
//						if (val != null) {
//							// System.out.println(vals[0][0]);
//							line = line + "	" + val[0][0] + "	"
//									+ Math.sqrt(val[0][1]);
//						} else {
//							line = line + "	" + 0 + " " + 0;
//						}
//					}
//					// System.out.println(line);
//					bwData.write(line);
//					bwData.newLine();
//				}
//				bwData.flush();
//				bwData.close();
//				BufferedWriter bwPlot = new BufferedWriter(new FileWriter(
//						resultfolder + "C=" + C[c] + "RA=" + routing
//								+ "Plot.txt"));
//				bwPlot.write("reset");
//				bwPlot.newLine();
//				bwPlot.write("set terminal postscript eps color enhanced");
//				bwPlot.newLine();
//				bwPlot.write("set output " + "'" + resultfolder + "plots/C="
//						+ C[c] + "RA=" + routing + ".eps'");
//				bwPlot.newLine();
//				bwPlot.write("set title " + "'C=" + C[c] + " RA="
//						+ routing.replace("_", "") + "'");
//				bwPlot.newLine();
//				bwPlot.write("set xlabel  'alpha'");
//				bwPlot.newLine();
//				bwPlot.write("set ylabel 'Routing length'");
//				bwPlot.newLine();
//				bwPlot.write("set pointsize 2");
//				bwPlot.newLine();
//				String plot = "plot ";
//				for (int j = 0; j < size.length; j++) {
//					plot = plot + "'" + resultfolder + "C=" + C[c] + "RA="
//							+ routing + ".txt' using 1:" + (2 * j + 2) + ":"
//							+ (2 * j + 3) + " title 'N=" + size[j]
//							+ "' with errorbars";
//					if (j < size.length - 1) {
//						plot = plot + ", ";
//					}
//				}
//				bwPlot.write(plot);
//				(new File(resultfolder + "plots/")).mkdir();
////				Process p = Runtime.getRuntime().exec(
////						"/usr/bin/gnuplot" + " " + resultfolder + "C=" + C[c]
////								+ "RA=" + routing + "Plot.txt",
////						new String[] { "PATH=/usr/local/bin" });
//				bwPlot.flush();
//				bwPlot.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		}
//	}
//	
//	private static void plotAlphavsC(int[] size, double[] alpha, int[] C,
//			String resultfolder, String datafolder, String[] routings,
//			String single) {
//		for (int i = 0; i < routings.length; i++){
//		String routing = routings[i];
//			String[] metrics = { routing };
//		String[][] keys = { new String[] { single } };
//		// double[][] vals = new double[size.length][alpha.length];
//		// step 1: get data
//		double[][] val;
//		for (int s = 0; s < size.length; s++) {
//			try {
//				BufferedWriter bwData = new BufferedWriter(new FileWriter(
//						resultfolder + "N=" + size[s] + "RA=" + routing + ".txt"));
//				for (int a = 0; a < alpha.length; a++) {
//					String line = "" + alpha[a];
//					for (int c = 0; c < C.length; c++) {
//						val = getSingleValueVariance(
//								datafolder
//										+ size[s]
//										+ "/READABLE_LIST_SCALE_FREE_UNDIRECTED_LD-"
//										+ size[s]
//										+ "-"
//										+ alpha[a]
//										+ "-"
//										+ (int) Math.sqrt(size[s])
//										+ "--2147483648--SCALE_FREE_UNDIRECTED_SD-"
//										+ C[c] + "/", metrics, keys);
//						if (val != null) {
//							// System.out.println(vals[0][0]);
//							line = line + "	" + val[0][0] + "	"
//									+ Math.sqrt(val[0][1]);
//						} else {
//							line = line + "	" + 0 + " " + 0;
//						}
//					}
//					// System.out.println(line);
//					bwData.write(line);
//					bwData.newLine();
//				}
//				bwData.flush();
//				bwData.close();
//				BufferedWriter bwPlot = new BufferedWriter(new FileWriter(
//						resultfolder + "N=" + size[s] + "RA=" + routing
//								+ "Plot.txt"));
//				bwPlot.write("reset");
//				bwPlot.newLine();
//				bwPlot.write("set terminal postscript eps color enhanced");
//				bwPlot.newLine();
//				bwPlot.write("set output " + "'" + resultfolder + "plots/N=" + size[s] + "RA=" + routing + ".eps'");
//				bwPlot.newLine();
//				bwPlot.write("set title " + "'N=" + size[s] + " RA="
//						+ routing.replace("_", "") + "'");
//				bwPlot.newLine();
//				bwPlot.write("set xlabel  'alpha'");
//				bwPlot.newLine();
//				bwPlot.write("set ylabel 'Routing length'");
//				bwPlot.newLine();
//				bwPlot.write("set pointsize 2");
//				bwPlot.newLine();
//				String plot = "plot ";
//				for (int j = 0; j < C.length; j++) {
//					plot = plot + "'" + resultfolder + "N=" + size[s] + "RA="
//							+ routing + ".txt' using 1:" + (2 * j + 2) + ":"
//							+ (2 * j + 3) + " title 'C=" + C[j]
//							+ "' with errorbars";
//					if (j < C.length - 1) {
//						plot = plot + ", ";
//					}
//				}
//				bwPlot.write(plot);
//				(new File(resultfolder + "plots/")).mkdir();
////				Process p = Runtime.getRuntime().exec(
////						"/usr/bin/gnuplot" + " " + resultfolder + "N=" + size[s]
////								+ "RA=" + routing + "Plot.txt",
////						new String[] { "PATH=/usr/local/bin" });
//				bwPlot.flush();
//				bwPlot.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		}
//	}
//	
//		
//
//	private static void emebdding() {
//		String[] metrics = { "ROUTING" };
//		String[][] keys = { new String[] { "SUCCESS_RATE" } };
//		Distance[] d = Distance.values();
//		String[] algo = { "KLEINBERG", "REGION_COVERAGE", "DISTANCE_DIVERSITY" };
//		String[] bol = { "true", "false" };
//		IdentifierMethod[] id = IdentifierMethod.values();
//		DecisionMethod[] dm = DecisionMethod.values();
//		String folder = "../../embedding/data/WOT-WOT2005/READABLE_FILE_WOT2005-25487--RANDOM_RING_ID_SPACE_SIMPLE-";
//		// "CLOCKWISE--DISTANCE_DIVERSITY-1000-ALLNEIGHBORMIDDLE-PROPORTIONAL-1.220703125E-4-true-CLOCKWISE-true-NONE-RANDOM-0-20-0.5"
//		int max = 20;
//		double r = 0.5;
//		double val;
//		for (int i = 0; i < algo.length; i++) {
//			for (int j = 0; j < d.length; j++) {
//				for (int b1 = 0; b1 < 2; b1++) {
//					for (int b2 = 0; b2 < 2; b2++) {
//						System.out.println("\\begin{table}");
//						String line = "\\begin{tabular}{r";
//						for (int k = 0; k < id.length; k++) {
//							line = line + "|r";
//						}
//						System.out.println(line + "}");
//						line = "Decision ";
//						for (int k = 0; k < id.length; k++) {
//							line = line + " & " + id[k].toString();
//						}
//						System.out.println(line + " \\\\ \\hline");
//						for (int l = 0; l < dm.length; l++) {
//							line = dm[l].toString();
//							for (int k = 0; k < id.length; k++) {
//								String curFolder = folder
//										+ (d[j] == Distance.SIGNED ? "RING"
//												: d[j].toString()) + "--"
//										+ algo[i] + "-1000-" + id[k].toString()
//										+ "-" + dm[l].toString()
//										+ "-1.220703125E-4-" + bol[b1] + "-"
//										+ d[j].toString() + "-" + bol[b2]
//										+ "-NONE-RANDOM-0";
//								if (i > 0) {
//									curFolder = curFolder + "-" + max;
//								}
//								if (i == 2) {
//									curFolder = curFolder + "-" + r;
//								}
//								if ((new File(curFolder).exists())) {
//									val = Math.round(getSingleValue(curFolder
//											+ "/", metrics, keys)[0] * 100)
//											/ (double) 100;
//								} else {
//									val = -1;
//								}
//								line = line + " & " + val;
//							}
//							System.out.println(line + "\\\\");
//						}
//						System.out.println("\\end{tabular}");
//						System.out.println("\\caption{" + algo[i] + "-" + d[j]
//								+ "-" + bol[b1] + "-" + bol[b2] + "}");
//						System.out.println("\\end{table}");
//						System.out.println();
//					}
//				}
//			}
//		}
//	}
//
//	private static void multidim() {
//		String[] metrics = { "ROUTING-DEPTH_FIRST_GREEDY-174" };
//		String[][] keys = { new String[] { "SUCCESS_RATE" } };
//		String folder;
//		String[] type = { "LMC", "SWAPPING" };
//		String[] attack = { "NONE", "CONTRACTION", "DIVERGENCE", "REJECTION" };
//		String[][] parts;
//		for (int j = 2; j < 6; j++) {
//			folder = "data/MDMANHATTAN" + j + "/";
//			System.out.println("Dimension " + j);
//			String[] files = (new File(folder)).list();
//			parts = new String[files.length][];
//			for (int k = 0; k < files.length; k++) {
//				parts[k] = files[k].split("--");
//			}
//
//			for (int t = 0; t < type.length; t++) {
//				for (int a = 0; a < attack.length; a++) {
//					for (int i = 0; i < files.length; i++) {
//						if (parts[i].length == 2
//								&& parts[i][1].contains(type[t])
//								&& parts[i][1].contains(attack[a])) {
//							double[] res = getSingleValue(folder + files[i]
//									+ "/", metrics, keys);
//							System.out.println(type[t] + " - " + attack[a]
//									+ ": " + Math.round(res[0] * 100)
//									/ (double) 100);
//						}
//					}
//					for (int i = 0; i < files.length; i++) {
//						if (parts[i].length == 3
//								&& parts[i][1].contains(type[t])
//								&& parts[i][2].contains(attack[a])) {
//							double[] res = getSingleValue(folder + files[i]
//									+ "/", metrics, keys);
//							System.out.println(type[t] + " - NONE -"
//									+ attack[a] + ": "
//									+ Math.round(res[0] * 100) / (double) 100);
//						}
//					}
//				}
//			}
//			System.out.println();
//		}
//	}
//
//	public static void mainMotif() {
//		String[] lang = { "deu", "eng", "fra", "pol" };
//		int[] k = { 1, 2, 3, 5 };
//		int[] sig = { 1083, 1513 };
//		String[] n = { "2gram", "3gram", "3gram-ipol", "real" };
//		String[] metrics = { "DEGREE_DISTRIBUTION", "CLUSTERING_COEFFICIENT" };
//		String[] all = (new File("../GTNATrans/data/4lang/")).list();
//		String[][] keys = {
//				new String[] { "NODES", "EDGES", "OUT_DEGREE_AVG" },
//				new String[] { "CLUSTERING_COEFFICIENT_CLUSTERING_COEFFICIENT",
//						"TRANSITIVITY" } };
//		for (int l = 0; l < lang.length; l++) {
//			try {
//				BufferedWriter bw = new BufferedWriter(new FileWriter(lang[l]));
//				bw.write(" & Nodes & Edges & Average Degree & CC & Transitivity \\\\ \\hline");
//				for (int i = 0; i < sig.length; i++) {
//					for (int j = 0; j < k.length; j++) {
//						for (int s = 0; s < n.length; s++) {
//							String name = "";
//							for (int a = 0; a < all.length; a++) {
//								if (all[a].contains(lang[l])
//										&& all[a].contains(sig[i] + "")
//										&& all[a].contains(k[j] + "K")
//										&& all[a].contains(n[s])) {
//									name = all[a];
//									break;
//								}
//							}
//
//							double[] entries = getSingleValue(
//									"../GTNATrans/data/4lang/" + name + "/",
//									metrics, keys);
//							String line = "v=" + k[j] + "K, S=" + sig[i]
//									+ ",n=" + n[s];
//							for (int t = 0; t < entries.length; t++) {
//								if (t < 2) {
//									line = line + " & "
//											+ Math.round(entries[t]);
//								} else {
//									if (t < 3) {
//										line = line + " & "
//												+ Math.round(entries[t] * 100)
//												/ (double) 100;
//									} else {
//										line = line
//												+ " & "
//												+ Math.round(entries[t] * 10000)
//												/ (double) 10000;
//									}
//								}
//							}
//							bw.newLine();
//							bw.write(line + " \\\\");
//						}
//						bw.write(" \\hline");
//					}
//					bw.write(" \\hline");
//				}
//				bw.flush();
//				bw.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}

	public static void main(String[] args){
//		Network net = new ReadableFile("KARATE", "KARATE", "../graphs/karate.txt", null);
//		Graph g = net.generate();
//		Metric mod = new Modularity(new int[] {2,2,2,2,1,1,1,2,3,3,1,2,2,2,3,3,1,2,3,2,3,1,3,4,4,4,3,4,4,3,3,4,3,3});
//		mod.computeData(g, net, null);
//		int[] nodes = {1000,10000};
//		int[] degree = {2,4,6,8,10};
//		double[][][] singles = new double[degree.length][nodes.length][4];
//		String[] metrics = {"ROUTINGSP-GREEDYTREE"};
//		String[][] keys = {new String[] {"ROUTINGSP_STRETCH", "ROUTINGSP_TREE_EDGES", "ROUTINGSP_TREE_EDGES_AVG", "ROUTINGSP_TREESTRETCH"}};
//		for (int i = 0; i < degree.length; i++){
//			for (int j = 0; j < nodes.length; j++){
//				singles[i][j] = GetValuesGTNA.getSingleValue("../GTNASpectral/data/stretch/BARABASI_ALBERT-"+nodes[j]+"-"+degree[i]+"--PREFIXEMBEDDINGSTVIRTUAL--1-2147483647-160/", metrics, keys);
//			}
//		}
//		for (int k = 0; k < keys[0].length; k++){
//			try {
//				BufferedWriter bw = new BufferedWriter(new FileWriter("../GTNASpectral/data/stretch/"+keys[0][k]));
//				bw.write("# degree, 1000 nodes, 10000 nodes");
//				for (int i = 0; i < degree.length; i++){
//					String line = "" + degree[i];
//					for (int j = 0; j < nodes.length; j++){
//						line = line + "	" + singles[i][j][k];
//					}
//					bw.newLine();
//					bw.write(line);
//					
//				}
//				bw.flush();
//				bw.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		Config.overwrite("SERIES_GRAPH_WRITE", ""+true);
		int n = Integer.parseInt(args[0]);
		Network net = new ScaleFreeUndirected(n,2.3,2,(int)Math.sqrt(n), null);
			Metric[] m = new Metric[]{new DegreeDistribution()};
			Series.generate(net, m, 1);
		
	}

}
