package app.exception;

public class InvalidCredentialsException extends ApplicationException{
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
