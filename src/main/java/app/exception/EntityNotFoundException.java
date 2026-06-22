package app.exception;

public class EntityNotFoundException extends ApplicationException{
    protected EntityNotFoundException(String message) {
        super(message);
    }
}
