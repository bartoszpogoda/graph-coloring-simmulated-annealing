package tsp;

import java.io.IOException;

import tsp.algorithm.Algorithm;
import tsp.algorithm.ColoredVertexVector;
import tsp.algorithm.FitnessCalculator;
import tsp.instance.AbstractInstance;
import tsp.instance.reader.InstanceFileReader;

public class BetaParameterTuner {
	
	private static final double STARTING_BETA = 0.1d;

	public static void main(String[] args) throws IOException {
		InstanceFileReader instanceFileReader = new InstanceFileReader();
		AbstractInstance instance = instanceFileReader.read(getPath("input/queen7_7.col"));
		
		BetaParameterTuner tuner = new BetaParameterTuner();
		tuner.tune(instance, 10000, 0.00001, 0.1, 10);
	}
	
	private double tune(AbstractInstance instance, int iterations, double betaStepStart, double betaStepMax, double betaStepMultiplier) {
		
		FitnessCalculator fitnessCalculator = new FitnessCalculator(instance);
		Algorithm algorithm = new Algorithm(instance, fitnessCalculator);
		
		double currentBetaStep = betaStepStart;
		
		double currentBestBetaStep = currentBetaStep;
		int currentBestPenalty = Integer.MAX_VALUE;
		
		for( ; currentBetaStep  <= betaStepMax ; currentBetaStep *= betaStepMultiplier) {
			
			ColoredVertexVector result = algorithm.execute(iterations, STARTING_BETA, currentBetaStep);
			int penalty = fitnessCalculator.calculatePenalty(result);
			
			if(penalty <= currentBestPenalty) {
				currentBestBetaStep = currentBetaStep;
				currentBestPenalty = penalty;
			}
			
		}
		
		System.out.println("Best: " + currentBestBetaStep + " with penalty " + currentBestPenalty);
		return currentBestBetaStep;
	}


	private static String getPath(String classPath) {
		return Demo.class.getClassLoader().getResource(classPath).getPath();
	}
	
	
}
