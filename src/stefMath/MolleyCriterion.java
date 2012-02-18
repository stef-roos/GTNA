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
 * MolleyCriterion.java
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
package stefMath;

import gtna.io.DataReader;

import java.io.File;

/**
 * @author stef
 *
 */
public class MolleyCriterion {
	
	public static void main(String[] args){
		double alpha = 2.5;
		double[] dist = new double[100];
		double norm = 0;
		for (int i = 1; i < dist.length; i++){
			dist[i] = Math.pow(i, -alpha);
			norm = norm + dist[i];
		}
		for (int i = 1; i < dist.length; i++){
			dist[i] = dist[i]/norm;
		}
		System.out.println(getLargestDeletionPerc(dist));
//		int[] counts = {10000,20000,30000,40000,60000,80000};
//		for (int i = 0; i < counts.length; i++){
//			String folder = "data/"+counts[i];
//			String[] files = ((new File(folder))).list();
//			for (int j = 0; j < files.length; j++){
//				if (files[j].contains("-2-")){
//					double random = 0;
//					double largest = 0;
//					for (int c = 0; c < 30; c++){
//						String adress = folder + "/"+files[j]+"/"+c + "/data/dd-inDegreeDistribution.txt";
//						double[] prob = DataReader.readDouble(adress);
//						random = random + getRandomDeletionPerc(prob);
//						largest = largest + getLargestDeletionPerc(prob);
//					}
//					random = random/30;
//					largest = largest/30;
//					System.out.println(files[j]);
//					System.out.println(random);
//					System.out.println(largest);
//					
//				}
//			}
//		}
	}
	
	public static double getRandomDeletionPerc(double[] dist){
		double k0 = getk0(dist);
		double r = 1 - 1/(k0-1);
		if (r < 0){
			return 0;
		}else {
			return r;
		}
	}
	
	
	private static double getk0(double[] dist, int bound){
		double firstMoment = 0;
		double secondMoment = 0;
		for (int i = 0; i < Math.min(dist.length,bound+1); i++){
			firstMoment = firstMoment+ i*dist[i];
			secondMoment = secondMoment+ i*i*dist[i];
		}
		return secondMoment/firstMoment;
	}
	
	private static double getk0(double[] dist){
		return getk0(dist,dist.length);
	}
	
	public static double getLargestDeletionPerc(double[] dist){
		double k0 = getk0(dist);
		double av = 0;
		for (int i = 1; i < dist.length; i++){
			av = av + dist[i]*i;
		}
		double r = 1 - 1/(k0-1);
		if (r < 0){
			return 0;
		}else {
			double p = 0;
			double[] distN = new double[dist.length];
			double edgef = 0;
			double fpN;
			for (int j = dist.length-1; j > 0; j--){
				p = p + dist[j];
				edgef = edgef + dist[j]*j/(av);
				for (int i = 0; i < j; i++){
					distN[i] = dist[i]/(1-p);
				}
               for (int i = j; i < dist.length; i++){
            	   distN[i] = 0;
               }
			   k0 = getk0(distN,j-1);
			   fpN = 1 - 1/(k0-1);
			   System.out.println( j + " p=" + p);
			   System.out.println( j + " fpN=" + fpN);
			   System.out.println( j + " ep=" + edgef);
				if (edgef > fpN){
					System.out.println( "Break");
					//return p;
				}
			}
		}
		return 1;
	}

}
