package tsp.algorithm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tsp.instance.AbstractInstance;
import tsp.instance.Edge;

public class FitnessCalculator {

	private AbstractInstance instance;

	public FitnessCalculator(AbstractInstance instance) {
		this.instance = instance;
	}

	public double calculateFitness(ColoredVertexVector vector) {

		int colorCount = countColors(vector);
		int invalidEdgesCount = countInvalidEdges(vector);

		double score = (double) 1 / (colorCount + invalidEdgesCount);

		return score;
	}
	
	public int calculatePenalty(ColoredVertexVector vector) {
		return countColors(vector) + countInvalidEdges(vector);
	}

	public int countColors(ColoredVertexVector vector) {
		Map<Integer, Integer> colorCounts = new HashMap<>();

		for (int i = 0; i < vector.getLength(); i++) {
			int color = vector.getVertexAt(i).getColor();

			colorCounts.putIfAbsent(color, 0);
			colorCounts.computeIfPresent(color, (key, value) -> value + 1);
		}

		return colorCounts.keySet().size();
	}

	public int countInvalidEdges(ColoredVertexVector vector) {
		int count = 0;
		List<Edge> edges = instance.getAllEdges();

		for (Edge edge : edges) {
			if (vector.getVertexAt(edge.getFrom()).getColor() == vector.getVertexAt(edge.getTo()).getColor()) {
				count++;
			}
		}
		return count;
	}

	public AbstractInstance getInstance() {
		return instance;
	}

}
