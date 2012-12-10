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
 * MaxNormBPartitionSimple.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Andreas Höfer;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.id.maxnorm;

import gtna.id.SPartition;
import gtna.id.Identifier;
import gtna.id.Partition;

/**
 * @author Andreas Höfer
 *
 */
public class MaxNormSPartitionSimple implements SPartition {

	private MaxNormSIdentifier id;
	
	public MaxNormSPartitionSimple(MaxNormSIdentifier id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see gtna.id.Partition#distance(gtna.id.Identifier)
	 */
	@Override
	public Short distance(Identifier<Short> id) {
		return this.id.distance(id);
	}

	/* (non-Javadoc)
	 * @see gtna.id.Partition#equals(gtna.id.Partition)
	 */
	@Override
	public boolean equals(Partition<Short> p) {
		return this.id.equals((MaxNormSIdentifier) p.getRepresentativeID());
	}

	/* (non-Javadoc)
	 * @see gtna.id.Partition#contains(gtna.id.Identifier)
	 */
	@Override
	public boolean contains(Identifier<Short> id) {
		return this.id.equals(id);
	}

	/* (non-Javadoc)
	 * @see gtna.id.Partition#getRepresentativeID()
	 */
	@Override
	public Identifier<Short> getRepresentativeID() {
		return this.id;
	}

	public String toString() {
		return this.id.toString();
	}
}
