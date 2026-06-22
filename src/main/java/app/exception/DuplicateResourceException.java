package app.exception;

public class DuplicateResourceException extends ApplicationException{
    protected DuplicateResourceException(String message) {
        super(message);
    }
}
