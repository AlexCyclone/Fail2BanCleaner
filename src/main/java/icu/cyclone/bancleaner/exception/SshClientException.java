package icu.cyclone.bancleaner.exception;

public class SshClientException extends CleanerException {

    public SshClientException(String message) {
        super(message);
    }

    public SshClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
