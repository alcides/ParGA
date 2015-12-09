package aeminum.parga.executors;

import aeminum.parga.GeneticAlgorithm;
import aeminum.parga.Individual;

public class GAParallelExecutor<I extends Individual> extends GAAbstractExecutor<I> {
	
	public static int numberOfProcesses = Runtime.getRuntime().availableProcessors();

	@Override
	public void evolve(GeneticAlgorithm<I> ga) {
		this.ga = ga;
		this.gen = ga.createArray(ga.getPopulationSize());
		initiatePopulation();
		for (int ngen=0; ngen < ga.getNumberOfIterations(); ngen++) {
			Thread[] threads = new Thread[numberOfProcesses];
			final int span = (int) Math.ceil((gen.length) / (double)(numberOfProcesses));
			for (int tc = 0; tc < numberOfProcesses; tc++) {
				final int st = tc * span;
				threads[tc] = new Thread() {
					public void run() {
						for (int i = st; i < st + span && i < gen.length; i++) {
							gen[i].fitness = ga.fitness(gen[i]);
						}
					}
				};
				threads[tc].start();
			}
			for (int tc = 0; tc < numberOfProcesses; tc++) {
				try {
					threads[tc].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			gen = sortByFitness(gen, fitnesses);
			final int elite = ga.getElitismSize();
			
			for (int tc = 0; tc < numberOfProcesses; tc++) {
				final int spanR = (int) Math.ceil((gen.length-elite) / (double)(numberOfProcesses));
				final int st = elite + tc * spanR;
				threads[tc] = new Thread() {
					public void run() {
						for (int i = st; i < st + spanR && i < gen.length; i++) {
							if (r.nextDouble() < ga.getRecombinationProbability()) {
								I[] pair = ga.recombine(tournament(), tournament(), r);
								gen[i] = pair[0];
							}
							if (r.nextDouble() < ga.getMutationProbability()) {
								gen[i] = ga.mutate(gen[i], r);
							}
						}
					}
				};
				threads[tc].start();
			}
			for (int tc = 0; tc < numberOfProcesses; tc++) {
				try {
					threads[tc].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		for (int i = 0; i < gen.length; i++) {
			gen[i].fitness = ga.fitness(gen[i]);
		}
	}
}
