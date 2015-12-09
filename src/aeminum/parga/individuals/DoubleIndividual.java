package aeminum.parga.individuals;

import aeminum.parga.Individual;

public class DoubleIndividual extends Individual {
	public double value;
	
	public DoubleIndividual(double d) {
		value = d;
	}
	
	public String toString() {
		return this.value + "";
	}
}
