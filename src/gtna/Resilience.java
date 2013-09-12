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
 * Resilience.java
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
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.sorting.BetweennessCentralityNSUpdate;
import gtna.graph.sorting.CCSorterGreedy;
import gtna.graph.sorting.CCSorterItertively;
import gtna.graph.sorting.CCSorterUpdate;
import gtna.graph.sorting.CCSorterUpdate.Computation;
import gtna.graph.sorting.CentralityNodeSorter;
import gtna.graph.sorting.CentralityNodeSorter.CentralityMode;
import gtna.graph.sorting.ClosenessCentralityNSUpdate;
import gtna.graph.sorting.ClosenessCentralityNodeSorter;
import gtna.graph.sorting.ConnectivityNSUpdate;
import gtna.graph.sorting.ConnectivityNodeSorter;
import gtna.graph.sorting.DegreeNodeSorter;
import gtna.graph.sorting.DegreeNodeSorterUpdate;
import gtna.graph.sorting.EigenvectorCentralityNSUpdate;
import gtna.graph.sorting.EigenvectorCentralityNodeSorter;
import gtna.graph.sorting.NodeSorter.NodeSorterMode;
import gtna.graph.sorting.RandomNodeSorter;
import gtna.metrics.Metric;
import gtna.metrics.fragmentation.Fragmentation;
import gtna.metrics.fragmentation.FragmentationRecompute;
import gtna.metrics.fragmentation.StrongFragmentationRecompute;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.model.randomGraphs.PowerLawRandomGraph;
import gtna.util.Config;

import java.util.Random;

/**
 * @author stef
 *
 */
public class Resilience {
	
	public static void main(String[] args){
		int mode = Integer.parseInt(args[0]);
		int n = Integer.parseInt(args[1]);
		int times = Integer.parseInt(args[2]);
		double d = Double.parseDouble(args[3]);
		switch (mode){
		case 1: ERU(n,d,times); break;
		case 2: ERD(n,d,times); break;
		case 3: PLU(n,d,times); break;
		case 4: double a2 = Integer.parseInt(args[4]);
		        PLD(n,d,a2,times);
		}
		
		
//		Graph g = net.generate();
//		printCCNonIterative(g);
	}
	
	public static void ERU(int n, double d, int times){
		Network net = new ErdosRenyi(n,d,true,null);
		Metric[] m = new Metric[]{
		new StrongFragmentationRecompute(new BetweennessCentralityNSUpdate(), 
				FragmentationRecompute.Resolution.SINGLE, true),
		new StrongFragmentationRecompute(new CCSorterGreedy(true,CCSorterGreedy.Computation.DEGREEBASED), 
						FragmentationRecompute.Resolution.SINGLE, true),
		new StrongFragmentationRecompute(new CCSorterGreedy(true,CCSorterGreedy.Computation.NODEBASED), 
		FragmentationRecompute.Resolution.SINGLE, true),
		new StrongFragmentationRecompute(new CCSorterItertively(true,CCSorterItertively.Computation.DEGREEBASED), 
				FragmentationRecompute.Resolution.SINGLE, true),
		new StrongFragmentationRecompute(new CCSorterItertively(true,CCSorterItertively.Computation.NODEBASED), 
						FragmentationRecompute.Resolution.SINGLE, true),
		new StrongFragmentationRecompute(new CCSorterUpdate(true,CCSorterUpdate.Computation.DEGREEBASED), 
								FragmentationRecompute.Resolution.SINGLE, true),
		new StrongFragmentationRecompute(new CCSorterUpdate(true,CCSorterUpdate.Computation.NODEBASED), 
										FragmentationRecompute.Resolution.SINGLE, true),
		new StrongFragmentationRecompute(new CentralityNodeSorter(CentralityMode.BETWEENNESS,NodeSorterMode.DESC), 
												FragmentationRecompute.Resolution.SINGLE, true),
		new StrongFragmentationRecompute(new ClosenessCentralityNodeSorter(NodeSorterMode.DESC), 
														FragmentationRecompute.Resolution.SINGLE, true),
		new StrongFragmentationRecompute(new ClosenessCentralityNSUpdate(), 
																FragmentationRecompute.Resolution.SINGLE, true),
		new StrongFragmentationRecompute(new DegreeNodeSorter(NodeSorterMode.DESC),FragmentationRecompute.Resolution.SINGLE, true),
		new StrongFragmentationRecompute(new DegreeNodeSorterUpdate(NodeSorterMode.DESC), FragmentationRecompute.Resolution.SINGLE, true),
		new StrongFragmentationRecompute(new EigenvectorCentralityNodeSorter(NodeSorterMode.DESC), 
				FragmentationRecompute.Resolution.SINGLE, true),
				new StrongFragmentationRecompute(new EigenvectorCentralityNSUpdate(), 
						FragmentationRecompute.Resolution.SINGLE, true),
						new StrongFragmentationRecompute(new ConnectivityNodeSorter(Fragmentation.Type.WEAK), 
								FragmentationRecompute.Resolution.SINGLE, true),
								new StrongFragmentationRecompute(new ConnectivityNSUpdate(Fragmentation.Type.WEAK), 
										FragmentationRecompute.Resolution.SINGLE, true),
		new StrongFragmentationRecompute(new RandomNodeSorter(), 
				FragmentationRecompute.Resolution.SINGLE, true)}; 
		Series.generate(net, m, times);
	}
	
	public static void PLU(int n, double alpha, int times){
		Network net = new PowerLawRandomGraph(n,alpha,1,n,true,null);
		Metric[] m = new Metric[]{
		new StrongFragmentationRecompute(new BetweennessCentralityNSUpdate(), 
				FragmentationRecompute.Resolution.SINGLE, true),
		new StrongFragmentationRecompute(new CCSorterGreedy(true,CCSorterGreedy.Computation.DEGREEBASED), 
						FragmentationRecompute.Resolution.SINGLE, true),
		new StrongFragmentationRecompute(new CCSorterGreedy(true,CCSorterGreedy.Computation.NODEBASED), 
		FragmentationRecompute.Resolution.SINGLE, true),
		new StrongFragmentationRecompute(new CCSorterItertively(true,CCSorterItertively.Computation.DEGREEBASED), 
				FragmentationRecompute.Resolution.SINGLE, true),
		new StrongFragmentationRecompute(new CCSorterItertively(true,CCSorterItertively.Computation.NODEBASED), 
						FragmentationRecompute.Resolution.SINGLE, true),
		new StrongFragmentationRecompute(new CCSorterUpdate(true,CCSorterUpdate.Computation.DEGREEBASED), 
								FragmentationRecompute.Resolution.SINGLE, true),
		new StrongFragmentationRecompute(new CCSorterUpdate(true,CCSorterUpdate.Computation.NODEBASED), 
										FragmentationRecompute.Resolution.SINGLE, true),
		new StrongFragmentationRecompute(new CentralityNodeSorter(CentralityMode.BETWEENNESS,NodeSorterMode.DESC), 
												FragmentationRecompute.Resolution.SINGLE, true),
		new StrongFragmentationRecompute(new ClosenessCentralityNodeSorter(NodeSorterMode.DESC), 
														FragmentationRecompute.Resolution.SINGLE, true),
		new StrongFragmentationRecompute(new ClosenessCentralityNSUpdate(), 
																FragmentationRecompute.Resolution.SINGLE, true),
		new StrongFragmentationRecompute(new DegreeNodeSorter(NodeSorterMode.DESC),FragmentationRecompute.Resolution.SINGLE, true),
		new StrongFragmentationRecompute(new DegreeNodeSorterUpdate(NodeSorterMode.DESC), FragmentationRecompute.Resolution.SINGLE, true),
		new StrongFragmentationRecompute(new EigenvectorCentralityNodeSorter(NodeSorterMode.DESC), 
				FragmentationRecompute.Resolution.SINGLE, true),
				new StrongFragmentationRecompute(new EigenvectorCentralityNSUpdate(), 
						FragmentationRecompute.Resolution.SINGLE, true),new StrongFragmentationRecompute(new ConnectivityNodeSorter(Fragmentation.Type.WEAK), 
								FragmentationRecompute.Resolution.SINGLE, true),
								new StrongFragmentationRecompute(new ConnectivityNSUpdate(Fragmentation.Type.WEAK), 
										FragmentationRecompute.Resolution.SINGLE, true),
		new StrongFragmentationRecompute(new RandomNodeSorter(), 
				FragmentationRecompute.Resolution.SINGLE, true)}; 
		Series.generate(net, m, times);
	}
	
	public static void ERD(int n, double d, int times){
		Network net = new ErdosRenyi(n,d,false,null);
		Metric[] m = new Metric[]{
		new StrongFragmentationRecompute(new BetweennessCentralityNSUpdate(), 
				FragmentationRecompute.Resolution.SINGLE, false),
		new StrongFragmentationRecompute(new CCSorterGreedy(false,CCSorterGreedy.Computation.DEGREEBASED), 
						FragmentationRecompute.Resolution.SINGLE, false),
		new StrongFragmentationRecompute(new CCSorterGreedy(false,CCSorterGreedy.Computation.NODEBASED), 
		FragmentationRecompute.Resolution.SINGLE, false),
		new StrongFragmentationRecompute(new CCSorterItertively(false,CCSorterItertively.Computation.DEGREEBASED), 
				FragmentationRecompute.Resolution.SINGLE, false),
		new StrongFragmentationRecompute(new CCSorterItertively(false,CCSorterItertively.Computation.NODEBASED), 
						FragmentationRecompute.Resolution.SINGLE, false),
		new StrongFragmentationRecompute(new CCSorterUpdate(false,CCSorterUpdate.Computation.DEGREEBASED), 
								FragmentationRecompute.Resolution.SINGLE, false),
		new StrongFragmentationRecompute(new CCSorterUpdate(false,CCSorterUpdate.Computation.NODEBASED), 
										FragmentationRecompute.Resolution.SINGLE, false),
		new StrongFragmentationRecompute(new CentralityNodeSorter(CentralityMode.BETWEENNESS,NodeSorterMode.DESC), 
												FragmentationRecompute.Resolution.SINGLE, false),
		new StrongFragmentationRecompute(new ClosenessCentralityNodeSorter(NodeSorterMode.DESC), 
														FragmentationRecompute.Resolution.SINGLE, false),
		new StrongFragmentationRecompute(new ClosenessCentralityNSUpdate(), 
																FragmentationRecompute.Resolution.SINGLE, false),
		new StrongFragmentationRecompute(new DegreeNodeSorter(NodeSorterMode.DESC),FragmentationRecompute.Resolution.SINGLE, false),
		new StrongFragmentationRecompute(new DegreeNodeSorterUpdate(NodeSorterMode.DESC), FragmentationRecompute.Resolution.SINGLE, false),
		new StrongFragmentationRecompute(new EigenvectorCentralityNodeSorter(NodeSorterMode.DESC), 
				FragmentationRecompute.Resolution.SINGLE, false),
				new StrongFragmentationRecompute(new EigenvectorCentralityNSUpdate(), 
						FragmentationRecompute.Resolution.SINGLE, false), new StrongFragmentationRecompute(new ConnectivityNodeSorter(Fragmentation.Type.WEAK), 
								FragmentationRecompute.Resolution.SINGLE, false),
								new StrongFragmentationRecompute(new ConnectivityNSUpdate(Fragmentation.Type.WEAK), 
										FragmentationRecompute.Resolution.SINGLE, false),
		new StrongFragmentationRecompute(new RandomNodeSorter(), 
				FragmentationRecompute.Resolution.SINGLE, false)}; 
		Series.generate(net, m, times);
	}
	
	public static void PLD(int n, double alpha1,double alpha2, int times){
		Network net = new PowerLawRandomGraph(n,alpha1,alpha2,1,1,n,n,null);
		Metric[] m = new Metric[]{
		new StrongFragmentationRecompute(new BetweennessCentralityNSUpdate(), 
				FragmentationRecompute.Resolution.SINGLE, false),
		new StrongFragmentationRecompute(new CCSorterGreedy(false,CCSorterGreedy.Computation.DEGREEBASED), 
						FragmentationRecompute.Resolution.SINGLE, false),
		new StrongFragmentationRecompute(new CCSorterGreedy(false,CCSorterGreedy.Computation.NODEBASED), 
		FragmentationRecompute.Resolution.SINGLE, false),
		new StrongFragmentationRecompute(new CCSorterItertively(false,CCSorterItertively.Computation.DEGREEBASED), 
				FragmentationRecompute.Resolution.SINGLE, false),
		new StrongFragmentationRecompute(new CCSorterItertively(false,CCSorterItertively.Computation.NODEBASED), 
						FragmentationRecompute.Resolution.SINGLE, false),
		new StrongFragmentationRecompute(new CCSorterUpdate(false,CCSorterUpdate.Computation.DEGREEBASED), 
								FragmentationRecompute.Resolution.SINGLE, false),
		new StrongFragmentationRecompute(new CCSorterUpdate(false,CCSorterUpdate.Computation.NODEBASED), 
										FragmentationRecompute.Resolution.SINGLE, false),
		new StrongFragmentationRecompute(new CentralityNodeSorter(CentralityMode.BETWEENNESS,NodeSorterMode.DESC), 
												FragmentationRecompute.Resolution.SINGLE, false),
		new StrongFragmentationRecompute(new ClosenessCentralityNodeSorter(NodeSorterMode.DESC), 
														FragmentationRecompute.Resolution.SINGLE, false),
		new StrongFragmentationRecompute(new ClosenessCentralityNSUpdate(), 
																FragmentationRecompute.Resolution.SINGLE, false),
		new StrongFragmentationRecompute(new DegreeNodeSorter(NodeSorterMode.DESC),FragmentationRecompute.Resolution.SINGLE, false),
		new StrongFragmentationRecompute(new DegreeNodeSorterUpdate(NodeSorterMode.DESC), FragmentationRecompute.Resolution.SINGLE, false),
		new StrongFragmentationRecompute(new EigenvectorCentralityNodeSorter(NodeSorterMode.DESC), 
				FragmentationRecompute.Resolution.SINGLE, false),
				new StrongFragmentationRecompute(new EigenvectorCentralityNSUpdate(), 
						FragmentationRecompute.Resolution.SINGLE, false), new StrongFragmentationRecompute(new ConnectivityNodeSorter(Fragmentation.Type.WEAK), 
								FragmentationRecompute.Resolution.SINGLE, false),
								new StrongFragmentationRecompute(new ConnectivityNSUpdate(Fragmentation.Type.WEAK), 
										FragmentationRecompute.Resolution.SINGLE, false),
		new StrongFragmentationRecompute(new RandomNodeSorter(), 
				FragmentationRecompute.Resolution.SINGLE, false)}; 
		Series.generate(net, m, times);
	}
	
	private static void printCCNonIterative(Graph g){
//		int maxIn = 0;
//		int maxOut = 0;
//		Node[] nodes = g.getNodes();
//		for (int i = 0; i < nodes.length; i++){
//			if (nodes[i].getInDegree() > maxIn){
//				maxIn = nodes[i].getInDegree();
//			}
//			if (nodes[i].getOutDegree() > maxOut){
//				maxIn = nodes[i].getOutDegree();
//			}
//		}
		Node[] sortedDegree = (new DegreeNodeSorterUpdate(NodeSorterMode.DESC)).sort(g, new Random());
		Node[] sortedCC = (new CCSorterUpdate(false,Computation.DEGREEBASED)).sort(g, new Random());
		double c1degree = 1; double c2degree = 1;
		double c1cc = 1; double c2cc = 1;
		double c3degree = edpdm(sortedCC) - ex(sortedCC); double c3cc = c3degree;
		double f = 1/(double)sortedCC.length;
		double ex = ex(sortedCC);
		for (int i = 0; i < sortedDegree.length; i++){
			c3degree = c3degree - sortedDegree[i].getInDegree()*sortedDegree[i].getOutDegree()*f;
			c3cc = c3cc - sortedCC[i].getInDegree()*sortedCC[i].getOutDegree()*f;
			c1degree = c1degree - sortedDegree[i].getInDegree()/ex*f;
			c1cc = c1cc - sortedCC[i].getInDegree()/ex*f;
			c2degree = c2degree - sortedDegree[i].getOutDegree()/ex*f;
			c2cc = c2cc - sortedCC[i].getOutDegree()/ex*f;
			double cdegree = (c1degree*c2degree*c3degree);
			double ccc = (c1cc*c2cc*c3cc);
			String s = ccc + " removed ("+ sortedCC[i].getInDegree()+","+sortedCC[i].getOutDegree()+") " 
			+ cdegree + " removed ("+ sortedDegree[i].getInDegree()+","+sortedDegree[i].getOutDegree()+")";
			if (ccc <= cdegree){
			System.out.println(s);
			} else {
				System.out.println(s + " ATTTENTION");
			}
		}
	   
	}	
	
	private static double edpdm(Node[] sorted){
		double e = 0;
		for (int i = 0; i < sorted.length; i++){
			e = e + sorted[i].getInDegree()*sorted[i].getOutDegree();
		}
		return e/(double)sorted.length;
	}
	
	private static double ex(Node[] sorted){
		double e = 0;
		for (int i = 0; i < sorted.length; i++){
			e = e + sorted[i].getInDegree();
		}
		return e/(double)sorted.length;
	}
	
	

}
