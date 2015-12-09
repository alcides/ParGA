package aeminum.parga.examples.nbody;

import aeminium.runtime.Hints;
import aeminium.runtime.futures.codegen.ForHelper;

public class AeminiumNBodySystem extends NBodySystem {
	
	public AeminiumNBodySystem(NBody[] data) {
		super(data);
	}
	
	public void advance(double dt) {
		if (dt < 0 ) bodies = null;
		
		
		
		ForHelper.forContinuousInteger(0, bodies.length, (Integer i) -> {
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
			return null;
		}, Hints.LARGE);

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
