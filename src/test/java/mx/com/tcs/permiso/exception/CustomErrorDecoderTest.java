package mx.com.tcs.permiso.exception;

import feign.Request;
import feign.Response;
import feign.Util;
import mx.com.tcs.permiso.model.client.ErrorMessageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomErrorDecoderTest {

    @InjectMocks
    private CustomErrorDecoder customErrorDecoder;
    private Response response;
    @BeforeEach
    void setUp() {
        response = getInternalServError();
    }

    @DisplayName("Test decode when feign client response is 500 Internal Server Error.")
    @Test
    void testDecodeInternalServerError() {
        Throwable exception = customErrorDecoder.decode("UserTypeFeignClient", response);
        assertTrue(
                exception.getMessage().contains(
                        "Response from FeignClient return an Internal Server Error"),
                "Error when get Internal Server exception message");
    }

    @DisplayName("Test decode when feign client response is 501 Not Implemented and throws default.")
    @Test
    void testDecodeDefault() {
        // Given
        response = getNotImplementedResp();

        // When
        Throwable exception = customErrorDecoder.decode("UserTypeFeignClient", response);

        // Then
        assertTrue(
                exception.getMessage().contains(
                        "Exception in request from FeignClient:"),
                "Error when get Internal Server exception message");
    }

    @DisplayName("Test decode when feign client response is 404 Not Found Error.")
    @Test
    void testDecodeNotFound() {
        response = getNotFoundError();
        Throwable exception = customErrorDecoder.decode("UserTypeFeignClient", response);
        assertTrue(
                exception.getMessage().contains(
                        "Not found in request to FeignClient:"),
                "Error when get Internal Server exception message");
    }

    @DisplayName("Test decode when feign client response is 400 Bad Request.")
    @Test
    void testDecodeBadRequest() {
        response = getBadRequestResp();
        Throwable exception = customErrorDecoder.decode("UserTypeFeignClient", response);
        assertTrue(
                exception.getMessage().contains(
                        "Bad Request Error"),
                "Error when get Internal Server exception message");
    }

    @DisplayName("Test decode when deserialized has error.")
    @Test
    void testDecodeWhenDeserializedHasError() {
        // Given
        response = getMalformedResp();

        // When and Then
        assertThrows(PermisoSrvInternalServErrorException.class,
                () ->  customErrorDecoder.decode("UserTypeFeignClient", response));
    }

    private Response getInternalServError() {
        Map<String, Collection<String>> headers = new LinkedHashMap<>();

        String bdy = "{\n" +
                "    \"timestamp\": \"2024-05-01T02:16:17.4592107\",\n" +
                "    \"status\": 500,\n" +
                "    \"error\": \"Internal Server Error: error when connect to database.\",\n" +
                "    \"path\": \"/api/tipousuario\"\n" +
                "}";

        return Response.builder()
                .status(500)
                .reason("Internal server error when call UserType service.")
                .request(
                        Request.create(
                                Request.HttpMethod.GET, "/api/tipousuario",
                                Collections.emptyMap(), null, Util.UTF_8))
                .headers(headers)
                .body(bdy,Util.UTF_8)
                .build();
    }

    private Response getNotImplementedResp() {
        Map<String, Collection<String>> headers = new LinkedHashMap<>();

        String bdy = "{\n" +
                "    \"timestamp\": \"2024-05-01T02:16:17.4592107\",\n" +
                "    \"status\": 501,\n" +
                "    \"error\": \"Not implemented: error when request service.\",\n" +
                "    \"path\": \"/api/tipousuario\"\n" +
                "}";

        return Response.builder()
                .status(501)
                .reason("Not implemented.")
                .request(
                        Request.create(
                                Request.HttpMethod.GET, "/api/tipousuario",
                                Collections.emptyMap(), null, Util.UTF_8))
                .headers(headers)
                .body(bdy,Util.UTF_8)
                .build();
    }

    private Response getNotFoundError() {
        Map<String, Collection<String>> headers = new LinkedHashMap<>();

        String bdy = "{\n" +
                "    \"timestamp\": \"2024-05-01T02:16:17.4592107\",\n" +
                "    \"status\": 404,\n" +
                "    \"error\": \"Item(s) not found: The row in table TipoUsuario with id 3 is not found.\",\n" +
                "    \"path\": \"/api/tipousuario\"\n" +
                "}";

        return Response.builder()
                .status(404)
                .reason("Not Found Exception when call feign client.")
                .request(
                        Request.create(
                                Request.HttpMethod.GET, "/api/tipousuario",
                                Collections.emptyMap(), null, Util.UTF_8))
                .headers(headers)
                .body(bdy,Util.UTF_8)
                .build();
    }

    private Response getBadRequestResp() {
        Map<String, Collection<String>> headers = new LinkedHashMap<>();

        String bdy = "{\n" +
                "    \"timestamp\": \"2024-05-01T02:16:17.4592107\",\n" +
                "    \"status\": 400,\n" +
                "    \"error\": \"Bad Request: id is not found.\",\n" +
                "    \"path\": \"/api/tipousuario\"\n" +
                "}";

        return Response.builder()
                .status(400)
                .reason("Bad Request when call feign client.")
                .request(
                        Request.create(
                                Request.HttpMethod.GET, "/api/tipousuario",
                                Collections.emptyMap(), null, Util.UTF_8))
                .headers(headers)
                .body(bdy,Util.UTF_8)
                .build();
    }

    private Response getMalformedResp() {
        Map<String, Collection<String>> headers = new LinkedHashMap<>();

        ErrorMessageDTO errorMessDTO = new ErrorMessageDTO();

        return Response.builder()
                .status(404)
                .reason("Not Found Exception when call feign client.")
                .request(
                        Request.create(
                                Request.HttpMethod.GET, "/api/tipousuario",
                                Collections.emptyMap(), null, Util.UTF_8))
                .headers(headers)
                .body(errorMessDTO.toString().getBytes(Util.UTF_8))
                .build();
    }
}