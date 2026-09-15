package comp3011.assignment1.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TranscriptionService {
	
	// OPENAPI private key
	@Value("${openai.api.key}")
    private String apiKey;
	
	// restClient for sending API requests
	private final RestClient restClient = RestClient.create();		
		
		//Post audio file to https://api.openai.com/v1/audio/transcriptions
	public String sendToTranscriptService(MultipartFile file) throws Exception {
		
		// Get audio file resource ready
		ByteArrayResource fileResources = new ByteArrayResource(file.getBytes()) {
			@Override
			public String getFilename() {
				return file.getOriginalFilename() != null ? file.getOriginalFilename() : "audio.webm";
			}
		};
		
		// Build body
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", fileResources);
		body.add("model", "gpt-4o-mini-transcribe");
				
		// Send post request to API
		String resp = restClient.post()
             .uri("https://api.openai.com/v1/audio/transcriptions")
             .header("Authorization", "Bearer " + apiKey)
             .contentType(MediaType.MULTIPART_FORM_DATA)
             .body(body)
             .retrieve()
             .body(String.class);
		
		System.err.println("RAW OPENAI RESPONSE: " + resp);
		return resp;
	}
}