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
 * MainDegreeCC.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: stefanie;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.sorting.CCSorterItertively;
import gtna.graph.sorting.DegreeNodeSorter;
import gtna.graph.sorting.NodeSorter;
import gtna.networks.Network;
import gtna.networks.model.randomGraphs.PowerLawRandomGraph;

import java.util.Random;

/**
 * @author stefanie
 *
 */
public class MainDegreeCC {
	
	public static void main(String[] args) {
		int n = 100;
		Network net = new PowerLawRandomGraph(n,2.3,0,1,1,n,n,null);
		Graph g = net.generate();
		NodeSorter sortCC = new CCSorterItertively(false);
		NodeSorter sortDeg = new DegreeNodeSorter(NodeSorter.NodeSorterMode.DESC);
		double c1D = 1; double c2D = 1;
		double c1C = 1; double c2C = 1;
		double e2 = 0; double e = 0;
		Node[] nodeC = sortCC.sort(g, new Random());
		Node[] nodeD = sortDeg.sort(g, new Random());
		for (int i = 0; i < nodeC.length; i++){
			e2 = e2 + nodeC[i].getInDegree()*nodeC[i].getOutDegree();
			e = e + nodeC[i].getInDegree();
		}
		double f = 1/(double)n;
		double c3D = (e2-e)*f; double c3C = c3D;
		for (int i = 0; i < nodeC.length; i++){
			c3D = c3D - nodeD[i].getInDegree()*nodeD[i].getOutDegree()*f;
			c1D = c1D - nodeD[i].getInDegree()/e*f;
			c2D = c2D - nodeD[i].getOutDegree()/e*f;
			c3C = c3C - nodeC[i].getInDegree()*nodeC[i].getOutDegree()*f;
			c1C = c1C - nodeC[i].getInDegree()/e*f;
			c2C = c2C - nodeC[i].getOutDegree()/e*f;
			double cD = n/(double)(n-i)*(c1D*c2D*c3D);
			double cC = n/(double)(n-i)*(c1C*c2C*c3C);
			if (cD < cC){
				System.out.println("Attention " + i + " " +cD + " " + cC);
			}
		} 
	}

}
