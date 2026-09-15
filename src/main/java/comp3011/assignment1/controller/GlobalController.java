package comp3011.assignment1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import comp3011.assignment1.responses.GlobalStatsResponse;
import comp3011.assignment1.services.StatService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class GlobalController {
	
	// StatService class
	private final StatService statService;
	
	// Constructor
	public GlobalController(StatService statService) {
		this.statService = statService;
	}
	
	/*
	Returns cumulative input and output token usage for the speech-to-text
    Cloud service since the current UTC server start. Counters reset when
    the server process restarts.
	*/

	@GetMapping("/api/v1/global/stats")
	public ResponseEntity<GlobalStatsResponse> getGlobalStats() {
		// Create message
		GlobalStatsResponse newGlobStat = new GlobalStatsResponse(
			statService.getInputToken(), 
			statService.getOutputToken()
		);
		
		return ResponseEntity.status(200).body(newGlobStat);
	}
	
}