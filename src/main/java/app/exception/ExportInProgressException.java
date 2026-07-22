package app.exception;

public class ExportInProgressException extends ApplicationException {
    public ExportInProgressException(String message) {
        super(message);
    }
}