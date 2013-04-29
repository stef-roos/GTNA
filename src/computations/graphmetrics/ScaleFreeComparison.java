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
 * ScaleFreeComparison.java
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
package computations.graphmetrics;

import gtna.data.Series;
import gtna.graph.sorting.RandomNodeSorter;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.fragmentation.Fragmentation;
import gtna.metrics.fragmentation.WeakFragmentation;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.model.randomGraphs.PowerLawRandomGraph;
import gtna.util.Config;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author stef
 *
 */
public class ScaleFreeComparison {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//double[] p = computeResilience(2.1, 1, 1000);
		int[] n = {100};
		double step = 0.001;
		int[] init = {1,2,3,4,5};
		double[] p;
		
		for (int i = 0; i < n.length; i++){
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter("n="+n[i]+"SF.txt"));
				BufferedWriter bwR = new BufferedWriter(new FileWriter("n="+n[i]+"R.txt"));
				double alpha = 2 + step;
				while (alpha < 3){
					String line = alpha + "";
					String lineR = alpha + "";
					for (int j = 0; j < init.length; j++){
						p = computeResilience(alpha, init[j],n[i]);
						line = line + " " + p[0];
						lineR = lineR + " " + p[1];
					}
					bw.write(line);
					bw.newLine();
					bwR.write(lineR);
					bwR.newLine();
					alpha = alpha + step;
				}
				bw.flush();
				bw.close();
				bwR.flush();
				bwR.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void plotSF(){
		double[] alpha = {2.1,2.2,2.3,2.4,2.5,2.6,2.7,2.8,2.9};
		int[] nodes = {1000,10000,100000};
		int d = 1;
		for (int a = 0; a < alpha.length; a++){
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter("data/SFRG/alpha="+alpha[a]+"d="+d));
				bw.write("reset"); bw.newLine();
				bw.write("set terminal png"); bw.newLine();
				bw.write("set output 'plots/alpha="+alpha[a]+"d="+d+".png'"); bw.newLine();
				String line = "plot ";
				for (int j = 0; j < nodes.length; j++){
					double av = 0;
			    	for (int k = d; k < nodes[j]; k++){
			    		av = av + Math.pow(k, 1-alpha[a]);
			    	}
					line = line + "'POWER_LAW_RANDOM-"+nodes[j]+"-"+alpha[a]+"-"+d+"-"+nodes[j]+"-false/FRAGMENTATION-WEAK-RANDOM-PERCENT/largestComponentSize-fraction.txt' " +
							"using 1:2 title 'SF, n="+nodes[j]+"' with points, " +
							"'ERDOS_RENYI-"+nodes[j]+"-"+av+"-true/FRAGMENTATION-WEAK-RANDOM-PERCENT/largestComponentSize-fraction.txt' " +
							"using 1:2 title 'Random, n="+nodes[j]+"' with points";
					if (j < nodes.length-1){
						line = line + ",";
					}
				}
				bw.write(line); bw.newLine();
				bw.flush();
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void  mainSF(String[] args){
		Config.overwrite("MAIN_DATA_FOLDER", "data/SFRG/");
		int[] nodes = {1000,10000,100000};
		double alpha = Double.parseDouble(args[0]);
		int initDegree = Integer.parseInt(args[1]);
		int times = Integer.parseInt(args[2]);
		Metric[] metrics = new Metric[] {new DegreeDistribution(), new WeakFragmentation(new RandomNodeSorter(),Fragmentation.Resolution.PERCENT)};
        for (int i = 0; i < nodes.length; i++){
        	Network sf = new PowerLawRandomGraph(nodes[i],alpha,initDegree,nodes[i],false,null);
        	Series.generate(sf, metrics, times);
        	double av = 0;
        	for (int j = initDegree; j < nodes[i]; j++){
        		av = av + Math.pow(j, 1-alpha);
        	}
        	Network er = new ErdosRenyi(nodes[i],av,true,null);
        	Series.generate(er, metrics, times);
        }
	}
	
	public static double[] computeResilience(double alpha, int initDeg, int n){
		double prob = 0;
		for (int i = initDeg; i < n+1; i++){
			prob = prob + Math.pow(i, -alpha);
		}
		double c = 1/prob;
		double exp = 0;
		for (int i = initDeg; i < n+1; i++){
			exp = exp + Math.pow(i, -alpha+1);
		}
		exp = exp*c;
		
		double secMom = 0;
		for (int i = initDeg; i < n+1; i++){
			secMom = secMom + Math.pow(i, -alpha+2);
		}
		secMom = secMom*c;
		//System.out.println("secMom =" + secMom + " exp=" +exp);
		double sfpc = (secMom/exp-2)/(secMom/exp-1);
		double rpc = (exp+1-2)/(exp+1-1);
		return new double[]{sfpc,rpc};
	}

}
  