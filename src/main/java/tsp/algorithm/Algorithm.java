package tsp.algorithm;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import tsp.instance.AbstractInstance;

public class Algorithm {

	private AbstractInstance instance;

	private FitnessCalculator fitnessCalculator;

	private RandomGenerator randomGenerator;
	
	private IterationResultListener iterationResultListener;

	public Algorithm(AbstractInstance instance, FitnessCalculator fitnessCalculator, RandomGenerator randomGenerator) {
		if (!instance.equals(fitnessCalculator.getInstance())) {
			throw new IllegalArgumentException("Fitness calculator is not bound to given instance");
		}

		this.iterationResultListener = null;
		this.instance = instance;
		this.fitnessCalculator = fitnessCalculator;
		this.randomGenerator = randomGenerator;
	}

	public Algorithm(AbstractInstance instance, FitnessCalculator fitnessCalculator) {
		this(instance, fitnessCalculator, new RandomGenerator());
	}
	

	public synchronized ColoredVertexVector execute(int iterations, double beta, double betaStep) {
		ColoredVertexVector vector = generateRandomVector(instance.getSize());
		
		return execute(vector, iterations - 1, beta, betaStep);
	}
	
	public synchronized ColoredVertexVector execute(ColoredVertexVector vector, int iterations, double beta, double betaStep) {
		
		int length = vector.getLength();
		
		for(int i = 0 ; i < iterations ; i++) {
			int randomVertex = randomGenerator.generateIntInRangeExclusive(0, length);
			int currentColor = vector.getVertexAt(randomVertex).getColor();
			
			double penaltyBeforeChange = fitnessCalculator.calculatePenalty(vector);
			
			int newColor = generateRandomNewColor(vector, currentColor);
			ColoredVertexVector newVector = vector.setVertexAt(randomVertex, new ColoredVertex(newColor));
			
			double penaltyAfterChange = fitnessCalculator.calculatePenalty(newVector);
			
			double deltaPenalty = penaltyAfterChange - penaltyBeforeChange;
			
			if(deltaPenalty <= 0) {
				// accept with probability 1
				vector = newVector;
			} else {
				double acceptanceProbability = Math.exp((-1) * beta * deltaPenalty);
				
				double random = randomGenerator.nextDouble();
				if(random <= acceptanceProbability) {
					vector = newVector;
				}
			}
			
			beta = beta + betaStep;

			if(iterationResultListener != null) {
				iterationResultListener.notify(i, vector, beta);
			}
		}
		
		return vector; 
	}

	private ColoredVertexVector generateRandomVector(int graphSize) {
		ColoredVertexVector vector = new ColoredVertexVector(graphSize);

		for(int i = 0 ; i < graphSize ; i++) {
			ColoredVertex randomVertex = new ColoredVertex(randomGenerator.generateIntInRangeExclusive(0, graphSize));
			vector = vector.setVertexAt(i, randomVertex);
		}
		
		return vector;
	}
	

	private int generateRandomNewColor(ColoredVertexVector vector, int currentColor) {
		List<Integer> availableColors = IntStream.range(0, vector.getLength())
											.mapToObj(i -> vector.getVertexAt(i))
											.map(ColoredVertex::getColor)
											.distinct()
											.filter(color -> color != currentColor)
											.collect(Collectors.toList());
		
		if(availableColors.size() > 0) {
			return availableColors.get(randomGenerator.generateIntInRangeExclusive(0, availableColors.size()));	
		} else {
			return currentColor;
		}
	}
	

	public void setIterationResultListener(IterationResultListener iterationResultListener) {
		this.iterationResultListener = iterationResultListener;
	}

}
