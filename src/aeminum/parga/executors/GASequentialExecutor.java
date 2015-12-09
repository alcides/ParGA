package aeminum.parga.executors;

import aeminum.parga.GeneticAlgorithm;
import aeminum.parga.Individual;

public class GASequentialExecutor<I extends Individual> extends GAAbstractExecutor<I> {
	
	@Override
	public void evolve(GeneticAlgorithm<I> ga) {
		this.ga = ga;
		this.gen = ga.createArray(ga.getPopulationSize());
		this.fitnesses = new double[ga.getPopulationSize()];
		initiatePopulation();
		for (int ngen=0; ngen < ga.getNumberOfIterations(); ngen++) {
			for (int i = 0; i < gen.length; i++) {
				gen[i].fitness = ga.fitness(gen[i]);
			}
			
			
			gen = sortByFitness(gen, fitnesses);
			int elite = ga.getElitismSize();
			
			for (int i = elite; i < gen.length; i++) {
				if (r.nextDouble() < ga.getRecombinationProbability()) {
					I[] pair = ga.recombine(tournament(), tournament(), r);
					gen[i] = pair[0];
				}
			}
			
			for (int i = elite; i < gen.length; i++) {
				if (r.nextDouble() < ga.getMutationProbability()) {
					gen[i] = ga.mutate(gen[i], r);
				}
			}
		}
		
		for (int i = 0; i < gen.length; i++) {
			gen[i].fitness = ga.fitness(gen[i]);
		}
	}

}
