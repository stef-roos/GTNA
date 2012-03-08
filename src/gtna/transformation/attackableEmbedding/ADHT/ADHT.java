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
import gtna.transformation.attackableEmbedding.lmc.LMCAttackerContraction;
import gtna.transformation.attackableEmbedding.lmc.LMCAttackerConvergence;
import gtna.transformation.attackableEmbedding.lmc.LMCAttackerKleinberg;
import gtna.transformation.attackableEmbedding.lmc.LMCNode;

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
	

	public ADHT(int iterations, String nodeType) {
		this(iterations, nodeType, ATTACK_NONE,
				ATTACKER_SELECTION_NONE, 0);
	}
	
	public ADHT(int iterations, String nodeType, int succ) {
		this(iterations, nodeType, ATTACK_NONE,
				ATTACKER_SELECTION_NONE, 0, succ, Integer.MAX_VALUE);
	}
	
	public ADHT(int iterations, String nodeType, int succ, int upBound) {
		this(iterations, nodeType, ATTACK_NONE,
				ATTACKER_SELECTION_NONE, 0, succ, upBound);
	}

	
	public ADHT(int iterations, String nodeType, 
			String attack, String attackerSelection, int attackers) {
		this(iterations, nodeType,attack,attackerSelection,attackers,13,Integer.MAX_VALUE);
	}
	
	public ADHT(int iterations, String nodeType, 
			String attack, String attackerSelection, int attackers, int succ, int upBound) {
		super(iterations, "ADHT", new String[] { "ITERATIONS","MINSUCC", "UPBOUND", "NODETYPE", "ATTACK", "ATTACKERSELECTION", "ATTACKERS" },
				new String[] { "" + iterations, ""+succ,""+upBound, nodeType,  attack, attackerSelection, "" + attackers });
		this.nodeType = nodeType;
		this.attack = attack;
		this.attackerSelection = attackerSelection;
		this.attackers = attackers;
		this.minForSucc = succ;
		this.upBound = upBound;
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
				if (!success){
					throw new IllegalArgumentException(this.nodeType
							+ " is an unknown type in ADHT");
				}
			}
		}
		this.init(g, nodes);
		this.initIds(g);
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

}
