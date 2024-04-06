package mx.com.tcs.permiso.exception;

import lombok.extern.slf4j.Slf4j;
import mx.com.tcs.permiso.model.response.ErrorDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

/**
 * @author luis
 * @since 1.0
 *
 * Class used to manage the exceptions throw in the permiso API.
 */
@Slf4j
@ControllerAdvice
public class ExceptionManager {

    private static final String MESSAGE_NOT_FOUND = "Item(s) not found: ";
    private static final String NOTFOUND_EXPT_CLASSNAME = "ItemNotFoundException";
    private static final String MESSAGE_INTERNAL_SERV_ERR = "Internal Server Error: ";
    private static final String INTERNALSERVERR_EXPT_CLASSNAME = "PermisoSrvInternalServErrorException";

    @Value("${permiso.api.path}")
    private String permisoApiPath;

    /**
     * Method used to manage the exception NotFoundException when the query not contains coincidences.
     * @param ex The Exception throws in the service.
     * @return The response with ErrorDTO object filled with information about the exception.
     */

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> manageNotFoundException(ItemNotFoundException ex) {
        log.error("Error [{}]: {}",NOTFOUND_EXPT_CLASSNAME,ex.getMessage());
        ErrorDTO error = buildErrorMessage(
                HttpStatus.NOT_FOUND.value(),MESSAGE_NOT_FOUND,ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Method used to manage the exception InternalServerErrorException when the database throw a exception.
     *
     * @param ex The Exception throws in the service.
     * @return The response with ErrorDTO object filled with information about the exception.
     */
    @ExceptionHandler
    public ResponseEntity<ErrorDTO> manageInteranlServErrorExcpt(PermisoSrvInternalServErrorException ex) {
        log.error("Error [{}]: {}",INTERNALSERVERR_EXPT_CLASSNAME,ex.getMessage());
        ErrorDTO error = buildErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),MESSAGE_INTERNAL_SERV_ERR,ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Method to create a ErrorDTO using the message of the exception error.
     *
     * @param message The detail message error.
     * @return DTO object with error attributes.
     */
    private ErrorDTO buildErrorMessage(int status, String error, String message) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setStatus(status);
        errorDTO.setTimestamp(LocalDateTime.now());
        errorDTO.setPath(permisoApiPath);
        errorDTO.setError(error.concat(message));

        return errorDTO;
    }
}
