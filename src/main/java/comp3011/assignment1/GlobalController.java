package comp3011.assignment1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class GlobalController {
	
	/*
	Returns cumulative input and output token usage for the speech-to-text
    Cloud service since the current UTC server start. Counters reset when
    the server process restarts.
	*/
	@GetMapping("/api/v1/global/stats")
	public ResponseEntity<GlobalStatsResponse> getGlobalStats(HttpServletRequest req) {
		// Arbitrary for now
		GlobalStatsResponse newGlobStat = new GlobalStatsResponse(
				67, 
				420
				);
		return ResponseEntity.status(200).body(newGlobStat);
	}
	
}