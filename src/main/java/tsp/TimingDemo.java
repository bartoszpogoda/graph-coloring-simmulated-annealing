package tsp;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;

import tsp.algorithm.Algorithm;
import tsp.algorithm.ColoredVertexVector;
import tsp.algorithm.FitnessCalculator;
import tsp.algorithm.IterationResultListener;
import tsp.instance.AbstractInstance;
import tsp.instance.reader.InstanceFileReader;

public class TimingDemo {

	static class TimedIterationResultListener implements IterationResultListener {

		private Instant start;

		private FitnessCalculator fitnessCalculator;

		private BufferedWriter resultWriter;

		public TimedIterationResultListener(FitnessCalculator fitnessCalculator) throws IOException {
			this.fitnessCalculator = fitnessCalculator;

			Path resultFilePath = Paths.get("performance.txt");
			Files.deleteIfExists(resultFilePath);
			Path resultFile = Files.createFile(resultFilePath);

			resultWriter = Files.newBufferedWriter(resultFile);
		}

		public void start() {
			start = Instant.now();
		}

		@Override
		public void notify(int iteration, ColoredVertexVector result, double beta) {
			Instant now = Instant.now();

			try {
				resultWriter.append(Long.toString(Duration.between(start, now).toMillis()));
				resultWriter.append("\t");
				resultWriter.append(Integer.toString(fitnessCalculator.calculatePenalty(result)));
				resultWriter.newLine();

			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		public void finish() {
			try {
				resultWriter.flush();
				resultWriter.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

	}

	public static void main(String[] args) throws IOException {
		InstanceFileReader instanceFileReader = new InstanceFileReader();
		AbstractInstance instance = instanceFileReader.read(getPath("input/le450_15b.col"));

		FitnessCalculator fitnessCalculator = new FitnessCalculator(instance);
		Algorithm algorithm = new Algorithm(instance, fitnessCalculator);

		NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
		format.setGroupingUsed(false);

		TimedIterationResultListener listener = new TimingDemo.TimedIterationResultListener(fitnessCalculator);

		algorithm.setIterationResultListener(listener);

		listener.start();
		ColoredVertexVector result = algorithm.execute(10000, 0.1, 0.01);
		listener.finish();

	}

	private static String getPath(String classPath) {
		return TimingDemo.class.getClassLoader().getResource(classPath).getPath();
	}
}
