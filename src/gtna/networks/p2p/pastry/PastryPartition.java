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
 * PastryPartition.java
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
package gtna.networks.p2p.pastry;

import java.math.BigInteger;

import gtna.id.BIIdentifier;
import gtna.id.BIPartition;
import gtna.id.Identifier;
import gtna.id.Partition;

/**
 * @author stefanie
 *
 */
public class PastryPartition implements BIPartition {

	private PastryIdentifier pred;

	private PastryIdentifier succ;

	public PastryPartition(PastryIdentifier pred, PastryIdentifier succ) {
		this.pred = pred;
		this.succ = succ;
	}

	public PastryPartition(String string) {
		this(string, null);
	}

	public PastryPartition(String string, PastryIdentifierSpace idSpace) {
		String[] temp = string.replace("(", "").replace("]", "").split(",");
		this.pred = new PastryIdentifier(idSpace, temp[0]);
		this.succ = new PastryIdentifier(idSpace, temp[1]);
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
		PastryPartition compare = (PastryPartition) partition;
		return this.pred.equals(compare.getPred())
				&& this.succ.equals(compare.getSucc());
	}

	@Override
	public boolean contains(Identifier<BigInteger> id) {
		BigInteger v = ((PastryIdentifier) id).getId();
		BigInteger p = this.pred.getId();
		BigInteger s = this.succ.getId();
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
	public PastryIdentifier getPred() {
		return this.pred;
	}

	/**
	 * @param pred
	 *            the pred to set
	 */
	public void setPred(PastryIdentifier pred) {
		this.pred = pred;
	}

	/**
	 * @return the succ
	 */
	public PastryIdentifier getSucc() {
		return this.succ;
	}

	/**
	 * @param succ
	 *            the succ to set
	 */
	public void setSucc(PastryIdentifier succ) {
		this.succ = succ;
	}

}
