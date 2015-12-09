package aeminum.parga.sorters;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import aeminum.parga.Individual;

public class ParallelSorter<I extends Individual> implements Sorter<I> {

    public static final int MAX_THREADS = Runtime.getRuntime().availableProcessors();
    static final ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
    
	@Override
	public I[] sort(I[] population) {
		sort(population, 0, population.length-1);
		return population;
	}

	private void sort(I[] population, int st, int end) {
		if (end - st < population.length/MAX_THREADS) {
			Arrays.sort(population, st, end);
		} else {
				int index = partition(population, st, end);
				Thread[] arr = new Thread[2];
				if (st < index - 1) {
					arr[0] = new Thread() {
						@Override
						public void run() {
							sort(population, st, index-1);
						}
					};
					arr[0].start();
				}
				if (index < end) {
					arr[1] = new Thread() {
						@Override
						public void run() {
							sort(population, index, end);
						}
					};
					arr[1].start();
				}
				try {
					if (st < index - 1) {
						arr[0].join();
					}
					if (index < end) {
						arr[1].join();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
		}
	}
	
	public int partition(I[] data, int left, int right) {
		int i = left;
		int j = right;
		I tmp;
		I pivot = data[(left + right) / 2];

		while (i <= j) {
			while (data[i].fitness < pivot.fitness)
				i++;
			while (data[j].fitness > pivot.fitness)
				j--;
			if (i <= j) {
				tmp = data[i];
				data[i] = data[j];
				data[j] = tmp;
				i++;
				j--;
			}
		}

		return i;
	}
}
