package com.github.mouse0w0.ecs.system;

public class SystemRegistrationException extends RuntimeException {

    public SystemRegistrationException(String message) {
        super(message);
    }

    public SystemRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
