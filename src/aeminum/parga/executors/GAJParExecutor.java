package aeminum.parga.executors;

import aeminium.runtime.Hints;
import aeminium.runtime.futures.codegen.ForHelper;
import aeminum.parga.GeneticAlgorithm;
import aeminum.parga.Individual;

public class GAJParExecutor<I extends Individual> extends GAAbstractExecutor<I> {
	
	@Override
	public void evolve(GeneticAlgorithm<I> ga) {
		this.ga = ga;
		this.gen = ga.createArray(ga.getPopulationSize());
		initiatePopulation();
		for (int ngen=0; ngen < ga.getNumberOfIterations(); ngen++) {
			if ((((gen.length) * 17L) + 1L) < 206189950L) {
                for (int i = 0 ; i < (gen.length) ; i++) {
                    gen[i].fitness = ga.fitness(gen[i]);
                }
            } else {
            	ForHelper.forContinuousInteger(0, gen.length, (Integer i) -> {
    				gen[i].fitness = ga.fitness(gen[i]);
    				return null;
    			}, Hints.LARGE, (gen.length) / Runtime.getRuntime().availableProcessors() + 1);
            }
            gen = sortByFitness(gen, fitnesses);
            int elite = ga.getElitismSize();
            if (((((gen.length) - (ga.getElitismSize())) * 71429L) + 2L) < 206189950L) {
                for (int i = elite ; i < (gen.length) ; i++) {
                    if ((r.nextDouble()) < (ga.getRecombinationProbability())) {
                        I[] pair = ga.recombine(tournament(), tournament(), r);
                        gen[i] = pair[0];
                    } 
                    if ((r.nextDouble()) < (ga.getMutationProbability())) {
                        gen[i] = ga.mutate(gen[i], r);
                    } 
                }
            } else {
            	ForHelper.forContinuousInteger(elite, gen.length, (Integer i) -> {
    				if (r.nextDouble() < ga.getRecombinationProbability()) {
    					I[] pair = ga.recombine(tournament(), tournament(), r);
    					gen[i] = pair[0];
    				}
    				if (r.nextDouble() < ga.getMutationProbability()) {
    					gen[i] = ga.mutate(gen[i], r);
    				}
    				return null;
    			}, Hints.SMALL, (gen.length) / Runtime.getRuntime().availableProcessors() + 1);
            }
		}
		
		for (int i = 0; i < gen.length; i++) {
			gen[i].fitness = ga.fitness(gen[i]);
		}
	}
}
