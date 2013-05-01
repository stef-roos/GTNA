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
 * CCalculator.java
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
//package gtna.metrics.id;
//
//import gtna.data.Single;
//import gtna.graph.Graph;
//import gtna.graph.Node;
//<<<<<<< HEAD
//import gtna.id.DIdentifierSpace;
//import gtna.id.Partition;
//import gtna.id.ring.RingIdentifierSpace;
//import gtna.id.ring.RingIdentifierSpaceSimple;
//import gtna.id.ring.RingPartitionSimple;
//import gtna.io.DataWriter;
//import gtna.metrics.Metric;
//import gtna.networks.Network;
//import gtna.util.Distribution;
//import gtna.util.Timer;
//import gtna.util.parameter.IntParameter;
//import gtna.util.parameter.Parameter;
//
//import java.util.Arrays;
//=======
//import gtna.id.DPartition;
//import gtna.id.ring.RingIdentifierSpace;
//import gtna.id.ring.RingIdentifierSpaceSimple;
//import gtna.id.ring.RingPartitionSimple;
//import gtna.metrics.Metric;
//import gtna.networks.Network;
//
//>>>>>>> 9445d17ee06d98a45139e1a44bfa83c5778c2b54
//import java.util.HashMap;
//
///**
// * @author stefanie
// *
// */
//public class CCalculator extends Metric {
//<<<<<<< HEAD
//     private Distribution C;
//     private Timer runtime;
//     private int nr;
//     
//	
//=======
//
//>>>>>>> 9445d17ee06d98a45139e1a44bfa83c5778c2b54
//	/**
//	 * @param key
//	 */
//	public CCalculator() {
//<<<<<<< HEAD
//		this(0);
//	}
//	
//	public CCalculator(int nr) {
//		super("C", new Parameter[]{new IntParameter("NR", nr)});
//		this.nr = nr;
//=======
//		super("CCALCULATOR");
//		// TODO Auto-generated constructor stub
//>>>>>>> 9445d17ee06d98a45139e1a44bfa83c5778c2b54
//	}
//
//	/* (non-Javadoc)
//	 * @see gtna.metrics.Metric#computeData(gtna.graph.Graph, gtna.networks.Network, java.util.HashMap)
//	 */
//	@Override
//	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
//<<<<<<< HEAD
//		runtime = new Timer();
//		DIdentifierSpace idSpace = (DIdentifierSpace) g.getProperty("ID_SPACE_"+this.nr);
//        Partition<Double>[] parts = idSpace.getPartitions();  
//        double[] count = new double[1];
//        double[] sorted = this.getSortedIDs(parts);
//        int[] cur;
//        Node[] nodes = g.getNodes();
//        for (int i = 0; i < parts.length; i++){
//        	cur = getCForNode(nodes,i,parts,sorted);
//        	if (Math.max(cur[0],cur[1]) >= count.length){
//        		count = this.expand(count, Math.max(cur[0],cur[1])+1);
//        	}
//        	count[cur[0]]++;
//        	count[cur[1]]++;
//        }
//        for (int i = 0; i < count.length; i++){
//        	count[i] = count[i]/(double)(2*nodes.length);
//        }
//        this.C = new Distribution(count);
//        runtime.end();
//	}
//	
//	private double[] getSortedIDs(Partition<Double>[] parts){
//		double[] vals = new double[parts.length];
//		for (int i = 0; i < vals.length; i++){
//			vals[i] = ((RingPartitionSimple)parts[i]).getId().getPosition();
//		}
//		Arrays.sort(vals);
//		return vals;
//	}
//	
//	private int[] getCForNode(Node[] nodes, int index, Partition<Double>[] parts, double[] sortedIDs){
//		Node cur = nodes[index];
//		double left = 1;
//		double right = 1;
//		double idLeft = -1, idRight=-1;
//=======
//		// TODO Auto-generated method stub
//
//	}
//	
//	private HashMap<Integer,Integer> getMap(DPartition[] parts){
////		HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
////		double[] 
////		return map;
//		return null;
//	}
//	
//	private int[] getC(Node[] nodes, int index, DPartition[] parts, HashMap<Integer,Integer> map){
//		Node cur = nodes[index];
//		double left = 1;
//		double right = 1;
//		int indexLeft = -1, indexRight=-1;
//>>>>>>> 9445d17ee06d98a45139e1a44bfa83c5778c2b54
//		int[] out = cur.getOutgoingEdges();
//		double curID = ((RingPartitionSimple)parts[index]).getId().getPosition();
//		double d;
//		for (int j = 0; j < out.length; j++){
//<<<<<<< HEAD
//			double id = ((RingPartitionSimple)parts[out[j]]).getId().getPosition();
//			d = clockwiseDist(curID, id);
//			if (d < left){
//				left = d;
//				idLeft = id;
//=======
//			double id = ((RingPartitionSimple)parts[index]).getId().getPosition();
//			d = clockwiseDist(curID, id);
//			if (d < left){
//				left = d;
//				indexLeft = out[j];
//>>>>>>> 9445d17ee06d98a45139e1a44bfa83c5778c2b54
//			}
//			d = 1-d;
//			if (d < right){
//				right = d;
//<<<<<<< HEAD
//				idRight = id;
//			}
//		}
//		int sIndex = Arrays.binarySearch(sortedIDs, curID);
//		int lIndex = Arrays.binarySearch(sortedIDs, idLeft);
//		int rIndex = Arrays.binarySearch(sortedIDs, idRight);
//		//System.out.println(index + " " + sIndex + " " + lIndex + " " + rIndex + " ");
//		int[] res = {Math.min(Math.abs(sIndex-lIndex), Math.min(sortedIDs.length + sIndex - lIndex, sortedIDs.length - sIndex + lIndex)), 
//				Math.min(Math.abs(sIndex-rIndex), Math.min(sortedIDs.length + sIndex - rIndex, sortedIDs.length - sIndex + rIndex))};
//=======
//				indexRight = out[j];
//			}
//		}
//		int[] res = {indexLeft, indexRight};
//>>>>>>> 9445d17ee06d98a45139e1a44bfa83c5778c2b54
//		return res;
//	}
//	
//	private double clockwiseDist(double a, double b){
//		if (b >= a){
//			return b - a;
//		} else {
//			return (1+b-a);
//		}
//	}
//
//	/* (non-Javadoc)
//	 * @see gtna.metrics.Metric#writeData(java.lang.String)
//	 */
//	@Override
//	public boolean writeData(String folder) {
//<<<<<<< HEAD
//		boolean success = true;
//		success &= DataWriter.writeWithIndex(
//				this.C.getDistribution(),
//				"C_DISTRIBUTION", folder);
//		success &= DataWriter.writeWithIndex(this.C.getCdf(),
//				"C_DISTRIBUTION_CDF", folder);
//		return success;
//=======
//		// TODO Auto-generated method stub
//		return false;
//>>>>>>> 9445d17ee06d98a45139e1a44bfa83c5778c2b54
//	}
//
//	/* (non-Javadoc)
//	 * @see gtna.metrics.Metric#getSingles()
//	 */
//	@Override
//	public Single[] getSingles() {
//<<<<<<< HEAD
//		Single avg = new Single("C_AVG",this.C.getAverage());
//		Single max = new Single("C_MAX",this.C.getMax());
//		Single min = new Single("C_MIN",this.C.getMin());
//		Single med = new Single("C_MEDIAN",this.C.getMedian());
//		Single time = new Single("C_RUNTIME", this.runtime.getRuntime());
//		return new Single[]{avg,max,min,med,time};
//=======
//		// TODO Auto-generated method stub
//		return null;
//>>>>>>> 9445d17ee06d98a45139e1a44bfa83c5778c2b54
//	}
//
//	/* (non-Javadoc)
//	 * @see gtna.metrics.Metric#applicable(gtna.graph.Graph, gtna.networks.Network, java.util.HashMap)
//	 */
//	@Override
//	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
//		return g.hasProperty("ID_SPACE_0")
//				&& (g.getProperty("ID_SPACE_0") instanceof RingIdentifierSpace ||
//						g.getProperty("ID_SPACE_0") instanceof RingIdentifierSpaceSimple	);
//	}
//<<<<<<< HEAD
//	
//	private double[] expand(double[] array, int size) {
//	    double[] temp = new double[size];
//	    System.arraycopy(array, 0, temp, 0, array.length);
//	    for(int j = array.length; j < size; j++)
//	        temp[j] = 0;
//	    return temp;
//	}
//=======
//>>>>>>> 9445d17ee06d98a45139e1a44bfa83c5778c2b54
//
//}
