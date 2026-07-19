package app.exception;

public class ExportNotFoundException extends EntityNotFoundException {
    public ExportNotFoundException(String message) {
        super(message);
    }
}
