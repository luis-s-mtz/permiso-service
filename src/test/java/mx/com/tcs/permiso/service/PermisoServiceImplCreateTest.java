package mx.com.tcs.permiso.service;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import mx.com.tcs.permiso.exception.ItemNotFoundException;
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
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PermisoServiceImplCreateTest {

    @InjectMocks
    private PermisoServiceImpl service;
    @Mock
    private PermisoRepository repository;
    @Spy
    private ModelMapper mapper;
    @Mock
    private CircuitBreakerFactory circtBreakFactory;

    private PermisoRequestDTO permisoReqDTO;
    private Permiso permiso;
    private CircuitBreaker circtBreakerTest;

    @BeforeEach
    void setUp() {
        permisoReqDTO = getPermisoRequestDTO();
        permiso = getPermiso();

        Resilience4JCircuitBreakerFactory resCircuitBreakFactory = getCircuitBreakerFactory();
        circtBreakerTest = resCircuitBreakFactory.create("testCircuitBreaker");
    }

    @DisplayName("Test create method when result is OK")
    @Test
    void createIsOk() {

        // Given
        Mockito.when(
                circtBreakFactory.create(Mockito.anyString())
        ).thenReturn(circtBreakerTest);
        Mockito.when(
                repository.save(Mockito.any(Permiso.class))
        ).thenReturn(permiso);

        // When
        ResponseEntity<PermisoDTO> responseEntity = service.create(permisoReqDTO);

        // Then
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode(),
                "Test HTTP Status from ResponseEntity fails.");
    }

    @DisplayName("Test when create using circuit breaker throw an InternalServerErrorException happens")
    @Test
    void createWhenExceptionHappensInCircuitBreaker() {
        // Given
        Mockito.when(
                circtBreakFactory.create(Mockito.anyString())
        ).thenReturn(circtBreakerTest);
        Mockito.when(
                repository.save(Mockito.any(Permiso.class))
        ).thenAnswer(
                thr -> {
                    throw new RuntimeException("SQL error in test create.");
                });

        // When and Then
        assertThrows(PermisoSrvInternalServErrorException.class,
                () ->  service.create(permisoReqDTO)
        );
    }

    private PermisoRequestDTO getPermisoRequestDTO() {
        PermisoRequestDTO permisoReqDTO = new PermisoRequestDTO();
        permisoReqDTO.setNombre("Academico");
        permisoReqDTO.setDescripcion("Presentacion de examen profesional");
        permisoReqDTO.setActivo(1);
        permisoReqDTO.setIdPadre(1);
        permisoReqDTO.setIcono("/images/icon_school.gif");
        return permisoReqDTO;
    }

    private Permiso getPermiso() {
        Permiso permiso = new Permiso();
        permiso.setId(1);
        permiso.setNombre("Academico");
        permiso.setDescripcion("Presentacion de examen profesional");
        permiso.setActivo(1);
        permiso.setIdPadre(1);
        permiso.setIcono("/images/icon_school.gif");
        return permiso;
    }

    private Resilience4JCircuitBreakerFactory getCircuitBreakerFactory() {
        // Create a CircuitBreakerRegistry with a custom global configuration
        CircuitBreakerRegistry circuitBreakerRegistry =
                CircuitBreakerRegistry.of(getCircuitBreakerConfig());

        // Create a TimeLimiterRegistry with a custom configuration
        TimeLimiterRegistry timeLimiterRegistry = TimeLimiterRegistry.of(getTimeLimiterConfig());

        return new Resilience4JCircuitBreakerFactory(circuitBreakerRegistry,timeLimiterRegistry,null);
    }

    private CircuitBreakerConfig getCircuitBreakerConfig() {
        // Create a custom configuration for a CircuitBreaker
        return CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofMillis(1000))
                .permittedNumberOfCallsInHalfOpenState(2)
                .recordExceptions(IOException.class, TimeoutException.class)
                .slidingWindowSize(2)
                .build();
    }

    private TimeLimiterConfig getTimeLimiterConfig() {
        // Create a custom configuration for a TimeLimiter
        return TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(4))
                .build();
    }
}
