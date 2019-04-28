package tsp.algorithm;

import java.util.Arrays;

public class ColoredVertexVector {

	private ColoredVertex[] verticles = null;

	public ColoredVertexVector(int length) {
		this.verticles = new ColoredVertex[length];
	}
	
	public ColoredVertexVector(ColoredVertexVector otherVector) {
		this(otherVector.getLength());
		
		verticles = Arrays.copyOf(otherVector.verticles, verticles.length);
	}

	public int getLength() {
		return verticles.length;
	}
	
	public ColoredVertexVector setVertexAt(int i, ColoredVertex value) {
		ColoredVertexVector newVertexVector = new ColoredVertexVector(this);
		
		newVertexVector.verticles[i] = value;
		
		return newVertexVector;
	}
	
	public ColoredVertex getVertexAt(int i) {
		return this.verticles[i];
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < verticles.length; i++) {
			builder.append(verticles[i] + (i == verticles.length - 1 ? "" : " -> "));
		}
		return builder.toString();
	}
	
}
