package comp3011.assignment1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class AdminController {
		
	// When did this server start running?
	private final Instant serverStartUTC = Instant.now();
	private final long serverStartMilli = System.currentTimeMillis();
	
    @GetMapping("/api/v1/admin/uptime")
    public UptimeResponse getServerUpTime( ) {
    	
    	long nowMilli = System.currentTimeMillis();
    	double timeElapsed = (nowMilli - serverStartMilli) / 1000.0;
        return new UptimeResponse(serverStartUTC, Instant.now(), timeElapsed);
    }
    
    @GetMapping("/api/v1/admin/shutdown")
    public ShutdownResponse reqShutdown() {
    	
    	// Shutdown server TODO
    	
    	return new ShutdownResponse("Graceful shutdown requested");
    }
}