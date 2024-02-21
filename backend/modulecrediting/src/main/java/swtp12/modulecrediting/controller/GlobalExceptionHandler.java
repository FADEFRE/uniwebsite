package swtp12.modulecrediting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import swtp12.modulecrediting.service.AuthService;
import swtp12.modulecrediting.util.IncorrectKeyOnDecryptException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex) {
        StringBuilder errorMessage = new StringBuilder();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errorMessage.append(violation.getMessage()).append("; ");
        }
        return ResponseEntity.badRequest().body(errorMessage.toString());
    }

    // Add other exception handlers if needed for different types of exceptions

    // Example for handling other exceptions
    /*
    @ExceptionHandler(OtherException.class)
    public ResponseEntity<String> handleOtherException(OtherException ex) {
        // Handle other exceptions and return an appropriate response
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
    }
    */

    @Autowired
    private AuthService authService;

    @ExceptionHandler(IncorrectKeyOnDecryptException.class)
    public ResponseEntity<String> handleConstraintViolationException(IncorrectKeyOnDecryptException ex) {
        authService.deleteRefreshCookie();
        authService.logout();
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(ex.getMessage());
    }
}

