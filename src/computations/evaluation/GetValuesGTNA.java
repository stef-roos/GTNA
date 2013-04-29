package computations.evaluation;

import gtna.id.ring.RingIdentifierSpace.Distance;
import gtna.transformation.attackableEmbedding.IQD.IQDEmbedding.DecisionMethod;
import gtna.transformation.attackableEmbedding.IQD.IQDEmbedding.IdentifierMethod;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

public class GetValuesGTNA {

	

	public static double[] getSingleValue(String folder, String[] metrics,
			String[][] keys) {
		int c = 0;
		for (int i = 0; i < keys.length; i++) {
			c = c + keys[i].length;
		}
		double[] result = new double[c];
		c = 0;
		for (int i = 0; i < keys.length; i++) {
			for (int j = 0; j < keys[i].length; j++) {
				try {

					BufferedReader br = new BufferedReader(new FileReader(
							folder + metrics[i] + "/_singles.txt"));
					String line;
					while ((line = br.readLine()) != null) {
						if (line.contains(keys[i][j]+"=")) {
							// System.out.println(c + " " + line);
							String[] parts = (line.split("=")[1]).split("	");
							result[c] = Double.parseDouble(parts[0]);
							c++;
						}
					}
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	public static double[][] getSingleValueVariance(String folder,
			String[] metrics, String[][] keys) {
		int c = 0;
		for (int i = 0; i < keys.length; i++) {
			c = c + keys[i].length;
		}
		double[][] result = new double[c][2];
		c = 0;
		for (int i = 0; i < keys.length; i++) {
			for (int j = 0; j < keys[i].length; j++) {
				try {

					BufferedReader br = new BufferedReader(new FileReader(
							folder + metrics[i] + "/_singles.txt"));
					String line;
					while ((line = br.readLine()) != null) {
						if (line.contains(keys[i][j])) {
							// System.out.println(c + " " + line);
							String[] parts = (line.split("=")[1]).split("	");
							result[c][0] = Double.parseDouble(parts[0]);

							result[c][1] = Double.parseDouble(parts[4]);
							// System.out.println(c);
							c++;
						}
					}
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}
			}
		}

		return result;
	}

}
