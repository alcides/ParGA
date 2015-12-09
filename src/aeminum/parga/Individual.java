package aeminum.parga;

public class Individual implements Comparable<Individual> {
	public double fitness;

	@Override
	public int compareTo(Individual o) {
		return Double.compare(this.fitness, o.fitness);
	}
	
	
}
