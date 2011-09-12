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
 * RingID.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: benni;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.id.ring;

import gtna.id.ID;

import java.util.Random;

/**
 * Implements an ID in the wrapping ID space [0,1) (i.e. a ring). Distance
 * computations are performed with wrap-around. When creating a RingID or
 * setting a new position, the position if computed modulo 1.0.
 * 
 * @author benni
 * 
 */
public class RingID implements ID, Comparable<RingID> {
	private double position;

	private RingIDSpace idSpace;

	public RingID(double pos, RingIDSpace idSpace) {
		this.position = Math.abs(pos) % idSpace.getModulus();
		this.idSpace = idSpace;
	}

	@Override
	public double distance(ID id) {
		double dest = ((RingID) id).getPosition();
		if (this.idSpace.isWrapAround()) {
			return Math.abs(dest - this.position)
					% (this.idSpace.getModulus() / 2.0);
		} else {
			return Math.abs(dest - this.position);
		}
	}

	@Override
	public boolean equals(ID id) {
		return this.position == ((RingID) id).getPosition();
	}

	public static RingID rand(Random rand, RingIDSpace idSpace) {
		return new RingID(rand.nextDouble() * idSpace.getModulus(), idSpace);
	}

	/**
	 * @return the pos
	 */
	public double getPosition() {
		return this.position;
	}

	/**
	 * @param pos
	 *            the pos to set
	 */
	public void setPosition(double pos) {
		this.position = Math.abs(pos) % 1.0;
	}

	public String toString() {
		return "RingID(" + this.position + ")";
	}

	@Override
	public int compareTo(RingID id) {
		if (id.getPosition() < this.position) {
			return 1;
		} else if (id.getPosition() > this.position) {
			return -1;
		} else {
			return 0;
		}
	}
}