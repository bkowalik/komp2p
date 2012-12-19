package client.exceptions;


public class ComException extends Exception {
    public ComException() {

    }

    public ComException(String message) {
        super(message);
    }

    public ComException(String message, Throwable cause) {
        super(message, cause);
    }
}
