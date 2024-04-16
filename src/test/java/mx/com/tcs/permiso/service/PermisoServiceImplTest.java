package mx.com.tcs.permiso.service;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import mx.com.tcs.permiso.exception.ItemNotFoundException;
import mx.com.tcs.permiso.model.entity.Permiso;
import mx.com.tcs.permiso.model.repository.PermisoRepository;
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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PermisoServiceImplTest {

    private static final Integer SUCCESSFULL_HTTP_CODE = 200;

    @InjectMocks
    private PermisoServiceImpl service;

    @Mock
    private PermisoRepository repository;

    @Spy
    private ModelMapper mapper;

    @Mock
    private CircuitBreakerFactory circtBreakFactory;

    private List<Permiso> permisoList;
    private PermisoDTO permisoDTO;
    private CircuitBreaker circtBreakerTest;

    @BeforeEach
    void setUp() {
        permisoList = getPermisoList();
        permisoDTO = getPermisoDTO();

        Resilience4JCircuitBreakerFactory resCircuitBreakFactory = getCircuitBreakerFactory();
        circtBreakerTest = resCircuitBreakFactory.create("testCircuitBreaker");
    }

    @DisplayName("Test listAll when result is OK")
    @Test
    void listAll() {
        // Given
        Mockito.when(circtBreakFactory.create(Mockito.anyString())).thenReturn(circtBreakerTest);
        Mockito.when(repository.findAll()).thenReturn(permisoList);
        Mockito.doNothing().when(mapper).map(Mockito.any(Permiso.class),Mockito.any(PermisoDTO.class));

        // When
        ResponseEntity<List<PermisoDTO>> permisoDtoList = service.listAll();

        //Then
        assertEquals(permisoDtoList.getStatusCode(), HttpStatusCode.valueOf(SUCCESSFULL_HTTP_CODE),
                "Test StatusCode from ResponseEntity fails.");
        assertEquals(Objects.requireNonNull(permisoDtoList.getBody()).get(0).getId(),permisoDTO.getId(),
                "Test id of first element from ResponseEntity fails.");
    }

    @DisplayName("Test listAll when the empty list throws a ItemNotFoundException happens")
    @Test
    void listAllThrowsItemNotFoundException() {
        // Given
        permisoList.clear();
        Mockito.when(circtBreakFactory.create(Mockito.anyString())).thenReturn(circtBreakerTest);
        Mockito.when(repository.findAll()).thenReturn(permisoList);

        // When and Then
        assertThrows(ItemNotFoundException.class, () ->  service.listAll());
    }

    @DisplayName("Test when throw an InternalServerErrorException happens")
    @Test
    void listAllWhenExceptionHappensInCircuitBreaker() {
        // Given
        Mockito.when(circtBreakFactory.create(Mockito.anyString())).thenReturn(circtBreakerTest);
        Mockito.when(repository.findAll()).thenAnswer(thr -> {throw new RuntimeException("SQL test error.");});

        // When
        ResponseEntity<List<PermisoDTO>> permisoDtoList = service.listAll();

        // Then
        assertEquals(permisoDtoList.getStatusCode(), HttpStatusCode.valueOf(SUCCESSFULL_HTTP_CODE),
                "Test StatusCode from ResponseEntity fails when Exception happens in CircuitBreaker.");
        assertEquals(Objects.requireNonNull(permisoDtoList.getBody()).get(0).getId(),permisoDTO.getId(),
                "Test id of first element from ResponseEntity fails when Exception happens in CircuitBreaker.");
    }

    private List<Permiso> getPermisoList() {
        List<Permiso> entityList = new ArrayList<>();
        Permiso permiso = getPermiso();
        entityList.add(permiso);
        return entityList;
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

    private PermisoDTO getPermisoDTO() {
        PermisoDTO dto =  new PermisoDTO();
        dto.setId(1);
        dto.setNombre("Academico");
        dto.setDescripcion("Presentacion de examen profesional");
        dto.setActivo(1);
        dto.setIdPadre(1);
        dto.setIcono("/images/icon_school.gif");

        return dto;
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