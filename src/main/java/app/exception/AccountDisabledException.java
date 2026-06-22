package app.exception;

public class AccountDisabledException extends ApplicationException{
    protected AccountDisabledException(String message) {
        super(message);
    }
}
