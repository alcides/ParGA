package aeminum.parga.examples.nbody;

import aeminium.runtime.futures.RuntimeManager;
import aeminum.parga.executors.GAAeminiumExecutor;
import aeminum.parga.executors.GAExecutor;
import aeminum.parga.executors.GAForkJoinExecutor;
import aeminum.parga.executors.GAJParExecutor;
import aeminum.parga.executors.GAParallelExecutor;
import aeminum.parga.executors.GASequentialExecutor;
import aeminum.parga.sorters.AeminiumSorter;
import aeminum.parga.sorters.ForkJoinSorter;
import aeminum.parga.sorters.JParSorter;
import aeminum.parga.sorters.ParallelSorter;
import aeminum.parga.sorters.SequentialSorter;

public class Runner {
	private static final long NPS = (1000L * 1000 * 1000);
	
	public static void main(String[] args) {
		
		int exe = Integer.parseInt(args[0]);
		int sort = Integer.parseInt(args[1]);
		int nbody = Integer.parseInt(args[2]);
		String nbodyCode = "sequential";
		if (nbody == 2) nbodyCode = "parallel";
		if (nbody == 3) nbodyCode = "forkjoin";
		if (nbody == 4) nbodyCode = "aeminium";
		if (nbody == 5) nbodyCode = "jpar";
		
		if (exe >= 4 || sort >= 4 || nbody >= 4) {
			RuntimeManager.init();	
		}
		long t = System.nanoTime();
		
		
		NBodyGA min = new NBodyGA(nbodyCode);
		GAExecutor<NBodyIndividual> ex;
		ex = new GASequentialExecutor<NBodyIndividual>();
		if (exe == 2) ex = new GAParallelExecutor<NBodyIndividual>();
		if (exe == 3) ex = new GAForkJoinExecutor<NBodyIndividual>();
		if (exe == 4) ex = new GAAeminiumExecutor<NBodyIndividual>();
		if (exe == 5) ex = new GAJParExecutor<NBodyIndividual>();
		
		if (sort == 1) ex.setSorter(new SequentialSorter<NBodyIndividual>());
		if (sort == 2) ex.setSorter(new ParallelSorter<NBodyIndividual>());
		if (sort == 3) ex.setSorter(new ForkJoinSorter<NBodyIndividual>());
		if (sort == 4) ex.setSorter(new AeminiumSorter<NBodyIndividual>());
		if (sort == 5) ex.setSorter(new JParSorter<NBodyIndividual>());
		
		ex.evolve(min);
		double v = (System.nanoTime() - t) * 1.0 / NPS;
		//System.out.println(ex.getBestIndividual() + " => " + ex.getBestIndividual().fitness);
		if (exe >= 4 || sort >= 4 || nbody >= 4) {
			RuntimeManager.shutdown();	
		}
		System.out.println(v);
	}
}
