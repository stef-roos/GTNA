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
 * PETs.java
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
package computations.conferences;

import gtna.data.Series;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.routing.Routing;
import gtna.networks.Network;
import gtna.networks.model.smallWorld.KleinbergDegreeDist;
import gtna.routing.greedy.Greedy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author stef
 *
 */
public class PETs {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int k = Integer.parseInt(args[0]);
        double[] d = getDegreeDist(args[1],false,100);
        for (int i = 1; i <= 2*k;i++){
        	d[0] = d[0] + d[i];
        }
        for (int i = 1; i < d.length-2*k; i++){
        	d[i] = d[i+2*k];
        }
        for (int i = d.length-2*k; i < d.length; i++){
        	d[i] = 0;
        }
        Network n0 = new KleinbergDegreeDist(15000,k,d,args[2],0,false,true,null);
        Metric[] m = new Metric[]{new DegreeDistribution(),new Routing(new Greedy())};
        Series.generate(n0, m, 30);
        Network n1 = new KleinbergDegreeDist(15000,k,d,args[2],1,false,true,null);
        Series.generate(n1, m, 30);
	}
	
	
	private static double[] getDegreeDist(String file, boolean dist, int max){
		try {
			double[] d = new double[max+1];
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null){
				String[] parts = line.split(" ");
				if (parts.length < 2) continue;
				d[Integer.parseInt(parts[0])] = Double.parseDouble(parts[1]);
			}
			br.close();
			if (!dist){
				double sum = 0;
				for (int i = 0; i < d.length; i++){
					sum = sum + d[i];
				}
				for (int i = 0; i < d.length; i++){
					d[i] = d[i]/sum;
				}
			}
			return d;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
