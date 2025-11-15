package backend.grupo73.gateway.config.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceError.class)
    public ResponseEntity<Map<String, Object>> handleServiceError(ServiceError ex) {

        HttpStatus status = HttpStatus.valueOf(ex.getHttpCode());

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", ex.getHttpCode());
        errorDetails.put("message", ex.getMessage());

        return ResponseEntity.status(status).body(errorDetails);
    }

}
