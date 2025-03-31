package org.yc.gnosdrasil.gdpromptprocessingservice.exception;

public class NLPProcessingException extends RuntimeException {
    public NLPProcessingException(String message) {
        super(message);
    }

    public NLPProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
} 