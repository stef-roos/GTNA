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
 * MainMotifs.java
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
package gtna;

import gtna.data.Series;
import gtna.networks.util.ReadableFile;
import gtna.plot.Plot;
import gtna.util.Config;

import java.io.File;

/**
 * @author stefanie
 *
 */
public class MainMotifs {
	
	public static void main(String[] args) {
		String folder = "manylangs/";
		Config.overwrite("METRICS", "DEGREE_DISTRIBUTION, CLUSTERING_COEFFICIENT");
		String[] files = ((new File(folder))).list();
		Series[] ser = new Series[files.length];
		for (int i = 0; i < files.length; i++){
			ReadableFile net = new ReadableFile(files[i], folder, folder+files[i], null, null);
			 ser[i] = Series.generate(net, 1);
			Plot.multiAvg(ser[i], "motifAv"+ files[i]+"/");
		}
		Plot.allMulti(ser, "allMulti/");
		Plot.allSingle(ser, "allSingle/");
	}

}
