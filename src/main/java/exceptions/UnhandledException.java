package exceptions;

public class UnhandledException extends RuntimeException {
    public UnhandledException(Throwable cause) {
        super(cause);
    }
}
