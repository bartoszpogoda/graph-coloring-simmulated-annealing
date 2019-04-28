package tsp;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.Locale;

import tsp.algorithm.Algorithm;
import tsp.algorithm.ColoredVertexVector;
import tsp.algorithm.FitnessCalculator;
import tsp.instance.AbstractInstance;
import tsp.instance.reader.InstanceFileReader;

public class Demo {
	public static void main(String[] args) throws IOException {
		InstanceFileReader instanceFileReader = new InstanceFileReader();
		AbstractInstance instance = instanceFileReader.read(getPath("input/queen7_7.col"));

		FitnessCalculator fitnessCalculator = new FitnessCalculator(instance);
		Algorithm algorithm = new Algorithm(instance, fitnessCalculator);

		Path resultFilePath = Paths.get("results.txt");
		Files.deleteIfExists(resultFilePath);
		Path resultFile = Files.createFile(resultFilePath);

		Path betaFilePath = Paths.get("beta.txt");
		Files.deleteIfExists(betaFilePath);
		Path betaFile = Files.createFile(betaFilePath);
		
		NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
		format.setGroupingUsed(false);

		try (BufferedWriter resultWriter = Files.newBufferedWriter(resultFile);
				BufferedWriter betaWriter = Files.newBufferedWriter(betaFile)) {
			algorithm.setIterationResultListener((it, result, beta) -> {
				if (it % 10 == 0) {
					try {
						resultWriter.append(fitnessCalculator.calculatePenalty(result) + "\t");
						betaWriter.append(format.format(beta) + "\t");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});

			ColoredVertexVector result = algorithm.execute(10002, 0.1, 10);
			
			resultWriter.flush();
			betaWriter.flush();
		}

	}

	private static String getPath(String classPath) {
		return Demo.class.getClassLoader().getResource(classPath).getPath();
	}
}
