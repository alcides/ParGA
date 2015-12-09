package aeminum.parga.examples;

import java.util.Random;

import aeminum.parga.GeneticAlgorithm;
import aeminum.parga.executors.GAAeminiumExecutor;
import aeminum.parga.executors.GAExecutor;
import aeminum.parga.individuals.DoubleIndividual;
import aeminum.parga.sorters.AeminiumSorter;

public class MinimumGA implements GeneticAlgorithm<DoubleIndividual> {
	
	public double function(Double i) {
		return (i+2)*i + 4;
	}
	
	@Override
	public DoubleIndividual createRandomIndividual(Random r) {
		return new DoubleIndividual(r.nextDouble() * 200);
	}

	@Override
	public DoubleIndividual mutate(DoubleIndividual ind, Random r) {
		return new DoubleIndividual(ind.value + r.nextDouble() * 10 - 5);
	}

	@Override
	public DoubleIndividual[] recombine(DoubleIndividual i1, DoubleIndividual i2, Random r) {
		DoubleIndividual[] pair = new DoubleIndividual[2];
		pair[0] = new DoubleIndividual(i1.value - i2.value / 2);
		pair[1] = new DoubleIndividual(i2.value - i1.value / 2);
		return pair;
	}

	@Override
	public double fitness(DoubleIndividual ind) {
		return function(ind.value);
	}

	@Override
	public double getMutationProbability() {
		return 0.2;
	}

	@Override
	public double getRecombinationProbability() {
		return 0.9;
	}

	@Override
	public int getPopulationSize() {
		return 100;
	}

	@Override
	public int getNumberOfIterations() {
		return 100;
	}

	@Override
	public int getElitismSize() {
		return 10;
	}
	
	@Override
	public int getTournamentSize() {
		return 2;
	}

	@Override
	public DoubleIndividual[] createArray(int n) {
		return new DoubleIndividual[n];
	}
	
	
	public static void main(String[] args) {
		MinimumGA min = new MinimumGA();
		GAExecutor<DoubleIndividual> ex = new GAAeminiumExecutor<DoubleIndividual>();
		ex.setSorter(new AeminiumSorter<DoubleIndividual>());
		ex.evolve(min);
		System.out.println(ex.getBestIndividual());
	}

	
}
