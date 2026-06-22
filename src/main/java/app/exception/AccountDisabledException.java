package app.exception;

public class AccountDisabledException extends ApplicationException{
    public AccountDisabledException(String message) {
        super(message);
    }
}
