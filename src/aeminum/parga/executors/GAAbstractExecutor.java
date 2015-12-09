package aeminum.parga.executors;

import java.util.concurrent.ThreadLocalRandom;

import aeminum.parga.GeneticAlgorithm;
import aeminum.parga.Individual;
import aeminum.parga.sorters.SequentialSorter;
import aeminum.parga.sorters.Sorter;

public abstract class GAAbstractExecutor<I extends Individual> implements GAExecutor<I> {
	public I[] gen;
	public double[] fitnesses;
	public GeneticAlgorithm<I> ga;
	public ThreadLocalRandom r = ThreadLocalRandom.current();
	
	public Sorter<I> sorter = new SequentialSorter<I>();
	
	@Override
	public I getBestIndividual() {
		return gen[0];
	}
	
	protected void initiatePopulation() {
		for (int i=0; i< ga.getPopulationSize(); i++) {
			gen[i] = ga.createRandomIndividual(r);
		}
	}

	protected I[] sortByFitness(I[] gen2, double[] fitnesses) {
		return sorter.sort(gen2);
	}

	public void setSorter(Sorter<I> s) {
		sorter = s;
	}
	
	public I tournament() {
		I first = gen[r.nextInt(gen.length)];
		for (int i=1; i<ga.getTournamentSize(); i++) {
			I other = gen[r.nextInt(gen.length)];
			if (other.fitness < first.fitness)
				first = other;
		}	
		return first;
	}
}
