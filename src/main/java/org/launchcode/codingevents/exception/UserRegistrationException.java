package org.launchcode.codingevents.exception;

public class UserRegistrationException extends RuntimeException {
    public  UserRegistrationException() {

    }

    public UserRegistrationException(String message) {
        super(message);
    }

    public UserRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserRegistrationException(Throwable cause) {
        super(cause);
    }

    public UserRegistrationException(String message, Throwable cause, boolean enableSuppression,
                                     boolean writeableStackTrace) {
        super(message, cause, enableSuppression, writeableStackTrace);
    }
}
