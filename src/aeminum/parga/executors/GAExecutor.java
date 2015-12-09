package aeminum.parga.executors;

import aeminum.parga.GeneticAlgorithm;
import aeminum.parga.Individual;
import aeminum.parga.sorters.Sorter;

public interface GAExecutor<I extends Individual> {
	public void setSorter(Sorter<I> s);
	public void evolve(GeneticAlgorithm<I> ga);
	public I getBestIndividual();
}
