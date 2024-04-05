package mx.com.tcs.permiso.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author luis
 * @since 1.0
 *
 * Class ErrorDTO used in the response entity when throws a error in the API of permiso.
 */
@Getter
@Setter
@ToString
public class ErrorDTO {
    /**
     * The datetime when error happens.
     */
    private LocalDateTime timestamp;
    /**
     * The HTTP status code of the API response.
     */
    private Integer status;
    /**
     * The error message.
     */
    private String error;
    /**
     * Path of the permiso API.
     */
    private String path;
}
