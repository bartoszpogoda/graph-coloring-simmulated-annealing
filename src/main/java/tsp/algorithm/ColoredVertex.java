package tsp.algorithm;

public class ColoredVertex {

	private int color;

	public ColoredVertex(int color) {
		this.color = color;
	}

	public int getColor() {
		return color;
	}

	@Override
	public String toString() {
		return Integer.toString(color);
	}
}
