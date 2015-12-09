package aeminum.parga.sorters;

import aeminum.parga.Individual;

public interface Sorter<I extends Individual> {
	public I[] sort(I[] arr);
}
