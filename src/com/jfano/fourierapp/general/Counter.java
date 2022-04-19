package com.jfano.fourierapp.general;

import java.util.Optional;

public class Counter {
	
	private int val = 0;
	private final int max;
	private Optional<Runnable> onTick = Optional.empty();
	
	public Counter(int max) {
		this.max = max;
	}
	
	public Counter(int max, Runnable action) {
		this(max);
		onTick = Optional.of(action);
	}
	
	public void update() {
		
		val += 1;
		
		if(val >= max) {
			onTick.ifPresent(act -> act.run());
			val = 0;
		}
		
	}

	public int getVal() {
		return val;
	}

}
