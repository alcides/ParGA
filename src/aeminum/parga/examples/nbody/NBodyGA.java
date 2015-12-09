package aeminum.parga.examples.nbody;

import java.util.Arrays;
import java.util.Random;

import aeminium.runtime.futures.RuntimeManager;
import aeminum.parga.GeneticAlgorithm;
import aeminum.parga.executors.GAExecutor;
import aeminum.parga.executors.GAJParExecutor;
import aeminum.parga.sorters.JParSorter;

public class NBodyGA implements GeneticAlgorithm<NBodyIndividual> {
	
	public static int N = 5000;
	public static int ITERATIONS = 3;
	public static NBody[] data = NBodySystem.generateRandomBodies(N, 1L);
	
	
	String version = "sequential";
	
	public NBodyGA(String v) {
		version = v;
	}
	
	
	@Override
	public NBodyIndividual createRandomIndividual(Random r) {
		NBodyIndividual ind = new NBodyIndividual();
		for (int i=0; i<6; i++)
			ind.data[i] = NBody.getRandomCoordinate(r);
		return ind;
	}

	@Override
	public NBodyIndividual mutate(NBodyIndividual original, Random r) {
		NBodyIndividual ind = new NBodyIndividual();
		int o = r.nextInt(6);
		for (int i=0; i<6; i++) {
			if (i == o) {
				ind.data[i] = NBody.getRandomCoordinate(r);
			} else {
				ind.data[i] = original.data[i];
			}
		}
		return ind;
	}

	@Override
	public NBodyIndividual[] recombine(NBodyIndividual i1, NBodyIndividual i2, Random r) {
		NBodyIndividual[] pair = new NBodyIndividual[2];
		pair[0] = new NBodyIndividual();
		pair[1] = new NBodyIndividual();
		
		int cutPoint = r.nextInt(6);
		for (int i=0; i<6; i++) {
			if (i < cutPoint) {
				pair[0].data[i] = i1.data[i];
				pair[1].data[i] = i2.data[i];
			} else {
				pair[0].data[i] = i2.data[i];
				pair[1].data[i] = i1.data[i];
			}
		}
		return pair;
	}

	@Override
	public double fitness(NBodyIndividual ind) {
		NBody[] bodies = Arrays.copyOf(data, N);
		bodies[0].x = ind.data[0];
		bodies[0].y = ind.data[1];
		bodies[0].z = ind.data[2];
		bodies[0].vx = ind.data[3];
		bodies[0].vy = ind.data[4];
		bodies[0].vz = ind.data[5];
		NBodySystem s = null;
		if (version.equals("sequential")) s = new NBodySystem(bodies);
		if (version.equals("parallel")) s = new ParallelNBodySystem(bodies);
		if (version.equals("forkjoin")) s = new ForkJoinNBodySystem(bodies);
		if (version.equals("aeminium")) s = new AeminiumNBodySystem(bodies);
		if (version.equals("jpar")) s = new JParNBodySystem(bodies);
		for (int i=0; i<ITERATIONS; i++)
			s.advance(0.5);
		return s.energy();
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
		return 64;
	}

	@Override
	public int getNumberOfIterations() {
		return 10;
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
	public NBodyIndividual[] createArray(int n) {
		return new NBodyIndividual[n];
	}
	
	
	public static void main(String[] args) {
		RuntimeManager.init();
		NBodyGA min = new NBodyGA("jpar");
		GAExecutor<NBodyIndividual> ex = new GAJParExecutor<NBodyIndividual>();
		ex.setSorter(new JParSorter<NBodyIndividual>());
		ex.evolve(min);
		System.out.println(ex.getBestIndividual() + " => " + ex.getBestIndividual().fitness);
		RuntimeManager.shutdown();
	}

	
}
