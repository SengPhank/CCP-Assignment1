package comp3011.assignment1.controller;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import comp3011.assignment1.responses.ErrorResponse;
import comp3011.assignment1.services.TranscriptionService;

@RestController
public class AudioTranscribeController {
	
	// Create transcription server classes
	private final TranscriptionService transcriptionService;
    public AudioTranscribeController(TranscriptionService transcriptionService) { this.transcriptionService = transcriptionService; }
	
	// Get audio file from the specified address
	@PostMapping("/api/v1/audio")
	public ResponseEntity<?> convertSTT(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) return ResponseEntity.badRequest().body("No audio received");
		
		String fname = file.getOriginalFilename();
		String contentType = file.getContentType();
		long fSize = file.getSize();
		
		// Try extracting audio bytes and sending the API
		try {		
			System.out.println("Got " + fname + " size " + fSize + ", format: " + contentType);
			
			// Post audio to given external API
			String rawJson = transcriptionService.sendToTranscriptService(file);
            return ResponseEntity.ok(rawJson); //
			
		} catch (Exception e) {
			// Error when extracting bytes or posting audio
			ErrorResponse newErr = new ErrorResponse(
        		Instant.now(),
        		500, 
        		"Internal Server Error", 
        		"Error attempting to extract and send audio file",
        		"/api/v1/audio"
        	);
			return ResponseEntity.status(500).body(newErr);
		}
	}	
	
}
