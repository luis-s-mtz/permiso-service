package mx.com.tcs.permiso.exception;

/**
 * @author luis
 * @version 1.0
 *
 * Class to define the NotFoundException used in the exception manager of the permiso API.
 */
public class ItemNotFoundException extends RuntimeException {
    /**
     * Constructor of ItemNotFoundException using the message of exception.
     * @param message The message of the error.
     */
    public ItemNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor of ItemNotFoundException using the message and throw cause of exception.
     * @param message The message of the error.
     * @param cause   The throw cause of the error.
     */
    public ItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
