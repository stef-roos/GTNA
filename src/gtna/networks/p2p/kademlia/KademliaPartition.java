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
 * KademliaPartition.java
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
package gtna.networks.p2p.kademlia;

import gtna.id.BIIdentifier;
import gtna.id.BIPartition;
import gtna.id.Identifier;
import gtna.id.Partition;

import java.math.BigInteger;

/**
 * @author stefanie
 *
 */
public class KademliaPartition implements BIPartition {

	private KademliaIdentifier id;

	public KademliaPartition(KademliaIdentifier id) {
		this.id = id;
	}

	public KademliaPartition(String string) {
		this(string, null);
	}

	public KademliaPartition(String string, KademliaIdentifierSpace idSpace) {
		String[] temp = string.replace("(", "").replace(")", "").split(",");
		this.id = new KademliaIdentifier(idSpace, temp[0]);
		//this.succ = new KademliaIdentifier(idSpace, temp[1]);
	}

	public String toString() {
		return "(" + this.id.getId() + ")";
	}

	@Override
	public BigInteger distance(Identifier<BigInteger> id) {
		if (this.contains(id)) {
			return BigInteger.ZERO;
		}
		return this.id.distance(id);
	}

	@Override
	public boolean equals(Partition<BigInteger> partition) {
		KademliaPartition compare = (KademliaPartition) partition;
		return this.id.equals(compare.getRepresentativeID());
	}

	@Override
	public boolean contains(Identifier<BigInteger> id) {
		if (this.id.equals(id)){
			return true;
		}
		return false;
	}

	@Override
	public BIIdentifier getRepresentativeID() {
		return this.id;
	}

//	/**
//	 * @return the pred
//	 */
//	public KademliaIdentifier getPred() {
//		return this.pred;
//	}
//
//	/**
//	 * @param pred
//	 *            the pred to set
//	 */
//	public void setPred(KademliaIdentifier pred) {
//		this.pred = pred;
//	}

//	/**
//	 * @return the succ
//	 */
//	public KademliaIdentifier getSucc() {
//		return this.succ;
//	}

//	/**
//	 * @param succ
//	 *            the succ to set
//	 */
//	public void setSucc(KademliaIdentifier succ) {
//		this.succ = succ;
//	}

}
