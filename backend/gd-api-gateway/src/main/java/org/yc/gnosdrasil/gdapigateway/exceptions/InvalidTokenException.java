package org.yc.gnosdrasil.gdapigateway.exceptions;

import org.springframework.security.core.AuthenticationException;

public class InvalidTokenException extends AuthenticationException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
