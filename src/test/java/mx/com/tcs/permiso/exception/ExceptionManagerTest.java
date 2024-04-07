package mx.com.tcs.permiso.exception;

import mx.com.tcs.permiso.model.response.ErrorDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExceptionManagerTest {

    @InjectMocks
    private ExceptionManager exceptionManager;

    @BeforeEach
    void setUp() {
    }

    @DisplayName("Test manage of exception ItemNotFoundException OK.")
    @Test
    void manageNotFoundException() {
        // Given

        // when
        ResponseEntity<ErrorDTO> responseEntity = exceptionManager.manageNotFoundException(
                new ItemNotFoundException("Esta es una exception por pruebas..."));

        // Then
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCode().value());
    }

    @DisplayName("Test manage of exception ItemNotFoundException passing couple of params OK.")
    @Test
    void manageNotFoundExceptionWithTwoParams() {
        // Given

        // when
        ResponseEntity<ErrorDTO> responseEntity = exceptionManager.manageNotFoundException(
                new ItemNotFoundException("Esta es una exception por pruebas...",new Throwable()));

        // Then
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCode().value());
    }

    @DisplayName("Test manage of exception PermisoServInternalServErrorException passing couple of params OK.")
    @Test
    void manageInternalServErrorException() {
        // Given

        // when
        ResponseEntity<ErrorDTO> responseEntity = exceptionManager.manageInteranlServErrorExcpt(
                new PermisoSrvInternalServErrorException("Esta es una exception por pruebas..."));

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCode().value());
    }

    @DisplayName("Test manage of exception PermisoServInternalServErrorException passing couple of params OK.")
    @Test
    void manageInternalServErrorExceptionWithTwoParams() {
        // Given

        // when
        ResponseEntity<ErrorDTO> responseEntity = exceptionManager.manageInteranlServErrorExcpt(
                new PermisoSrvInternalServErrorException("Esta es una exception por pruebas...",new Throwable()));

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCode().value());
    }
}