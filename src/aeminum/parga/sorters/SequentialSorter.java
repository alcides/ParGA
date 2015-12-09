package aeminum.parga.sorters;

import java.util.Arrays;

import aeminum.parga.Individual;

public class SequentialSorter<I extends Individual> implements Sorter<I> {

	@Override
	public I[] sort(I[] population) {
		Arrays.sort(population);
		return population;
	}

}
