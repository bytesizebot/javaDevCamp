package za.co.entelect.java_devcamp.exception;

public class IncorrectPasswordException extends RuntimeException {
    public IncorrectPasswordException(String message) { super(message); }
}