package aeminum.parga.examples.nbody;

import java.util.Random;

public class NBody {
	static final double PI = 3.141592653589793;
	static final double SOLAR_MASS = 4 * PI * PI;
	
	public double x;
	public double y;
	public double z;
	public double vx;
	public double vy;
	public double vz;
	public double mass;
	
	
	public static double getRandomCoordinate(Random r) {
		return r.nextDouble() * 20000 - 10000;
	}
	
	public NBody(Random r) {
		x = NBody.getRandomCoordinate(r);
		y = NBody.getRandomCoordinate(r);
		z = NBody.getRandomCoordinate(r);
		vx = NBody.getRandomCoordinate(r);
		vy = NBody.getRandomCoordinate(r);
		vz = NBody.getRandomCoordinate(r);
		mass = NBody.getRandomCoordinate(r);
	}
	
	NBody offsetMomentum(double px, double py, double pz) {
		vx = -px / SOLAR_MASS;
		vy = -py / SOLAR_MASS;
		vz = -pz / SOLAR_MASS;
		return this;
	}
	
	public void changeVelocity(double vx, double vy, double vz) {
		this.vx += vx;
		this.vy += vy;
		this.vz += vz;
	}
	
	public static double minus(double a, double b) {
		return a-b;
	}
}
