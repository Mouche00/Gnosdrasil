package org.yc.gnosdrasil.gduserservice.utils.helpers;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ValidationErrorsHelper {
    // Thread-local variable to hold validation errors per thread
    private static final ThreadLocal<Map<String, Map<String, String>>> validationErrorsHolder = ThreadLocal.withInitial(HashMap::new);
    public static void setCachedValidationErrors(Map<String, Map<String, String>> errors) {
        validationErrorsHolder.set(errors);
    }

    public static Map<String, Map<String, String>> getCachedValidationErrors() {
        return validationErrorsHolder.get();
    }

    public static void clearCachedValidationErrors() {
        validationErrorsHolder.remove();
    }

    public static Map<String, String> extractFieldErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage));
    }
}
