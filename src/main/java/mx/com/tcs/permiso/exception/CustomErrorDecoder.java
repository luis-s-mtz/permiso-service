package mx.com.tcs.permiso.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import mx.com.tcs.permiso.model.client.ErrorMessageDTO;
import mx.com.tcs.permiso.model.response.ErrorDTO;
import org.apache.coyote.BadRequestException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author luis
 * @since 1.0
 *
 * Class to manage Error in FeignClient using a decoder.
 */
@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {
    /**
     * Method to manage the errors generated in FeignClient.
     *
     * @param s        The methodKey.
     * @param response The response of the FeignClient.
     * @return The exception throw.
     */
    @Override
    public Exception decode(String s, Response response) {
        ErrorMessageDTO feignClienErrorDTO = deserializeResponse(response);
        String errorMessage = feignClienErrorDTO.getError();

        switch (response.status()) {
            case 400:
                return new BadRequestException();
            case 404:
                return new ItemNotFoundException("Not found in request to FeignClient: " + errorMessage + ".");
            case 500:
                return new PermisoSrvInternalServErrorException(
                        "Response from FeignClient return an Internal Server Error: "+ errorMessage +".");
            default:
                return new PermisoSrvInternalServErrorException("Exception in request from FeignClient: "+ errorMessage +".");
        }
    }

    private ErrorMessageDTO deserializeResponse(Response response) {
        ErrorMessageDTO errorDTO = null;
        try (InputStream bodyIs = response.body().asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            errorDTO = mapper.readValue(bodyIs, ErrorMessageDTO.class);
        } catch (IOException e) {
            throw new PermisoSrvInternalServErrorException("Error when deserialize response from FeignClient: "+ e.getMessage());
        }
        return errorDTO;
    }
}
