package mx.com.tcs.permiso.exception;

import lombok.extern.slf4j.Slf4j;
import mx.com.tcs.permiso.model.response.ErrorDTO;
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

    /**
     * Method used to manage the exception NotFoundException when the query not contains coincidences.
     * @param ex The Exception throws in the service.
     * @return The response with ErrorDTO object filled with information about the exception.
     */
    @ExceptionHandler
    public ResponseEntity<ErrorDTO> manageNotFoundException(ItemNotFoundException ex) {
        log.error("Error [{}]: {}",NOTFOUND_EXPT_CLASSNAME,ex.getMessage());
        ErrorDTO error = buildErrorMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    private ErrorDTO buildErrorMessage(String message) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setStatus(HttpStatus.NOT_FOUND.value());
        errorDTO.setTimestamp(LocalDateTime.now());
        errorDTO.setPath("/permiso");
        errorDTO.setError(MESSAGE_NOT_FOUND+message);

        return errorDTO;
    }
}
