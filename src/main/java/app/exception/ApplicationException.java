package app.exception;

public class ApplicationException extends RuntimeException{
    protected ApplicationException(String message) {
        super(message);
    }
}
