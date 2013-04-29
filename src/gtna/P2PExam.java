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
 * P2PExam.java
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
import gtna.networks.model.smallWorld.Kleinberg1D;
import gtna.networks.p2p.chord.Chord;
import gtna.networks.p2p.chord.Chord.IDSelection;
import gtna.networks.p2p.kademlia.Kademlia;
import gtna.networks.p2p.pastry.Pastry;
import gtna.util.Config;

import java.util.Arrays;
import java.util.Random;

/**
 * @author stef
 *
 */
public class P2PExam {
	
	public static void main(String[] args){
		int[] ids = getIDs(40,8);
		Arrays.sort(ids);
		//write in Hex
		for (int i=0; i < 3; i++){
			String line = "Node ";
			String lineid = "ID";
			for (int j = 0; j < 15; j++){
				if (i*15+j < 40){
				line = line + " & " + (i*15+j);
				lineid = lineid + " & " + leadingZeros(Integer.toHexString(ids[i*15+j]),2);
				} else {
					line = line + " & " ;
					lineid = lineid + " & " ;
				}
			}
			//System.out.println(line + " \\\\");
			System.out.println(lineid + " \\\\ \\hline");
		}
		
		System.out.println();
		for (int i=0; i < 5; i++){
			String line = "Node ";
			String lineid = "ID";
			for (int j = 0; j < 8; j++){
				line = line + " & " + (i*8+j);
				lineid = lineid + " & " + leadingZeros(Integer.toBinaryString(ids[i*8+j]),8);
			}
			//System.out.println(line + " \\\\");
			System.out.println(lineid + " \\\\ \\hline");
		}
	}
	
	private static String leadingZeros(String nr, int l){
		while (nr.length() < l){
			nr =  "0" + nr;
		}
		return nr;
	}
	
	public static int[] getIDs(int nr, int bits){
		int[] ids = new int[nr];
		int max = (int)Math.pow(2, bits);
		Random rand = new Random();
		for (int i = 0; i < nr; i++){
			ids[i] = rand.nextInt(max);
		}
		return ids;
	}
	
	public static void graphs(){
		Metric[] m = new Metric[]{new DegreeDistribution()};
		Config.overwrite("MAIN_DATA_FOLDER", "data/exam/");
		Network chord = new Chord(1000,128,IDSelection.RANDOM, null);
		Series.generate(chord, m, 10);
		Network pastry = new Pastry(1000,128,4,0,IDSelection.RANDOM, null);
		Series.generate(pastry, m, 10);
		Network kademlia = new Kademlia(1000,128,16,IDSelection.RANDOM, null);
		Series.generate(kademlia, m, 10);
		Network kademlia2 = new Kademlia(1000,128,2,IDSelection.RANDOM, null);
		Series.generate(kademlia2, m, 10);
	}
	
	

}
