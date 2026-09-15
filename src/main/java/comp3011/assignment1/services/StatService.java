package comp3011.assignment1.services;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

@Service
public class StatService {
	
	// Use atomic variables in case a bunch of users decide to update inputs used
	public final AtomicInteger currentInputToken = new AtomicInteger(0);
	public final AtomicInteger currentOutputToken = new AtomicInteger(0);
	
	// Update Atomic integers
	public void addTokens(Integer inp, Integer out) {
		currentInputToken.getAndAdd(inp);
		currentOutputToken.getAndAdd(out);
	}
	
	// Get functions
	public Integer getInputToken() {
		return currentInputToken.get();
	}
	
	public Integer getOutputToken() {
		return currentOutputToken.get();
	}
}