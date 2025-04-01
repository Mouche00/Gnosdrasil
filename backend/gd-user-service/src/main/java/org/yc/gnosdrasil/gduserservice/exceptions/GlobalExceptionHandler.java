package org.yc.gnosdrasil.gduserservice.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.yc.gnosdrasil.gduserservice.exceptions.custom.EmailAlreadyExistsException;
import org.yc.gnosdrasil.gduserservice.utils.response.ApiResponse;

import java.util.Map;

import static org.yc.gnosdrasil.gduserservice.utils.helpers.ResponseHelper.errorResponse;
import static org.yc.gnosdrasil.gduserservice.utils.helpers.ValidationErrorsHelper.extractFieldErrors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<String>> handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {
        log.warn("Email already exists: {}", e.getMessage());
        return errorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Email already exists",
                e.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> validationErrors = extractFieldErrors(e.getBindingResult());

        log.warn("Validation failed: {}", validationErrors);
        return errorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                validationErrors
        );
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneralException(Exception e) {
        log.error("Internal Server Error: {}", e.getMessage(), e);
        return errorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                e.getMessage()
        );
    }
}
