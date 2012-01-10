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
 * LMCLog.java
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
package gtna.transformation.attackableEmbedding.log.lmc;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.graph.Node;
import gtna.id.DIdentifierSpace;
import gtna.id.Partition;
import gtna.id.ring.RingIdentifier;
import gtna.id.ring.RingPartitionSimple;
import gtna.transformation.attackableEmbedding.AttackableEmbeddingNode;
import gtna.transformation.attackableEmbedding.log.EmbeddingLog;
import gtna.transformation.attackableEmbedding.log.EmbeddingNodeLog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

/**
 * @author stefanie
 *
 */
public class LMCLog extends EmbeddingLog {

	public static final String MODE_UNRESTRICTED = "UNRESTRICTED";

	public static final String MODE_RESTRICTED = "RESTRICTED";

	public static final String DELTA_1_N = "1_N";

	public static final String DELTA_1_N2 = "1_N_2";

	public static final String ATTACK_CONVERGENCE = "CONVERGENCE";

	public static final String ATTACK_KLEINBERG = "KLEINBERG";

	public static final String ATTACK_CONTRACTION = "CONTRACTION";
	
	public static final String ATTACK_NEIGHBOR = "NEIGHBOR";

	public static final String ATTACK_NONE = "NONE";

	public static final String ATTACKER_SELECTION_LARGEST = "LARGEST";

	public static final String ATTACKER_SELECTION_SMALLEST = "SMALLEST";

	public static final String ATTACKER_SELECTION_MEDIAN = "MEDIAN";

	public static final String ATTACKER_SELECTION_RANDOM = "RANDOM";

	public static final String ATTACKER_SELECTION_NONE = "NONE";
	
	public static final String ATTACK_OPTI = "OPTIMAL";

	protected String mode;

	protected double P;

	protected String deltaMode;

	protected double delta;

	protected int C;

	protected String attack;

	protected String attackerSelection;

	protected int attackers;

	public static int MEDIAN_SET_SIZE = 500;
	
	private RingIdentifier[] ids;

	public LMCLog(String report, String log,int iterations, String mode, double P, String deltaMode, int C) {
		this(report,log,iterations, mode, P, deltaMode, C, ATTACK_NONE,
				ATTACKER_SELECTION_NONE, 0);
	}

	public LMCLog(String report, String log, int iterations, String mode, double P, String deltaMode, int C,
			String attack, String attackerSelection, int attackers) {
		super(report,log,iterations, "LMC", new String[] { "ITERATIONS", "MODE", "P",
				"DELTA", "C", "ATTACK", "ATTACKER_SELECTION", "ATTACKERS" },
				new String[] { "" + iterations, mode, "" + P, deltaMode,
						"" + C, attack, attackerSelection, "" + attackers });
		this.mode = mode;
		this.P = P;
		this.deltaMode = deltaMode;
		this.delta = 0;
		this.C = C;
		this.attack = attack;
		this.attackerSelection = attackerSelection;
		this.attackers = attackers;
		((new File(report))).mkdir();
		((new File(log))).mkdir();
	}

	protected AttackableEmbeddingNode[] generateNodes(Graph g, Random rand) {
		
		this.setDelta(g);
		//retrieve readers and writers
		String[] files = new String[] {"Attackers.txt", "Selection.txt"};
		Node[] nodesG = g.getNodes();
		String[] filesNew = new String[nodesG.length];
		for (int i = 0; i < nodesG.length; i++){
			filesNew[i] = i + ".txt";
		}
		this.setReport(this.getFolderReport(), files);
		this.setLog(this.getFolderLog(), filesNew);
		HashSet<Node> attackers;
		if (!(new File(this.getFolderReport() + "Attackers.txt")).exists()){
			System.out.println("Not exist");
		attackers = new HashSet<Node>();
		if (!ATTACK_NONE.equals(this.attack)
				&& !ATTACKER_SELECTION_NONE.equals(this.attackerSelection)) {
			if (ATTACKER_SELECTION_LARGEST.equals(this.attackerSelection)) {
				attackers = this.selectNodesByDegreeDesc(nodesG,
						this.attackers, rand);
			} else if (ATTACKER_SELECTION_SMALLEST
					.equals(this.attackerSelection)) {
				attackers = this.selectNodesByDegreeAsc(nodesG,
						this.attackers, rand);
			} else if (ATTACKER_SELECTION_MEDIAN.equals(this.attackerSelection)) {
				attackers = this.selectNodesAroundMedian(nodesG,
						this.attackers, rand, MEDIAN_SET_SIZE);
			} else if (ATTACKER_SELECTION_RANDOM.equals(this.attackerSelection)) {
				attackers = this.selectNodesRandomly(nodesG, this.attackers,
						rand);
			} else {
				throw new IllegalArgumentException(this.attackerSelection
						+ " is an unknown attacker selection in LMC");
			}
			Iterator<Node> it = attackers.iterator();
			try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(this.getFolderReport() + "Attackers.txt"));
			while (it.hasNext()){
				bw.write(""+it.next().getIndex());
				if (it.hasNext()){
					bw.newLine();
				}
			}
			bw.flush();
			bw.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		} else {
			attackers = this.readAttackers(g);
		}
		AttackableEmbeddingNode[] nodes = new AttackableEmbeddingNode[nodesG.length];
		for (int i = 0; i < g.getNodes().length; i++) {
			 //double pos = ((RingNode) g.getNodes()[i]).getID().pos;
			 if (attackers.contains(g.getNodes()[i])) {
			 // System.out.println("adding attacker @ " + i + " (D="
			 // + g.getNodes()[i].out().length * 2 + ")");
			 if (ATTACK_CONTRACTION.equals(this.attack)) {
			 nodes[i] = new LMCContractionLog(i, g, this);
			 } else if (ATTACK_CONVERGENCE.equals(this.attack)) {
			 nodes[i] = new LMCConvergenceLog(i, g, this);
			 } else if (ATTACK_KLEINBERG.equals(this.attack)) {
			 nodes[i] = new LMCKleinbergLog(i, g, this);
			 } else if (ATTACK_OPTI.equals(this.attack)) {
				 nodes[i] = new LMCOptiLog(i, g, this);
				 } 
			  else if (ATTACK_NONE.equals(this.attack)) {
				 nodes[i] = new LMCNodeLog(i, g, this);
				 } 
			 else {
				
			 throw new IllegalArgumentException(this.attack
			 + " is an unknown attack in LMC");
			 }
		}
			  else {
			 nodes[i] = new LMCNodeLog(i, g, this);
			 }
			 }
			 this.init(g, nodes);
			 this.initIds(g);
			 return nodes;
	}

	/**
	 * 
	 */
	private HashSet<Node> readAttackers(Graph g) {
		HashSet<Node> attackers = new HashSet<Node>();
		BufferedReader r = this.brs[brs.length-2];
		try {
			String line = r.readLine();
			while (line != null){
				attackers.add(g.getNodes()[Integer.parseInt(line)]);
				line = r.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return attackers;
	}

	protected AttackableEmbeddingNode[] generateSelectionSet(AttackableEmbeddingNode[] nodes,
			Random rand) {
		return nodes.clone();
	}

	/**
	 * initializes delta depending on the configuration parameter deltaMode and
	 * possibly the graph g
	 * 
	 * @param g
	 *            graph on which the selection of delta might depend
	 */
	protected void setDelta(Graph g) {
		if (DELTA_1_N.equals(this.deltaMode)) {
			this.delta = 1.0 / (double) g.getNodes().length;
		} else if (DELTA_1_N2.equals(this.deltaMode)) {
			this.delta = 1.0 / (double) (g.getNodes().length * g.getNodes().length);
		} else {
			this.delta = Double.parseDouble(this.deltaMode);
		}
	}
	
	public Graph transform(Graph g) {
		Random rand = new Random();
		 AttackableEmbeddingNode[] nodes = this.generateNodes(g, rand);
		 AttackableEmbeddingNode[] selectionSet = this.generateSelectionSet(nodes,
		 rand);
		 g.setNodes(nodes);
		 GraphProperty[] prop = g.getProperties("ID_SPACE");
		 DIdentifierSpace idSpace = (DIdentifierSpace) prop[prop.length-1];
		 //Partition<Double>[] ids = idSpace.getPartitions();
		 this.setIdspace(idSpace);
		
		for (int i = 0; i < g.getNodes().length; i++){
			try {
				bws[i].write(""+this.ids[i].getPosition());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if ((new File(this.getFolderReport() + "Selection.txt")).exists()){
            System.out.println("Exists");
            
			BufferedReader r = this.brs[this.brs.length-1];
		   try{
			String line = r.readLine();
			String[] parts;
		   while (line != null){
			   parts = line.split(" ");
			   int index = Integer.parseInt(parts[0]);
			   double newID = Double.parseDouble(parts[1]);
			   double random = Double.parseDouble(parts[2]);
			   ((EmbeddingNodeLog)selectionSet[index]).setOffered(newID);
			   ((EmbeddingNodeLog)selectionSet[index]).setRandom(random);
			   selectionSet[index].updateNeighbors(rand);
			   ((EmbeddingNodeLog)selectionSet[index]).turn(rand);
			   if (newID == this.ids[selectionSet[index].getIndex()].getPosition()){
				   this.bws[selectionSet[index].getIndex()].newLine();
				   this.bws[selectionSet[index].getIndex()].write(""+newID);
				  
			   } 
				  
			   
			   line = r.readLine();
		   }  
		  
		   } catch (IOException e){
			   e.printStackTrace();
		   }
		  } else {
			  try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(this.getFolderReport() + "Selection.txt"));
				for (int i = 0; i < this.iterations * selectionSet.length; i++) {
					int index = rand.nextInt(selectionSet.length);
					double r = rand.nextDouble();
					double random = rand.nextDouble();
					boolean accepted = false;
					if (selectionSet[index].getOutDegree() > 0) {
						selectionSet[index].updateNeighbors(rand);
						((LMCNodeLog)selectionSet[index]).setOffered(r);
						((LMCNodeLog)selectionSet[index]).setRandom(random);
						selectionSet[index].turn(rand);
					}
					if (r == this.ids[selectionSet[index].getIndex()].getPosition()){
						this.bws[selectionSet[index].getIndex()].newLine();
						   this.bws[selectionSet[index].getIndex()].write(""+r);
						   accepted = true;
					   }
					bw.write(index + " " + r + " " + random + " " + accepted);
					if (i < this.iterations*selectionSet.length){
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
		this.closeLog();
		this.closeReport();
		Partition<Double>[] parts = new RingPartitionSimple[g.getNodes().length];
		
		 for (int i = 0; i < parts.length; i++){
			 parts[i] = new RingPartitionSimple(ids[i]);
			// System.out.println(parts[i].getRepresentativeID());
		 }
		 idSpace.setPartitions(parts);
		// g.addProperty(g.getNextKey("ID_SPACE"), idSpace);
		 return g;
	}

	 /**
	  * init IdSpace from a graph g
	  * @param g
	  */
	 public void initIds(Graph g){
		 GraphProperty[] gp = g.getProperties("ID_SPACE");
		 GraphProperty p = gp[gp.length-1];
		 DIdentifierSpace idSpaceD = (DIdentifierSpace) p;
		 this.ids = new RingIdentifier[g.getNodes().length];
		 for (int i = 0; i < ids.length; i++ ){
			 ids[i] = (RingIdentifier) idSpaceD.getPartitions()[i].getRepresentativeID();
		 }
	 }
	

	@Override
	public RingIdentifier[] getIds() {
		// TODO Auto-generated method stub
		return this.ids;
	}

}
