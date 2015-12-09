package aeminum.parga.executors;

import aeminum.parga.GeneticAlgorithm;
import aeminum.parga.Individual;
import aeminum.parga.executor.helpers.FJHelper;

public class GAForkJoinExecutor<I extends Individual> extends GAAbstractExecutor<I>{

	@Override
	public void evolve(GeneticAlgorithm<I> ga) {
		this.ga = ga;
		this.gen = ga.createArray(ga.getPopulationSize());
		initiatePopulation();
		for (int ngen=0; ngen < ga.getNumberOfIterations(); ngen++) {
			
			
			FJHelper.loop(0, gen.length, (i) -> {
				gen[i].fitness = ga.fitness(gen[i]);
			});
			gen = sortByFitness(gen, fitnesses);
			final int elite = ga.getElitismSize();
			
			FJHelper.loop(elite, gen.length, (i) -> {
				if (r.nextDouble() < ga.getRecombinationProbability()) {
					I[] pair = ga.recombine(tournament(), tournament(), r);
					gen[i] = pair[0];
				}
				if (r.nextDouble() < ga.getMutationProbability()) {
					gen[i] = ga.mutate(gen[i], r);
				}
			});
		}
		
		for (int i = 0; i < gen.length; i++) {
			gen[i].fitness = ga.fitness(gen[i]);
		}
	}
}
