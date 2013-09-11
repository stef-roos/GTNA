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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author stefanie
 *
 */
public class MainDegreeCC {
	
	public static void main(String[] args) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("relation.txt"));
			BufferedReader br = new BufferedReader(new FileReader("criterion.txt"));
			String line;
			int c = 0;
			while ((line = br.readLine()) != null){
				String[] parts = line.split(" ");
				double x = Double.parseDouble(parts[0]);
				double y = Double.parseDouble(parts[1]);
				bw.write(c + " " + parts[0] + " " + parts[1] + " " + y/x + " " + (y-x));
				c++;
				bw.newLine();
			}
			br.close();
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		int n = 50;
//		Network net = new PowerLawRandomGraph(n,2.3,0,1,1,n,n,null);
//		try {
//			
//		Graph g = net.generate();
//		NodeSorter sortCC = new CCSorterItertively(false);
//		NodeSorter sortDeg = new DegreeNodeSorter(NodeSorter.NodeSorterMode.DESC);
//		double c1D = 1; double c2D = 1;
//		double c1C = 1; double c2C = 1;
//		double e2 = 0; double e = 0;
//		Node[] nodeC = sortCC.sort(g, new Random());
//		Node[] nodeD = sortDeg.sort(g, new Random());
//		int maxIn = 0;
//		int maxOut = 0;
//		for (int i = 0; i < nodeC.length; i++){
//			e2 = e2 + nodeC[i].getInDegree()*nodeC[i].getOutDegree();
//			e = e + nodeC[i].getInDegree();
//			if ( nodeC[i].getInDegree() > maxIn){
//				maxIn = nodeC[i].getInDegree();
//			}
//			if ( nodeC[i].getOutDegree() > maxOut){
//				maxOut = nodeC[i].getOutDegree();
//			}
//		}
//		
//		double[][] degs = new double[maxIn+1][maxOut+1];
//		for (int i = 0; i < nodeC.length; i++){
//			degs[nodeC[i].getInDegree()][nodeC[i].getOutDegree()]++;
//		}
//        BufferedWriter bw = new BufferedWriter(new FileWriter("dd.txt"));
//       double f = 1/(double)n;
//       e2 = e2*f;
//		e = e*f;
//       String line = " ";
//       for (int i = 0; i < degs[0].length; i++){
//    	   line = line + " " + i;
//       }
//       bw.write(line);
//		for (int j=0; j < degs.length; j++){
//			line = ""+j;
//			for (int k = 0; k < degs[j].length; k++){
//				line = line + " " +degs[j][k];
//			}
//			bw.newLine();
//			bw.write(line);
//		}
//		bw.flush();
//		bw.close();
//		bw = new BufferedWriter(new FileWriter("criterion.txt"));
//		double c3D = e2-e; double c3C = c3D;
//		bw.write(c3C + " " + c3D);
//		for (int i = 0; i < nodeC.length; i++){
//			c3D = c3D - nodeD[i].getInDegree()*nodeD[i].getOutDegree()*f;
//			c1D = c1D - nodeD[i].getInDegree()/e*f;
//			c2D = c2D - nodeD[i].getOutDegree()/e*f;
//			c3C = c3C - nodeC[i].getInDegree()*nodeC[i].getOutDegree()*f;
//			c1C = c1C - nodeC[i].getInDegree()/e*f;
//			c2C = c2C - nodeC[i].getOutDegree()/e*f;
//			double cD = n/(double)(n-i)*(c1D*c2D*c3D);
//			double cC = n/(double)(n-i)*(c1C*c2C*c3C);
//			bw.newLine();
//			bw.write(cC + " " + cD + " ("+nodeC[i].getInDegree() + ","+nodeC[i].getOutDegree()+ " )" 
//			+ " ("+nodeD[i].getInDegree() + ","+nodeD[i].getOutDegree()+ " )");
//			if (cD < cC){
//				System.out.println("Attention " + i + " " +cD + " " + cC);
//			}
//		} 
//		bw.flush();
//		bw.close();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
	}
	
	 

}
