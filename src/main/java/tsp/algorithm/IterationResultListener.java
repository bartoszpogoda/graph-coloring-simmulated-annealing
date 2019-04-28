package tsp.algorithm;

public interface IterationResultListener {

	public void notify(int iteration, ColoredVertexVector result, double beta);
	
}
