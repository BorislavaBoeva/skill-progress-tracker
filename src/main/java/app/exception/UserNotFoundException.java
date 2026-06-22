package app.exception;

public class UserNotFoundException extends ApplicationException{
    protected UserNotFoundException(String message) {
        super(message);
    }
}
