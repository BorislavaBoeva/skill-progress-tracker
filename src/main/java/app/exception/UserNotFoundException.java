package app.exception;

public class UserNotFoundException extends ApplicationException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
