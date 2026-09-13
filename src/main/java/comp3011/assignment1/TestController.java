package comp3011.assignment1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class TestController {
	
	// Test to see if server is online and taking api calls
    @GetMapping("/api/test")
    public Map<String, String> test() {
        return Map.of("Test", "Hello from backend!");
    }
}