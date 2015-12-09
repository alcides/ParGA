package aeminum.parga.examples.nbody;

import aeminium.runtime.Hints;
import aeminium.runtime.futures.codegen.ForHelper;

public class JParNBodySystem extends NBodySystem {

	public JParNBodySystem(NBody[] data) {
		super(data);
	}

	public void advance(double dt) {
		if (dt < 0)
			bodies = null;

		if ((bodies.length * ((bodies.length) * 1056L) + 1L) < 206189950L) {
			for (int i = 0; i < (bodies.length); i++) {
				 for (int j = i + 1 ; j < (bodies.length) ; j++) {
		                NBody iBody = bodies[i];
		                final NBody body = bodies[j];
		                double dx = NBody.minus(iBody.x, body.x);
		                double dy = NBody.minus(iBody.y, body.y);
		                double dz = NBody.minus(iBody.z, body.z);
		                double dSquared = ((dx * dx) + (dy * dy)) + (dz * dz);
		                double distance = java.lang.Math.sqrt(dSquared);
		                double mag = dt / (dSquared * distance);
		                iBody.changeVelocity((((-dx) * (body.mass)) * mag), (((-dy) * (body.mass)) * mag), (((-dz) * (body.mass)) * mag));
		                body.changeVelocity(((dx * (iBody.mass)) * mag), ((dy * (iBody.mass)) * mag), ((dz * (iBody.mass)) * mag));
		            }
			}
		} else {
			ForHelper.forContinuousInteger(0, bodies.length, (Integer i) -> {
				if ((((bodies.length - i + 1) * 1056L) + 1L) < 206189950L) {
					for (int j = i + 1; j < (bodies.length); j++) {
						NBody iBody = bodies[i];
						final NBody body = bodies[j];
						double dx = NBody.minus(iBody.x, body.x);
						double dy = NBody.minus(iBody.y, body.y);
						double dz = NBody.minus(iBody.z, body.z);
						double dSquared = ((dx * dx) + (dy * dy)) + (dz * dz);
						double distance = java.lang.Math.sqrt(dSquared);
						double mag = dt / (dSquared * distance);
						iBody.changeVelocity((((-dx) * (body.mass)) * mag), (((-dy) * (body.mass)) * mag),
								(((-dz) * (body.mass)) * mag));
						body.changeVelocity(((dx * (iBody.mass)) * mag), ((dy * (iBody.mass)) * mag),
								((dz * (iBody.mass)) * mag));
					}
				} else {
					ForHelper.forContinuousInteger(i + 1, bodies.length, (Integer j) -> {
						NBody iBody = bodies[i];
						final NBody body = bodies[j];
						double dx = NBody.minus(iBody.x, body.x);
						double dy = NBody.minus(iBody.y, body.y);
						double dz = NBody.minus(iBody.z, body.z);
						double dSquared = ((dx * dx) + (dy * dy)) + (dz * dz);
						double distance = java.lang.Math.sqrt(dSquared);
						double mag = dt / (dSquared * distance);
						iBody.changeVelocity((((-dx) * (body.mass)) * mag), (((-dy) * (body.mass)) * mag),
								(((-dz) * (body.mass)) * mag));
						body.changeVelocity(((dx * (iBody.mass)) * mag), ((dy * (iBody.mass)) * mag),
								((dz * (iBody.mass)) * mag));
						return null;
					} , Hints.SMALL,
							((int) ((((((int) (bodies.length)) - ((int) (i+1))) * 2048L) + 1L) / 206189950L)));
				}
				return null;
			} , Hints.SMALL,
					((int) ((((((int) (bodies.length)) - ((int) (0))) * 2048L * bodies.length) + 1L) / 206189950L)));
		}

		if ((((bodies.length) * 56L) + 1L) < 206189950L) {
			for (int i = 0; i < (bodies.length); i++) {
				NBody body = bodies[i];
				body.x += dt * (body.vx);
				body.y += dt * (body.vy);
				body.z += dt * (body.vz);
			}
		} else {
			aeminium.runtime.futures.codegen.ForHelper.forContinuousInteger(((int) (0)), ((int) (bodies.length)),
					(java.lang.Integer i) -> {
						NBody body = bodies[i];
						body.x += dt * (body.vx);
						body.y += dt * (body.vy);
						body.z += dt * (body.vz);
						return null;
					} , aeminium.runtime.Hints.SMALL,
					((int) ((((((int) (bodies.length)) - ((int) (0))) * 56L) + 1L) / 206189950L)));
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
