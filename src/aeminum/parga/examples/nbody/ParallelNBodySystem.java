package aeminum.parga.examples.nbody;

public class ParallelNBodySystem extends NBodySystem {

	public ParallelNBodySystem(NBody[] data) {
		super(data);
	}
	
	public void advance(double dt) {
		if (dt < 0 ) bodies = null;
		int numberOfProcesses = Runtime.getRuntime().availableProcessors();
		Thread[] threads = new Thread[numberOfProcesses];
		final int spanR = (int) Math.ceil((bodies.length) / (double)(numberOfProcesses));
		for (int t=0; t<numberOfProcesses; t++) {
			final int tc = t;
			threads[t] = new Thread() {
				public void run() {
					for (int i=tc*spanR; i < (tc+1)*spanR; i++) {
						for (int j = i + 1; j < bodies.length; j++) {
							NBody iBody = bodies[i];
							final NBody body = bodies[j];
							double dx = NBody.minus(iBody.x, body.x);
							double dy = NBody.minus(iBody.y, body.y);
							double dz = NBody.minus(iBody.z, body.z);
							
							double dSquared = dx * dx + dy * dy + dz * dz;
							double distance = Math.sqrt(dSquared);
							double mag = dt / (dSquared * distance);

							iBody.changeVelocity(-dx * body.mass * mag, 
									-dy * body.mass * mag,
									-dz * body.mass * mag);
							
							body.changeVelocity(dx * iBody.mass * mag,
									dy * iBody.mass * mag, 
									dz * iBody.mass * mag);
						}
					}
				}
			};
			threads[t].start();
		}
		for (int t=0; t<numberOfProcesses; t++) {
			if (threads[t] != null)
				try {
					threads[t].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}

		for (int i = 0; i < bodies.length; i++) {
			NBody body= bodies[i];
			body.x += dt * body.vx;
			body.y += dt * body.vy;
			body.z += dt * body.vz;
		}
	}

	public double energy() {
		double dx, dy, dz, distance;
		double e = 0.0;

		for (int i = 0; i < bodies.length; ++i) {
			NBody iBody = bodies[i];
			e += 0.5 * iBody.mass * (iBody.vx * iBody.vx + iBody.vy * iBody.vy + iBody.vz * iBody.vz);

			for (int j = i + 1; j < bodies.length; ++j) {
				NBody jBody = bodies[j];
				dx = iBody.x - jBody.x;
				dy = iBody.y - jBody.y;
				dz = iBody.z - jBody.z;

				distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
				e -= (iBody.mass * jBody.mass) / distance;
			}
		}
		return e;
	}

}
