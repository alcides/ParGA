package aeminum.parga.executors;

import aeminium.runtime.Hints;
import aeminium.runtime.futures.codegen.ForHelper;
import aeminum.parga.GeneticAlgorithm;
import aeminum.parga.Individual;

public class GAAeminiumExecutor<I extends Individual> extends GAAbstractExecutor<I> {
	@Override
	public void evolve(GeneticAlgorithm<I> ga) {
		this.ga = ga;
		this.gen = ga.createArray(ga.getPopulationSize());
		initiatePopulation();
		for (int ngen=0; ngen < ga.getNumberOfIterations(); ngen++) {
			ForHelper.forContinuousInteger(0, gen.length, (Integer i) -> {
				gen[i].fitness = ga.fitness(gen[i]);
				return null;
			}, Hints.LARGE);
			
			gen = sortByFitness(gen, fitnesses);
			final int elite = ga.getElitismSize();
			
			ForHelper.forContinuousInteger(elite, gen.length, (Integer i) -> {
				if (r.nextDouble() < ga.getRecombinationProbability()) {
					I[] pair = ga.recombine(tournament(), tournament(), r);
					gen[i] = pair[0];
				}
				if (r.nextDouble() < ga.getMutationProbability()) {
					gen[i] = ga.mutate(gen[i], r);
				}
				return null;
			}, Hints.LARGE);
		}
		
		for (int i = 0; i < gen.length; i++) {
			gen[i].fitness = ga.fitness(gen[i]);
		}
	}
}
