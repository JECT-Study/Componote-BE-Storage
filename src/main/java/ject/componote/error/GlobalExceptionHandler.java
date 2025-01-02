package ject.componote.error;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(final ConstraintViolationException exception) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(ErrorResponse.of(BAD_REQUEST, exception));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(ErrorResponse.of(BAD_REQUEST, String.join(", ", getFieldErrorMessage(exception))));
    }

    @ExceptionHandler(AmazonS3Exception.class)
    public ResponseEntity<ErrorResponse> handleAmazonS3Exception(final AmazonS3Exception exception) {
        return ResponseEntity.status(exception.getStatusCode())
                .body(ErrorResponse.of(HttpStatus.valueOf(exception.getStatusCode()), exception));
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ErrorResponse> handleStorageException(final StorageException exception) {
        return ResponseEntity.status(exception.getStatus())
                .body(ErrorResponse.of(exception.getStatus(), exception));
    }

    private List<String> getFieldErrorMessage(final MethodArgumentNotValidException exception) {
        return exception.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    final String fieldName = ((FieldError) error).getField();
                    final String message = error.getDefaultMessage();
                    return fieldName + ": " + message;
                })
                .toList();
    }
}
