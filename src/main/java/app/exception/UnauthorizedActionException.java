package app.exception;

public class UnauthorizedActionException extends ApplicationException{
    protected UnauthorizedActionException(String message) {
        super(message);
    }
}
