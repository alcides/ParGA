package aeminum.parga.sorters;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import aeminum.parga.Individual;

public class ForkJoinSorter<I extends Individual> implements Sorter<I> {

	@Override
	public I[] sort(I[] arr) {
		ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
		FjQuickSort t = new FjQuickSort(arr, 0, arr.length - 1, arr.length/Runtime.getRuntime().availableProcessors());
		pool.invoke(t);
		return t.population;
	}
		
	class FjQuickSort extends RecursiveAction {
	
		private static final long serialVersionUID = 1L;
		I[] population;
		int threshold;
		int left;
		int right;

		public FjQuickSort(I[] population, int left, int right, int threshold) {
			this.population = population;
			this.threshold = threshold;
			this.left = left;
			this.right = right;
		}
	
		@Override
		protected void compute() {
			if (population.length < threshold) {
				Arrays.sort(population, left, right);
				return;
			}
	
			final int index = partition(this.population, this.left, this.right);
			FjQuickSort s1 = null, s2 = null;
			if (this.left < index - 1) {
				s1 = new FjQuickSort(this.population, this.left, index - 1, threshold);
				s1.fork();
			}
	
			if (index < this.right) {
				s2 = new FjQuickSort(this.population, index, this.right, threshold);
				s2.compute();
			}
	
			if (this.left < index - 1) {
				s1.join();
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
}
