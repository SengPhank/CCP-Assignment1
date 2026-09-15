package comp3011.assignment1.services;

import java.util.concurrent.atomic.AtomicLong;

public class StatService {
	
	// Use atomic variables in case a bunch of users decide to update inputs used
	public final AtomicLong currentInputToken = new AtomicLong(0);
	public final AtomicLong currentOutputToken = new AtomicLong(0);
	
	// Update atmoic longs
	public void addTokens(Integer inp, Integer out) {
		currentInputToken.getAndAdd(inp);
		currentOutputToken.getAndAdd(out);
	}
	
	// Get functions
	public long getInputToken() {
		return currentInputToken.get();
	}
	
	public long getOutputToken() {
		return currentOutputToken.get();
	}
}