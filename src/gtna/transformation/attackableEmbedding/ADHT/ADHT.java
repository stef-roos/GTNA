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
 * ADHT.java
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
package gtna.transformation.attackableEmbedding.ADHT;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.graph.Node;
import gtna.id.DIdentifierSpace;
import gtna.id.ring.RingIdentifier;
import gtna.transformation.attackableEmbedding.AttackableEmbedding;
import gtna.transformation.attackableEmbedding.AttackableEmbeddingNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/**
 * @author stef
 *
 */
public class ADHT extends AttackableEmbedding {

	public static final String NODE_TYPE_SIMPLE = "SIMPLE";
	
	public static final String NODE_TYPE_SUCC = "SUCC";
	
	public static final String NODE_TYPE_ALLSUCC = "ALL";
	
	public static final String NODE_TYPE_RSUCC = "RSUCC";
	
	public static final String NODE_TYPE_CLOSE = "CLOSE";
	
	public static final String NODE_TYPE_RAND = "RAND";
	
	public static final String NODE_TYPE_RESSIZE = "RESSIZE";
	
	public static final String NODE_TYPE_RESNUMB = "RESNUMB";
	
	public static final String NODE_TYPE_DIVERSE = "DIVERSE";
	
	public static final String NODE_TYPE_SA = "SA";
	
	public static final String NODE_TYPE_TWOSIDE = "TWOSIDE";
	
	public static final String NODE_TYPE_OUTLIER = "OUTLIER";

	public static final String ATTACK_NONE = "NONE";

	public static final String ATTACKER_SELECTION_LARGEST = "LARGEST";

	public static final String ATTACKER_SELECTION_SMALLEST = "SMALLEST";

	public static final String ATTACKER_SELECTION_MEDIAN = "MEDIAN";

	public static final String ATTACKER_SELECTION_RANDOM = "RANDOM";

	public static final String ATTACKER_SELECTION_NONE = "NONE";

	public static int MEDIAN_SET_SIZE = 500;

	private RingIdentifier[] ids;
	
	private String nodeType;
	
	protected String attack;

	protected String attackerSelection;

	protected int attackers;
	
	protected int minForSucc = 13;
	
	protected int upBound;
	
	protected double out; 
	
	private int[][] BC;
	
	protected double[] exp;
	
	protected double[] var;
	

	public ADHT(int iterations, String nodeType,double out) {
		this(iterations, nodeType, out,ATTACK_NONE,
				ATTACKER_SELECTION_NONE, 0);
	}
	
	public ADHT(int iterations, String nodeType, int succ, double out) {
		this(iterations, nodeType, out,ATTACK_NONE,
				ATTACKER_SELECTION_NONE, 0, succ, Integer.MAX_VALUE);
	}
	
	public ADHT(int iterations, String nodeType, double out, int succ, int upBound) {
		this(iterations, nodeType, out, ATTACK_NONE,
				ATTACKER_SELECTION_NONE, 0, succ, upBound);
	}

	
	public ADHT(int iterations, String nodeType, double out,
			String attack, String attackerSelection, int attackers) {
		this(iterations, nodeType,out,attack,attackerSelection,attackers,13,Integer.MAX_VALUE);
	}
	
	public ADHT(int iterations, String nodeType, double out,
			String attack, String attackerSelection, int attackers, int succ, int upBound) {
		super(iterations, "ADHT", new String[] { "ITERATIONS","MINSUCC", "UPBOUND", "NODETYPE","OUT", "ATTACK", "ATTACKERSELECTION", "ATTACKERS" },
				new String[] { "" + iterations, ""+succ,""+upBound, nodeType, ""+out,  attack, attackerSelection, "" + attackers });
		this.nodeType = nodeType;
		this.attack = attack;
		this.attackerSelection = attackerSelection;
		this.attackers = attackers;
		this.minForSucc = succ;
		this.upBound = upBound;
		this.out = out;
	}

	protected AttackableEmbeddingNode[] generateNodes(Graph g, Random rand) {
		HashSet<Node> attackers = new HashSet<Node>();
		if (!ATTACK_NONE.equals(this.attack)
				&& !ATTACKER_SELECTION_NONE.equals(this.attackerSelection)) {
			if (ATTACKER_SELECTION_LARGEST.equals(this.attackerSelection)) {
				attackers = this.selectNodesByDegreeDesc(g.getNodes(),
						this.attackers, rand);
			} else if (ATTACKER_SELECTION_SMALLEST
					.equals(this.attackerSelection)) {
				attackers = this.selectNodesByDegreeAsc(g.getNodes(),
						this.attackers, rand);
			} else if (ATTACKER_SELECTION_MEDIAN.equals(this.attackerSelection)) {
				attackers = this.selectNodesAroundMedian(g.getNodes(),
						this.attackers, rand, MEDIAN_SET_SIZE);
			} else if (ATTACKER_SELECTION_RANDOM.equals(this.attackerSelection)) {
				attackers = this.selectNodesRandomly(g.getNodes(),
						this.attackers, rand);
			} else {
				throw new IllegalArgumentException(this.attackerSelection
						+ " is an unknown attacker selection in LMC");
			}
		}
		AttackableEmbeddingNode[] nodes = new AttackableEmbeddingNode[g
				.getNodes().length];

		for (int i = 0; i < g.getNodes().length; i++) {
			// double pos = ((RingNode) g.getNodes()[i]).getID().pos;
			if (attackers.contains(g.getNodes()[i])) {
				// System.out.println("adding attacker @ " + i + " (D="
				// + g.getNodes()[i].out().length * 2 + ")");
				
					throw new IllegalArgumentException(this.attack
							+ " is an unknown attack in ADHT");
				
			} else {
				boolean success = false;
				if (this.nodeType == NODE_TYPE_SIMPLE){
				  nodes[i] = new SimpleADHTNode(i, g, this);
				  success = true;
				}
				if (this.nodeType == NODE_TYPE_SUCC){
					  nodes[i] = new SuccADHTNode(i, g, this);
					  success = true;
				}
				if (this.nodeType == NODE_TYPE_ALLSUCC){
					  nodes[i] = new AllSuccADHTNode(i, g, this);
					  success = true;
				}
				if (this.nodeType == NODE_TYPE_CLOSE){
					  nodes[i] = new PreferCloseADHTNode(i, g, this);
					  success = true;
				}
				if (this.nodeType == NODE_TYPE_RSUCC){
					  nodes[i] = new RandSuccADHTNode(i, g, this);
					  success = true;
				}
				if (this.nodeType == NODE_TYPE_RAND){
					  nodes[i] = new RandDecisionADHTNode(i, g, this);
					  success = true;
				}
				if (this.nodeType == NODE_TYPE_RESSIZE){
					  nodes[i] = new RestrictSizeADHTNode(i, g, this);
					  success = true;
				}
				if (this.nodeType == NODE_TYPE_RESNUMB){
					  nodes[i] = new RestrictNumberADHTNode(i, g, this);
					  success = true;
				}
				if (this.nodeType == NODE_TYPE_DIVERSE){
					  nodes[i] = new DiverseADHTNode(i, g, this);
					  success = true;
				}
				if (this.nodeType == NODE_TYPE_SA){
					  nodes[i] = new SAADHTNode(i, g, this);
					  success = true;
				}
				if (this.nodeType == NODE_TYPE_TWOSIDE){
					  nodes[i] = new TwoSideADHTNode(i, g, this);
					  success = true;
				}
				if (this.nodeType == NODE_TYPE_OUTLIER){
					  nodes[i] = new OutlierADHTNode(i, g, this);
					  success = true;
				}
				if (!success){
					throw new IllegalArgumentException(this.nodeType
							+ " is an unknown type in ADHT");
				}
			}
		}
		this.init(g, nodes);
		this.initIds(g);
		if (this.nodeType == NODE_TYPE_OUTLIER){
			this.calculateExpVar(nodes);
		}
		return nodes;
	}

	protected AttackableEmbeddingNode[] generateSelectionSet(
			AttackableEmbeddingNode[] nodes, Random rand) {
		return nodes.clone();
	}

	

	/**
	 * init IdSpace from a graph g
	 * 
	 * @param g
	 */
	public void initIds(Graph g) {
		GraphProperty[] gp = g.getProperties("ID_SPACE");
		GraphProperty p = gp[gp.length - 1];
		DIdentifierSpace idSpaceD = (DIdentifierSpace) p;
		this.ids = new RingIdentifier[g.getNodes().length];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = (RingIdentifier) idSpaceD.getPartitions()[i]
					.getRepresentativeID();
			
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.transformation.attackableEmbedding.AttackableEmbedding#getIDs()
	 */
	@Override
	public RingIdentifier[] getIds() {
		return this.ids;
	}
	
	private void calculateExpVar(Node[] nodes){
		int maxDeg = 0;
		for (int i = 0; i < nodes.length; i++){
			if (nodes[i].getOutDegree() > maxDeg){
				maxDeg = nodes[i].getOutDegree();
			}
		}
		this.initializeBC(Math.max(maxDeg,this.upBound));
		HashMap<Integer, double[]> degreeVal = new HashMap<Integer,double[]>();
		exp = new double[nodes.length];
		var = new double[nodes.length];
		double[] res;
		for (int i = 0; i < nodes.length; i++){
			res = degreeVal.get(nodes[i].getOutDegree());
			if (res == null){
			    res = new double[2];
			    double[] prop = new double[Math.min(this.upBound,nodes[i].getOutDegree())+1];
			    double sum = 0;
			    for (int j = 1; j < prop.length; j++){
			    	prop[j] = nchoosek(this.upBound,j)*nchoosek(nodes[i].getOutDegree()-1,j-1);
			    	sum = sum + prop[j];
			    }
			    for (int j = 1; j < prop.length; j++){
			    	prop[j] = prop[j]/sum;
			    }
			    for (int k = 1; k < prop.length; k++){
			    	res[0] = res[0] + k*prop[k];
			    	res[1] = res[1] + k*k*prop[k];
			    }
			    res[1] = Math.sqrt(res[1] - res[0]*res[0]);
			    degreeVal.put(nodes[i].getOutDegree(), res);
			}
			exp[i] = res[0];
			var[i] = res[1];
			
		}
		
	}
	
	private int nchoosek(int n, int k){
		if (n < 0 || k < 0 || k > n){
			return 0;
		}
		return this.BC[n][k];
	}
	
	private void initializeBC(int max){
		this.BC = new int[max+1][max+2];
		this.BC[0][0] = 1;
		for (int i = 1; i < max+1; i++){
			this.BC[i][0] = 1;
			for (int j = 1; j < i+2; j++){
				this.BC[i][j] = this.BC[i-1][j-1] + this.BC[i-1][j];
			}
		}
	}

}
