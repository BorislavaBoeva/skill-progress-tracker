package app.exception;

public class ExportGenerationException extends ApplicationException{
    public ExportGenerationException(String message, Throwable cause) {
        super(message);
        initCause(cause);
    }
}
