package aeminum.parga.sorters;

import java.util.Arrays;

import aeminium.runtime.futures.FBody;
import aeminium.runtime.futures.RuntimeManager;
import aeminum.parga.Individual;

public class JParSorter<I extends Individual> implements Sorter<I> {

	@Override
	public I[] sort(I[] arr) {
		sort(arr, 0, arr.length-1);
		return arr;
	}
	
	public void aeminium_seq_sort(I[] population, int st, int end) {
		Arrays.sort(population, st, end+1);
	}
	
	public void sort(I[] pop, int st, int end) {
		final int index = partition(pop, st, end);
		FBody<Void> f1 = RuntimeManager.createTask(new Quicksort_par(pop, st, index-1));
		FBody<Void> f2 = RuntimeManager.createTask(new Quicksort_par(pop, index, end));
		f1.get();
		f2.get();
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

    class Quicksort_par extends FBody<Void> {
    	I[] pop;
    	int st;
    	int end;
        public Quicksort_par(I[] pop, int st, int end) {
            this.pop = pop;
            this.st = st;
            this.end = end;
        }
        
        public void execute(aeminium.runtime.Runtime aeRuntime, aeminium.runtime.Task aeTask) throws java.lang.Exception {
            if (aeRuntime.parallelize(aeTask))
            	sort(pop, st, end); 
            else
           		aeminium_seq_sort(pop, st, end);
        }
    }

}
