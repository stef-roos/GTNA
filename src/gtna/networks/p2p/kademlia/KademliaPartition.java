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

	private KademliaIdentifier pred;

	private KademliaIdentifier succ;

	public KademliaPartition(KademliaIdentifier pred, KademliaIdentifier succ) {
		this.pred = pred;
		this.succ = succ;
	}

	public KademliaPartition(String string) {
		this(string, null);
	}

	public KademliaPartition(String string, KademliaIdentifierSpace idSpace) {
		String[] temp = string.replace("(", "").replace("]", "").split(",");
		this.pred = new KademliaIdentifier(idSpace, temp[0]);
		this.succ = new KademliaIdentifier(idSpace, temp[1]);
	}

	public String toString() {
		return "(" + this.pred.getId() + "," + this.succ.getId() + "]";
	}

	@Override
	public BigInteger distance(Identifier<BigInteger> id) {
		if (this.contains(id)) {
			return BigInteger.ZERO;
		}
		return this.succ.distance(id);
	}

	@Override
	public boolean equals(Partition<BigInteger> partition) {
		KademliaPartition compare = (KademliaPartition) partition;
		return this.pred.equals(compare.getPred())
				&& this.succ.equals(compare.getSucc());
	}

	@Override
	public boolean contains(Identifier<BigInteger> id) {
		
		BigInteger v = ((KademliaIdentifier) id).getId();
		BigInteger p = this.pred.getId();
		BigInteger s = this.succ.getId();
		if (s.compareTo(p) == 0){
			if (v.compareTo(s) == 0){
				return true;
			}else {
				return false;
			}
		}
		if (this.pred.getId().compareTo(this.succ.getId()) == -1) {
			return p.compareTo(v) == -1 && v.compareTo(s) != 1;
		} else {
			return p.compareTo(v) == -1 || v.compareTo(s) != 1;
		}
	}

	@Override
	public BIIdentifier getRepresentativeID() {
		return this.succ;
	}

	/**
	 * @return the pred
	 */
	public KademliaIdentifier getPred() {
		return this.pred;
	}

	/**
	 * @param pred
	 *            the pred to set
	 */
	public void setPred(KademliaIdentifier pred) {
		this.pred = pred;
	}

	/**
	 * @return the succ
	 */
	public KademliaIdentifier getSucc() {
		return this.succ;
	}

	/**
	 * @param succ
	 *            the succ to set
	 */
	public void setSucc(KademliaIdentifier succ) {
		this.succ = succ;
	}

}
