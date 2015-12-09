package aeminum.parga.executor.helpers;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.function.Consumer;

public class FJHelper {
	
	static class Looper extends RecursiveAction {
		private static final long serialVersionUID = 1L;
		
		int st;
		int end;
		Consumer<Integer> action;
		public Looper(int st, int end, Consumer<Integer> action) {
			this.st = st;
			this.end = end;
			this.action = action;
		}
		
		@Override
		protected void compute() {
			if ((end-st < 2) || getSurplusQueuedTaskCount() > 3) {
				for (int i=st; i<end; i++) {
					action.accept(i);
				}
			} else {
				int midpoint = (end-st)/2 + st;
				Looper h1 = new Looper(st, midpoint, action);
				Looper h2 = new Looper(midpoint, end, action);
				try {
					h1.fork();
					h2.fork();
					h2.get();
					h1.get();
				} catch (InterruptedException | ExecutionException | OutOfMemoryError e) {
					h1.compute();
					h2.compute();
				}
			}
		}
		
	}
	
	public static void loop(int st, int end, Consumer<Integer> action) {
		ForkJoinPool pool = ForkJoinPool.commonPool();
		try {
			pool.invoke(new Looper(st, end, action));
		} catch (OutOfMemoryError e) {
			new Looper(st, end, action).compute();
		}
	}
}
