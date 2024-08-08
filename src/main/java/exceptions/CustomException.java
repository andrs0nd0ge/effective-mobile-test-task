package exceptions;

public class CustomException extends RuntimeException {
    public CustomException(Throwable cause) {
        super(cause);
    }
}
