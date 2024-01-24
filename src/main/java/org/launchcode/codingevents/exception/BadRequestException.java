package org.launchcode.codingevents.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException() {

    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }

    public BadRequestException(String message, Throwable cause, boolean enableSuppression,
                               boolean writeableStackTrace) {
        super(message, cause, enableSuppression, writeableStackTrace);
    }
}