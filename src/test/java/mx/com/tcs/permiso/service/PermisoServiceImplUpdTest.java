package mx.com.tcs.permiso.service;

import mx.com.tcs.permiso.exception.PermisoSrvInternalServErrorException;
import mx.com.tcs.permiso.model.entity.Permiso;
import mx.com.tcs.permiso.model.repository.PermisoRepository;
import mx.com.tcs.permiso.model.request.PermisoRequestDTO;
import mx.com.tcs.permiso.model.response.PermisoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static mx.com.tcs.permiso.utils.PermisoTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PermisoServiceImplUpdTest {

    @InjectMocks
    private PermisoServiceImpl service;
    @Mock
    private PermisoRepository repository;
    @Spy
    private ModelMapper mapper;
    @Mock
    private CircuitBreakerFactory circtBreakFactory;

    private CircuitBreaker circtBreakerTest;
    private Permiso permiso;
    private PermisoRequestDTO permisoUpdReqDTO;

    @BeforeEach
    void setUp() {
        Resilience4JCircuitBreakerFactory resCircuitBreakFactory = getCircuitBreakerFactory();
        circtBreakerTest = resCircuitBreakFactory.create("testCircuitBreaker");

        permiso = getPermiso();
        permisoUpdReqDTO = getPermisoUpdReqDTO();
    }

    @DisplayName("Test update method when result is OK")
    @Test
    void updateIsOk() {

        // Given
        int id = 1;
        Mockito.when(
                circtBreakFactory.create(Mockito.anyString())
        ).thenReturn(circtBreakerTest);
        Mockito.when(
                repository.findByIdAndActivo(Mockito.anyInt(), Mockito.anyInt())
        ).thenReturn(permiso);
        Mockito.when(
                repository.save(Mockito.any(Permiso.class))
        ).thenReturn(permiso);

        // When
        ResponseEntity<PermisoDTO> responseEntity = service.update(id,permisoUpdReqDTO);

        // Then
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode(),
                "Test HTTP Status from ResponseEntity fails.");
    }

    @DisplayName("Test update method when result is OK")
    @Test
    void updateWhenPropertiesAreNullAndResultIsOk() {

        // Given
        int id = 1;
        permisoUpdReqDTO = getPermUpdReqDTO();
        Mockito.when(
                circtBreakFactory.create(Mockito.anyString())
        ).thenReturn(circtBreakerTest);
        Mockito.when(
                repository.findByIdAndActivo(Mockito.anyInt(), Mockito.anyInt())
        ).thenReturn(permiso);
        Mockito.when(
                repository.save(Mockito.any(Permiso.class))
        ).thenReturn(permiso);

        // When
        ResponseEntity<PermisoDTO> responseEntity = service.update(id,permisoUpdReqDTO);

        // Then
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode(),
                "Test HTTP Status from ResponseEntity fails.");
    }

    @DisplayName("Test when update using circuit breaker throw an InternalServerErrorException happens")
    @Test
    void createWhenExceptionHappensInCircuitBreaker() {
        // Given
        int id = 1;
        Mockito.when(
                circtBreakFactory.create(Mockito.anyString())
        ).thenReturn(circtBreakerTest);
        Mockito.when(
                repository.findByIdAndActivo(Mockito.anyInt(), Mockito.anyInt())
        ).thenReturn(permiso);
        Mockito.when(
                repository.save(Mockito.any(Permiso.class))
        ).thenAnswer(
                thr -> {
                    throw new RuntimeException("SQL error in test update.");
                });

        // When and Then
        assertThrows(PermisoSrvInternalServErrorException.class,
                () ->  service.update(id,permisoUpdReqDTO)
        );
    }
}
