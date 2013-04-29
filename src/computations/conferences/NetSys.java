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
 * NetSys.java
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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import computations.evaluation.GetValuesGTNA;

/**
 * @author stef
 *
 */
public class NetSys {
	
	public static void main(String[] args){
		String[] f = new String[10];
		String[] nf = new String[10];
		int[] bits = {4,5,6,7, Integer.MAX_VALUE};
		String[] tree = {"BFS-RAND", "BFS-HD", "FAT" };
		String[] tree2 = {"", "BFS-HD-", "FAT-" };
		int[] mode = {1,2,2};
		String[] type = {"BFS_TREE", "BFS_TREE", "FAT_TREE"};
		for (int i =0; i < 5; i++){
			f[i] = "netsys/READABLE_FILE_PREFIX-"+tree[0]+"-33317--RESTRICTED_"+ type[0]+"--"
						+mode[0]+"-"+((i<4)?(int)Math.pow(2, bits[i]):Integer.MAX_VALUE)+"--PREFIX_EMBEDDING-"+bits[i]+"-160/ROUTING-GREEDY-200/r-hopDistributionAbsolute-cdf.txt";
			nf[i] = "/home/stef/svns/GreedyDarknetEmbed-NETSYS13/imagesdata/degree2/ISOMETRIC-Bits="+bits[i]+".txt";
		}
		for (int i =0; i < 5; i++){
			f[5+i] = "netsys/READABLE_LIST_VIRTUAL-"+tree[0]+"-33317--REMOVE_GRAPH_PROPERTY--PREFIX_EMBEDDING_VIRTUAL-"
						+bits[i]+"-160/ROUTING-GREEDYTREE-200/r-hopDistributionAbsolute-cdf.txt";
			nf[5+i] = "/home/stef/svns/GreedyDarknetEmbed-NETSYS13/imagesdata/degree2/VIRTUAL-Bits="+bits[i]+".txt";
		}
//		f[4] = "netsys/READABLE_FILE_PREFIX-"+tree2[0]+"33317--RESTRICTED_"+ type[0]+"--"
//				+mode[0]+"-"+Integer.MAX_VALUE+"--PREFIX_EMBEDDING-"+bits[4]+"-160/ROUTING-GREEDY-200/r-hopDistributionAbsolute-cdf.txt";
//	    nf[4] = "/home/stef/svns/GreedyDarknetEmbed-NETSYS13/imagesdata/degree2/ISOMETRIC-Bits=Unlimited.txt";
//	    f[9] = "netsys/READABLE_LIST_VIRTUAL-"+tree2[0]+"33317--REMOVE_GRAPH_PROPERTY--PREFIX_EMBEDDING_VIRTUAL-"
//				+bits[4]+"-160/ROUTING-GREEDYTREE-200/r-hopDistributionAbsolute-cdf.txt";
//	nf[9] = "/home/stef/svns/GreedyDarknetEmbed-NETSYS13/imagesdata/degree2/VIRTUAL-Bits=Unlimited.txt";
		writeCPbash(f,nf,"degree2.sh");
		
		
		String[] t = new String[6];
		String[] nt = new String[6];
		for (int i =0; i < tree.length; i++){
			t[i] = "netsys/READABLE_FILE_PREFIX-"+tree[i]+"-33317--RESTRICTED_"+ type[i]+"--"
						+mode[i]+"-"+Integer.MAX_VALUE+"--PREFIX_EMBEDDING-"+bits[4]+"-160/ROUTING-GREEDY-200/r-hopDistributionAbsolute-cdf.txt";
			nt[i] = "/home/stef/svns/GreedyDarknetEmbed-NETSYS13/imagesdata/trees/ISOMETRIC-Tree="+tree[i]+".txt";
		}
		for (int i =0; i < tree.length; i++){
			t[3+i] = "netsys/READABLE_LIST_VIRTUAL-"+tree[i]+"-33317--REMOVE_GRAPH_PROPERTY--PREFIX_EMBEDDING_VIRTUAL-"
						+Integer.MAX_VALUE+"-160/ROUTING-GREEDYTREE-200/r-hopDistributionAbsolute-cdf.txt";
			nt[3+i] = "/home/stef/svns/GreedyDarknetEmbed-NETSYS13/imagesdata/trees/VIRTUAL-Tree="+tree[i]+".txt";
		}
		writeCPbash(t,nt,"tree.sh");
		loadbalancing();
	}
	
	public static void loadbalancing(){
		String folder = "netsys/";
		String[] metrics = {"PREFIX_S_IDENTIFIER_SPACE_LOAD_BALANCE-1024"};
		String[][] keys = {{"PREFIX_S_IDENTIFIER_SPACE_LOAD_BALANCE_MAX", "PREFIX_S_IDENTIFIER_SPACE_LOAD_BALANCE_AVG", "PREFIX_S_IDENTIFIER_SPACE_LOAD_BALANCE_MEDIAN",
			"PREFIX_S_IDENTIFIER_SPACE_LOAD_BALANCE_MINGREATERZERO", "PREFIX_S_IDENTIFIER_SPACE_LOAD_BALANCE_FRACTIONOFZEROES" }};
		
		String line = "Embedding & Tree & Bits ";
		for (int j = 0; j < keys[0].length; j++){
			line = line + " & " + keys[0][j];
		}
		System.out.println(line + " \\\\");
		//Prefix
		int[] bits = {4,5,6,7};
		String[] tree = {"BFS-RAND", "BFS-HD", "FAT" };
		int[] mode = {1,2,2};
		String[] type = {"BFS_TREE", "BFS_TREE", "FAT_TREE"};
		for (int i = 0; i < tree.length; i++){
			for (int j = 0; j < bits.length; j++){
				double[] res = GetValuesGTNA.getSingleValue(folder+ "READABLE_FILE_PREFIX-"+tree[i]+"-33317--RESTRICTED_"+ type[i]+"--"
						+mode[i]+"-"+(int)Math.pow(2, bits[j])+"--PREFIX_EMBEDDING-"+bits[j]+"-160/", metrics, keys);
				line = "PREFIX & " + tree[i] + " & " + bits[j];
				for (int h = 0; h < res.length; h++){
					line = line + " & " + Math.round(res[h]*100)/(double)100;
				}
				System.out.println(line + " \\\\");
			}
		}
		
		//Virtual
		int[] bits2 = {4,5,6,7 , Integer.MAX_VALUE};
		String[] tree2 = {"", "BFS-HD-", "FAT-" };
		for (int i = 0; i < tree.length; i++){
			for (int j = 0; j < bits2.length; j++){
				double[] res = GetValuesGTNA.getSingleValue(folder+ "READABLE_LIST_VIRTUAL-"+(j==5?tree2[i]:(tree[i]+"-"))+"33317--REMOVE_GRAPH_PROPERTY--PREFIX_EMBEDDING_VIRTUAL-"
						+bits2[j]+"-160/", metrics, keys);
				line = "VIRTUAL & " +tree[i] + " & " + bits2[j];
				for (int h = 0; h < res.length; h++){
					line = line + " & " + Math.round(res[h]*100)/(double)100;
				}
				System.out.println(line + " \\\\");
			}
		}
		
//		String folder = "netsys/";
//		String[] metrics = {"PREFIX_S_IDENTIFIER_SPACE_LOAD_BALANCE-1024"};
//		String[][] keys = {{"PREFIX_S_IDENTIFIER_SPACE_LOAD_BALANCE_MAX", "PREFIX_S_IDENTIFIER_SPACE_LOAD_BALANCE_AVG", "PREFIX_S_IDENTIFIER_SPACE_LOAD_BALANCE_MEDIAN",
//			"PREFIX_S_IDENTIFIER_SPACE_LOAD_BALANCE_MINGREATERZERO", "PREFIX_S_IDENTIFIER_SPACE_LOAD_BALANCE_FRACTIONOFZEROES" }};
//		
//		String line = "Embedding & Tree & Bits ";
//		for (int j = 0; j < keys[0].length; j++){
//			line = line + " & " + keys[0][j];
//		}
//		System.out.println(line + " \\\\");
//		//Prefix
//		int[] bits = {4,5,6,7};
//		String[] tree = {"BFS-RAND", "BFS-HD", "FAT" };
//		int[] mode = {1,2,2};
//		String[] type = {"BFS_TREE", "BFS_TREE", "FAT_TREE"};
//		for (int i = 0; i < tree.length; i++){
//			for (int j = 0; j < bits.length; j++){
//				double[] res = GetValuesGTNA.getSingleValue(folder+ "READABLE_FILE_PREFIX-"+tree[i]+"-33317--RESTRICTED_"+ type[i]+"--"
//						+mode[i]+"-"+(int)Math.pow(2, bits[j])+"--PREFIX_EMBEDDING-"+bits[j]+"-160/", metrics, keys);
//				line = "PREFIX & " + tree[i] + " & " + bits[j];
//				for (int h = 0; h < res.length; h++){
//					line = line + " & " + Math.round(res[h]*100)/(double)100;
//				}
//				System.out.println(line + " \\\\");
//			}
//		}
//		
//		//Virtual
//		int[] bits2 = {4,5,6,7 , Integer.MAX_VALUE};
//		String[] tree2 = {"", "BFS-HD-", "FAT-" };
//		for (int i = 0; i < tree.length; i++){
//			for (int j = 0; j < bits2.length; j++){
//				double[] res = GetValuesGTNA.getSingleValue(folder+ "READABLE_LIST_VIRTUAL-"+(j==4?tree2[i]:(tree[i]+"-"))+"33317--REMOVE_GRAPH_PROPERTY--PREFIX_EMBEDDING_VIRTUAL-"
//						+bits2[j]+"-160/", metrics, keys);
//				line = "VIRTUAL & " +tree[i] + " & " + bits2[j];
//				for (int h = 0; h < res.length; h++){
//					line = line + " & " + Math.round(res[h]*100)/(double)100;
//				}
//				System.out.println(line + " \\\\");
//			}
//		}
		
		
	}
	
	public static void writeCPbash(String[] files, String[] newfiles, String name){
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(name));
			for (int i = 0; i < files.length; i++){
				bw.write("cp " + files[i] + " " + newfiles[i]);
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
