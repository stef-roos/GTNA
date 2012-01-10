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
 * EmbettingLog.java
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
package gtna.transformation.attackableEmbedding.log;

import gtna.graph.Graph;
import gtna.logging.Logging;
import gtna.transformation.attackableEmbedding.AttackableEmbedding;
 

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * @author stefanie
 *
 */
public abstract class EmbeddingLog extends AttackableEmbedding implements Logging{
	private String report;
	private String log;
	protected BufferedWriter[] bws;
	protected BufferedReader[] brs;
	
	public EmbeddingLog(String report, String log, int iterations, String key, String[] configKeys,
			String[] configValues){
		   super(iterations, key, configKeys, configValues);
		   this.report = report;
		   this.log = log;
	}

	


	

	/* (non-Javadoc)
	 * @see gtna.logging.Logging#closeLog()
	 */
	@Override
	public void closeLog() {
		try{
			for (int i = 0; i < bws.length; i++){
				if (bws[i] != null){
					bws[i].flush();
					bws[i].close();
				}
			}
		} catch (IOException e){
			e.printStackTrace();
		}
		
	}

	/* (non-Javadoc)
	 * @see gtna.logging.Logging#setReport(java.lang.String, java.lang.String[])
	 */
	@Override
	public void setReport(String folder, String[] files) {
		this.report = folder;
		this.brs = new BufferedReader[files.length];
		for (int i = 0; i < files.length; i++){
			try {
				if ((new File(folder + files[i])).exists()){
				    brs[i] = new BufferedReader(new FileReader(folder + files[i]));
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setLog(String folder, String[] files){
		this.log = folder;
		(new File(folder)).mkdir();
		this.bws = new BufferedWriter[files.length];
		for (int i = 0; i < files.length; i++){
			try {
				bws[i] = new BufferedWriter(new FileWriter(folder + files[i]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see gtna.logging.Logging#getFolderReport()
	 */
	@Override
	public String getFolderReport() {
		// TODO Auto-generated method stub
		return this.report;
	}

	/* (non-Javadoc)
	 * @see gtna.logging.Logging#getFilesReport()
	 */
	@Override
	public String[] getFilesReport() {
		// TODO Auto-generated method stub
		return (new File(this.report)).list();
	}

	/* (non-Javadoc)
	 * @see gtna.logging.Logging#getFolderLog()
	 */
	@Override
	public String getFolderLog() {
		// TODO Auto-generated method stub
		return this.log;
	}

	/* (non-Javadoc)
	 * @see gtna.logging.Logging#getFilesLog()
	 */
	@Override
	public String[] getFilesLog() {
		// TODO Auto-generated method stub
		return (new File(this.log)).list();
	}

	/* (non-Javadoc)
	 * @see gtna.logging.Logging#closeReport()
	 */
	@Override
	public void closeReport() {
		try{
			for (int i = 0; i < brs.length; i++){
				if (brs[i] != null){
					brs[i].close();
				}
			}
		} catch (IOException e){
			e.printStackTrace();
		}
		
	}

}
