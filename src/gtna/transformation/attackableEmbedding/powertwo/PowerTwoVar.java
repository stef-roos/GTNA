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
 * PowerTwoVar.java
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
package gtna.transformation.attackableEmbedding.powertwo;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.graph.Node;
import gtna.id.DIdentifierSpace;
import gtna.id.ring.RingIdentifier;
import gtna.transformation.attackableEmbedding.AttackableEmbedding;
import gtna.transformation.attackableEmbedding.AttackableEmbeddingNode;
import gtna.transformation.attackableEmbedding.ADHT.AllSuccADHTNode;
import gtna.transformation.attackableEmbedding.ADHT.DiverseADHTNode;
import gtna.transformation.attackableEmbedding.ADHT.PreferCloseADHTNode;
import gtna.transformation.attackableEmbedding.ADHT.RandDecisionADHTNode;
import gtna.transformation.attackableEmbedding.ADHT.RandSuccADHTNode;
import gtna.transformation.attackableEmbedding.ADHT.RestrictNumberADHTNode;
import gtna.transformation.attackableEmbedding.ADHT.RestrictSizeADHTNode;
import gtna.transformation.attackableEmbedding.ADHT.SimpleADHTNode;
import gtna.transformation.attackableEmbedding.ADHT.SuccADHTNode;

import java.util.HashSet;
import java.util.Random;

/**
 * @author stef
 *
 */
public class PowerTwoVar extends AttackableEmbedding{

	public static final String NODE_TYPE_SIMPLE = "SIMPLE";
	
	public static final String NODE_TYPE_TWOSIDE = "TWOSIDE";
	
	public static final String NODE_TYPE_MOD = "MOD";
	
	public static final String NODE_TYPE_CEN = "CENTRAL";
	
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
	
	protected double exponent;
	
	protected int upBound;
	
	protected int minForSucc;
	

	public PowerTwoVar(int iterations, double exponent, int succ, int upBound, String nodeType) {
		this(iterations,exponent, succ, upBound, nodeType, ATTACK_NONE,
				ATTACKER_SELECTION_NONE, 0);
	}
	
	
	
	public PowerTwoVar(int iterations, double exponent, int succ, int upBound,  String nodeType, 
			String attack, String attackerSelection, int attackers) {
		super(iterations, "POWER_TWO_VAR", new String[] { "ITERATIONS","EXPONENT", "MINSUCC", "UPBOUND", "NODETYPE", "ATTACK", "ATTACKERSELECTION", "ATTACKERS" },
				new String[] { "" + iterations, ""+exponent, ""+succ,""+upBound, nodeType,  attack, attackerSelection, "" + attackers });
		this.nodeType = nodeType;
		this.attack = attack;
		this.attackerSelection = attackerSelection;
		this.attackers = attackers;
		this.minForSucc = succ;
		this.upBound = upBound;
		this.exponent = exponent;
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
				  nodes[i] = new SimpleP2Node(i, g, this);
				  success = true;
				}
				if (this.nodeType == NODE_TYPE_TWOSIDE){
					  nodes[i] = new TwoSideP2Node(i, g, this);
					  success = true;
					}
				if (this.nodeType == NODE_TYPE_MOD){
					  nodes[i] = new ModP2Node(i, g, this);
					  success = true;
					}
				if (this.nodeType == NODE_TYPE_CEN){
					  nodes[i] = new CentralP2Node(i, g, this);
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
