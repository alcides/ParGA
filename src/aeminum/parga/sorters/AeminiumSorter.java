package aeminum.parga.sorters;

import java.util.Arrays;

import aeminium.runtime.Runtime;
import aeminium.runtime.Task;
import aeminium.runtime.futures.FBody;
import aeminium.runtime.futures.RuntimeManager;
import aeminum.parga.Individual;

public class AeminiumSorter<I extends Individual> implements Sorter<I> {

	@Override
	public I[] sort(I[] arr) {
		RuntimeManager.createTask(new AeminiumRecursiveSorter(arr, 0, arr.length-1)).get();
		return arr;
	}

    class AeminiumRecursiveSorter extends FBody<Void> {
    	I[] pop;
    	int st;
    	int end;
        public AeminiumRecursiveSorter(I[] pop, int st, int end) {
            this.pop = pop;
            this.st = st;
            this.end = end;
        }
        
        @Override
        public void execute(Runtime aeRuntime, Task aeTask) throws Exception {
        	if (aeRuntime.parallelize(aeTask)) {
            	final int index = partition(pop, st, end);
        		FBody<Void> s1 = null, s2 = null;
        		if (st < index - 1) {
        			s1 = RuntimeManager.createTask(new AeminiumRecursiveSorter(pop, st, index-1));
        		}
        		if (index < end) {
        			s2 = RuntimeManager.createTask(new AeminiumRecursiveSorter(pop, index, end));
        		}
        		if (s1 != null) {
        			s1.get();
        		}
        		if (s2 != null) {
        			s2.get();
        		}
        	} else {
        		Arrays.sort(pop, st, end);
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
