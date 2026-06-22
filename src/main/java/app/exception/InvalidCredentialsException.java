package app.exception;

public class InvalidCredentialsException extends ApplicationException{
    protected InvalidCredentialsException(String message) {
        super(message);
    }
}
