package main.errorhandling.exceptions;

public class SemanticException extends RuntimeException {
    public SemanticException(String message) {
        super(message);
    }
}
