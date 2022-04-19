package com.jfano.fourierapp.math;

public final class CurveCommon {
	
	public static final int factorial(int n) {
		
		if(n < 2) return 1;
		else return n * factorial(n - 1);
		
	}
	
	public static final double combination(int pool, int takenPer) {
		
		return factorial(pool) / (factorial(takenPer) - factorial(pool - takenPer));
		
	}
	
	public static final double deriveBernstein(double time, int power, int coefficient) {
		
		return combination(power, coefficient) * Math.pow(time, coefficient) * Math.pow(1 - time, power - coefficient);
		
	}

}
