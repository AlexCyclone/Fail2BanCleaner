package icu.cyclone.bancleaner.exception;

public class CleanerException extends RuntimeException {

    public CleanerException(String message) {
        super(message);
    }

    public CleanerException(String message, Throwable cause) {
        super(message, cause);
    }
}
