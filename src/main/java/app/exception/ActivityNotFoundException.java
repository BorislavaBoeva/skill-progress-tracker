package app.exception;

public class ActivityNotFoundException extends ApplicationException{
    protected ActivityNotFoundException(String message) {
        super(message);
    }
}
