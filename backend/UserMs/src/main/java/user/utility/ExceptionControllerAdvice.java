package user.utility;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import user.exception.HmsExcpetion;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    private final Environment environment;

    public ExceptionControllerAdvice(Environment environment) {
        this.environment = environment;
    }

    // Handles custom HMS exceptions
    @ExceptionHandler(HmsExcpetion.class)
    public ResponseEntity<ErrorInfo> hmsExceptionHandler(HmsExcpetion e) {

        String message = environment.getProperty(
                e.getMessage(),
                "Some error occurred"
        );

        ErrorInfo error = new ErrorInfo(
                message,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handles other unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> exceptionHandler(Exception e) {

        ErrorInfo error = new ErrorInfo(
                "Some error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handles validation exceptions
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class
    })
    public ResponseEntity<ErrorInfo> handleValidationExceptions(Exception e) {

        String errorMsg;

        if (e instanceof MethodArgumentNotValidException manv) {

            errorMsg = manv.getBindingResult()
                    .getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));

        } else {

            ConstraintViolationException cve =
                    (ConstraintViolationException) e;

            errorMsg = cve.getConstraintViolations()
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
        }

        ErrorInfo error = new ErrorInfo(
                errorMsg,
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}