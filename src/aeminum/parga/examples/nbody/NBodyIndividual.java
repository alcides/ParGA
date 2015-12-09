package aeminum.parga.examples.nbody;

import aeminum.parga.Individual;

public class NBodyIndividual extends Individual {
	
	public double[] data = new double[6];
		
	public String toString() {
		return "(" + data[0] + "," + data[1] + "," + data[2] + ") + (" + data[3] + "," + data[4] + "," + data[5] + ")";
	}
}
