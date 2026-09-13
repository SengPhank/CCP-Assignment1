package comp3011.assignment1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	// Catch internal server 500 errors
	@ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericError(Exception ex, HttpServletRequest req) {
		String readableMsg = (ex.getMessage() != null) ? ex.getMessage() : "No human readable msg found for this error";
		
        ErrorResponse newErr = new ErrorResponse(
        		Instant.now(),
        		500, 
        		"Internal Server Error", 
        		readableMsg,
        		req.getRequestURI()
        	);
        return ResponseEntity.status(500).body(newErr);
    }
}