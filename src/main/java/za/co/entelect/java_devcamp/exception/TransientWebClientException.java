package za.co.entelect.java_devcamp.exception;

public class TransientWebClientException extends RuntimeException{
    public TransientWebClientException(Throwable cause) {
        super(cause);
    }

    public TransientWebClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
