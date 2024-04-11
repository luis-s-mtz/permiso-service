package mx.com.tcs.permiso.exception;

/**
 * @author luis
 * @since 1.0
 *
 * Class to define the InternalServerErrorException used in the exception manager of the permiso API.
 */
public class PermisoSrvInternalServErrorException extends RuntimeException {
    /**
     * Constructor of PermisoSrvInternalServErrorException using the message of exception.
     *
     * @param message The message of the error
     */
    public PermisoSrvInternalServErrorException(String message) {
        super(message);
    }

    /**
     * Constructor of PermisoSrvInternalServErrorException using the message and throw cause of exception.
     *
     * @param message The message of the error.
     * @param cause The throw cause of the error.
     */
    public PermisoSrvInternalServErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
