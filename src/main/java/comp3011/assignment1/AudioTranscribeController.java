package comp3011.assignment1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AudioTranscribeController {
	
	// Get audio file from the specified address
	@PostMapping("/api/v1/audio")
	public ResponseEntity<String> convertSTT(@RequestParam("file") MultipartFile file) {
			return ResponseEntity.status(403).body("TBA");
	}
}
