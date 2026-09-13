package comp3011.assignment1;

import org.springframework.http.HttpStatus;
import java.time.Instant;

public class ErrorExceptionHandler {
	
	// Turns errors into a valid errorResponse
    public ErrorResponse getErrorMessage(HttpStatus httpState, String humanReadableErr, String path) {
        ErrorResponse newErr = new ErrorResponse(
        		Instant.now(), 
        		httpState.value(), 
        		httpState.getReasonPhrase(), 
        		humanReadableErr, 
        		path);
        return newErr;
    }
}