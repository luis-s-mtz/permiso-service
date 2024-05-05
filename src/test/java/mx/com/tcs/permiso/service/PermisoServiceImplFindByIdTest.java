package mx.com.tcs.permiso.service;

import mx.com.tcs.permiso.exception.PermisoSrvInternalServErrorException;
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
import org.modelmapper.ModelMapper;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static mx.com.tcs.permiso.utils.PermisoTestUtils.getCircuitBreakerFactory;
import static mx.com.tcs.permiso.utils.PermisoTestUtils.getPermiso;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PermisoServiceImplFindByIdTest {
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

    @BeforeEach
    void setUp() {
        Resilience4JCircuitBreakerFactory resCircuitBreakFactory = getCircuitBreakerFactory();
        circtBreakerTest = resCircuitBreakFactory.create("testCircuitBreaker");

        permiso = getPermiso();
    }

    @DisplayName("Test getById method when result is OK")
    @Test
    void getByIdIsOk() {

        int id = 1;
        // Given
        Mockito.when(
                circtBreakFactory.create(Mockito.anyString())
        ).thenReturn(circtBreakerTest);
        Mockito.when(
                repository.findByIdAndActivo(Mockito.anyInt(), Mockito.anyInt())
        ).thenReturn(permiso);

        // When
        ResponseEntity<PermisoDTO> responseEntity = service.getById(id);

        // Then
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode(),
                "Test HTTP Status from ResponseEntity fails.");
    }

    @DisplayName(" Test getById method circuit breaker throw exception.")
    @Test
    void getByIdWhenCircBreakThrowsExep() {

        int id = 1;
        // Given
        Mockito.when(
                circtBreakFactory.create(Mockito.anyString())
        ).thenReturn(circtBreakerTest);
        Mockito.when(
                repository.findByIdAndActivo(Mockito.anyInt(), Mockito.anyInt())
        ).thenAnswer(
                thr -> {
                    throw new RuntimeException("SQL error in test create.");
                });

        // When Then
        assertThrows(PermisoSrvInternalServErrorException.class,
                () ->  service.getById(id)
        );
    }
}
