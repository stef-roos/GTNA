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
 * PastryIdentifier.java
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
import java.util.Random;

import gtna.id.BIIdentifier;
import gtna.id.Identifier;

/**
 * @author stefanie
 *
 */
public class PastryIdentifier implements BIIdentifier, Comparable<PastryIdentifier> {
	private PastryIdentifierSpace idSpace;

	private BigInteger id;

	public PastryIdentifier(PastryIdentifierSpace idSpace, BigInteger id) {
		this.idSpace = idSpace;
		this.id = id;
	}

	public PastryIdentifier(PastryIdentifierSpace idSpace, String string) {
		this.id = null;
		this.id = new BigInteger(string);
	}

	@Override
	public int compareTo(PastryIdentifier arg0) {
		return this.id.compareTo(((PastryIdentifier) arg0).getId());
	}

	public BigInteger distance(Identifier<BigInteger> id) {
		BigInteger dest = ((PastryIdentifier) id).getId();
		BigInteger dist;
		if (this.id.compareTo(dest) == -1) {
			dist = dest.subtract(this.id);
		} else {
			dist = this.idSpace.getModulus().subtract(this.id).add(dest);
		}
		if (dist.compareTo(this.idSpace.getModulus().divide(BigInteger.ONE.add(BigInteger.ONE)))
				== 1){
			return this.idSpace.getModulus().subtract(dist);
		} else {
			return dist;
		}
	}

	@Override
	public boolean equals(Identifier<BigInteger> id) {
		return this.id.equals(((PastryIdentifier) id).getId());
	}

	public static PastryIdentifier rand(Random rand, PastryIdentifierSpace idSpace) {
		return new PastryIdentifier(idSpace, new BigInteger(idSpace.getBits(),
				rand));
	}

	/**
	 * @return the idSpace
	 */
	public PastryIdentifierSpace getIdSpace() {
		return this.idSpace;
	}

	/**
	 * @param idSpace
	 *            the idSpace to set
	 */
	public void setIdSpace(PastryIdentifierSpace idSpace) {
		this.idSpace = idSpace;
	}

	/**
	 * @return the id
	 */
	public BigInteger getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(BigInteger id) {
		this.id = id;
	}

	public String toString() {
		return this.id.toString();
	}
	
	 public int[] getPrefixLength(int b, int M, BigInteger other){
		 int[] res = new int[2];
		 BigInteger c = BigInteger.ZERO.add(this.id);
		 BigInteger a = BigInteger.ZERO.add(other);
		 BigInteger diff = c.subtract(a).abs();
		 int incommon = M/b -1;
		 BigInteger prefix = BigInteger.ONE.add(BigInteger.ONE).pow(b);
		 while (diff.compareTo(prefix) == 1){
			 a = a.shiftRight(b);
			 c = c.shiftRight(b);
			 diff = a.subtract(c).abs();
			 incommon--;
		 }
		 res[0] = incommon;
         res[1] = diff.intValue();
		 return res;
	 }

}
