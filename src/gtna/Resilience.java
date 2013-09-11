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
import gtna.graph.sorting.DegreeNodeSorter;
import gtna.graph.sorting.DegreeNodeSorterUpdate;
import gtna.graph.sorting.EigenvectorCentralityNSUpdate;
import gtna.graph.sorting.EigenvectorCentralityNodeSorter;
import gtna.graph.sorting.NodeSorter.NodeSorterMode;
import gtna.metrics.Metric;
import gtna.metrics.fragmentation.FragmentationRecompute;
import gtna.metrics.fragmentation.StrongFragmentationRecompute;
import gtna.networks.Network;
import gtna.networks.model.randomGraphs.PowerLawRandomGraph;
import gtna.util.Config;

import java.util.Random;

/**
 * @author stef
 *
 */
public class Resilience {
	
	public static void main(String[] args){
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		int n = 100;
		Network net = new PowerLawRandomGraph(n,2.3,0.0,1,1,n,n,null);
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
								FragmentationRecompute.Resolution.SINGLE, false)}; 
		Series.generate(net, m, 1);
		
//		Graph g = net.generate();
//		printCCNonIterative(g);
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
