package mx.com.tcs.permiso.model.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

/**
 * @author luis
 * @since 1.0
 *
 * Class used in the response of FeignClient when error is deserialized to manage exceptions.
 */
@Getter
@Setter
@ToString
public class ErrorMessageDTO {

    /**
     * The datetime when error happens.
     */
    @JsonProperty("timestamp")
    private Timestamp timestamp;
    /**
     * The HTTP status code of the API response.
     */
    @JsonProperty("status")
    private Integer status;
    /**
     * The error message.
     */
    @JsonProperty("error")
    private String error;
    /**
     * Path of the permiso API.
     */
    @JsonProperty("path")
    private String path;
}
