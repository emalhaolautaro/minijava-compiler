package main.errorhandling.exceptions;

public class CodeGenerationException extends RuntimeException {
    public CodeGenerationException(String message) {
        super(message);
    }
}
