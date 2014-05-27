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
 * DegreeRemoval.java
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author stef
 *
 */
public class DegreeRemoval {
	
	
	public static void writeLog(double[] dist, String file){
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			String line;
			for (int i = 1; i < dist.length; i++){
				if (dist[i] != 0){
				double d = Math.log(dist[i]);
				bw.write(i + " " +d);
				bw.newLine();
				}
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static double[] getScaleFree(int n, double alpha, int min){
		double[] dist = new double[n+1];
		double s = 0;
		for (int i = min; i < dist.length; i++){
			dist[i] = Math.pow(i, -alpha);
			s = s + dist[i];
		}
		for (int i = min; i < dist.length; i++){
			dist[i] = dist[i]/s;
		}
		return dist;
	}
	
	public static double[] removeRandom(double[] dist, double p){
		double[] distN = new double[dist.length];
		for (int i = 0; i < dist.length; i++){
			double binom = 1*Math.pow(1-p, i);
			for (int j = i; j < dist.length; j++){
		    	distN[i] = distN[i] + dist[i]*binom;
		    	if (binom == 0){
		    		binom = Calc.binomDist(j+1, i, 1-p);
		    	} else {
		    		binom = binom*p*(j+1)/(double)(j+1-i);
		    	}
		    }
		}
		return distN;
	}
	
	public static double[] getDegreeFile(String file, int max){
		double[] dist = new double[max+1];
		try{
		BufferedReader br = new  BufferedReader(new FileReader(file));
		String line;
		while ((line =br.readLine()) != null){
			String[] parts = line.split(" ");
			if (parts.length == 2){
				dist[Integer.parseInt(parts[0])] = Double.parseDouble(parts[1]);
			}
		}
		} catch(IOException e){
			
		}
		return dist;
	}

	public static double[] expand(double[] array, int size) {
	    double[] temp = new double[size];
	    System.arraycopy(array, 0, temp, 0, array.length);
	    for(int j = array.length; j < size; j++)
	        temp[j] = 0;
	    return temp;
	}
}
