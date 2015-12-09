package aeminum.parga;

import java.util.Random;

public interface GeneticAlgorithm<I> {
	public I createRandomIndividual(Random r);
	public I mutate(I ind, Random r);
	public I[] recombine(I i1, I i2, Random r);
	public double fitness(I ind);
	
	public double getMutationProbability(); // return between 0 and 1
	public double getRecombinationProbability(); // return between 0 and 1
	public int getPopulationSize();
	public int getNumberOfIterations();
	public int getElitismSize();
	public int getTournamentSize();
	
	public I[] createArray(int n);
	
}
