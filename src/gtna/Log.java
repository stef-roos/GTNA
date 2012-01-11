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
 * Log.java
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
package gtna;

import gtna.data.Series;
import gtna.networks.Network;
import gtna.networks.util.ReadableFile;
import gtna.transformation.Transformation;
import gtna.transformation.attackableEmbedding.log.lmc.LMCLog;
import gtna.transformation.id.RandomRingIDSpaceSimple;
import gtna.util.Config;

/**
 * @author stef
 *
 */
public class Log {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config.overwrite("WRITEGRAPH", "true");
		Config.overwrite("METRICS", "");
		//logLMC("SPI", "graphs/SPI-LMC/", 100);
         LMCRandom();
	}
	
	public static void logLMC(String name, String folder, int iter){
		Transformation[] lmcNorm = {new LMCLog("results/", "log/", iter,LMCLog.MODE_UNRESTRICTED, 0.0, "" + 10E-12, 0)};
        Transformation[] lmcContraction = {new LMCLog("results/", "log/", iter,LMCLog.MODE_UNRESTRICTED, 0.0, "" + 10E-12, 0,
				LMCLog.ATTACK_CONTRACTION, LMCLog.ATTACKER_SELECTION_LARGEST,100)};
        Transformation[] lmcConvergence = {new LMCLog("results/", "log/", iter,LMCLog.MODE_UNRESTRICTED, 0.0, "" + 10E-12, 0,
				LMCLog.ATTACK_CONVERGENCE, LMCLog.ATTACKER_SELECTION_LARGEST,100)};
        Transformation[] lmcKleinberg = {new LMCLog("results/", "log/", iter,LMCLog.MODE_UNRESTRICTED, 0.0, "" + 10E-12, 0,
				LMCLog.ATTACK_KLEINBERG, LMCLog.ATTACKER_SELECTION_LARGEST,100)};
        Transformation[] lmcOpti = {new LMCLog("results/", "log/", iter,LMCLog.MODE_UNRESTRICTED, 0.0, "" + 10E-12, 0,
				LMCLog.ATTACK_OPTI, LMCLog.ATTACKER_SELECTION_LARGEST,100)};
        Transformation[][] all = {lmcNorm, lmcContraction, lmcConvergence, lmcKleinberg, lmcOpti};
        for (int i = 0; i< 1; i++){
        	System.out.println("Start " + i);
        	Network net = new ReadableFile(name, folder, folder + "graph.txt", null, all[i]);
        	Series.generate(net, 1);
        }
	}
	
	public static void LMCRandom(){
		Transformation[] rand = {new RandomRingIDSpaceSimple()};
		Network net = new ReadableFile("SPI", "graphs/SPI-R/", "graphs/SPI-R/graph.txt", null, rand);
		Series.generate(net, 1);
	}

}
