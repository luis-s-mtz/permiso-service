package mx.com.tcs.permiso.utils;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import mx.com.tcs.permiso.model.entity.Permiso;
import mx.com.tcs.permiso.model.request.PermisoRequestDTO;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

public class PermisoTestUtils {

    public static Resilience4JCircuitBreakerFactory getCircuitBreakerFactory() {
        // Create a CircuitBreakerRegistry with a custom global configuration
        CircuitBreakerRegistry circuitBreakerRegistry =
                CircuitBreakerRegistry.of(getCircuitBreakerConfig());

        // Create a TimeLimiterRegistry with a custom configuration
        TimeLimiterRegistry timeLimiterRegistry = TimeLimiterRegistry.of(getTimeLimiterConfig());

        return new Resilience4JCircuitBreakerFactory(circuitBreakerRegistry,timeLimiterRegistry,null);
    }

    private static CircuitBreakerConfig getCircuitBreakerConfig() {
        // Create a custom configuration for a CircuitBreaker
        return CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofMillis(1000))
                .permittedNumberOfCallsInHalfOpenState(2)
                .recordExceptions(IOException.class, TimeoutException.class)
                .slidingWindowSize(2)
                .build();
    }

    private static TimeLimiterConfig getTimeLimiterConfig() {
        // Create a custom configuration for a TimeLimiter
        return TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(4))
                .build();
    }

    public static Permiso getPermiso() {
        Permiso permiso = new Permiso();
        permiso.setId(1);
        permiso.setNombre("Academico");
        permiso.setDescripcion("Presentacion de examen profesional");
        permiso.setActivo(1);
        permiso.setIdPadre(1);
        permiso.setIcono("/images/icon_school.gif");
        return permiso;
    }

    public static PermisoRequestDTO getPermisoUpdReqDTO() {
        PermisoRequestDTO permisoReqDTO = new PermisoRequestDTO();
        permisoReqDTO.setNombre("Enfermedad");
        permisoReqDTO.setDescripcion("Vacunacion covid");
        permisoReqDTO.setActivo(0);
        permisoReqDTO.setIdPadre(2);
        permisoReqDTO.setIcono("/images/icon_health.gif");
        return permisoReqDTO;
    }

    public static PermisoRequestDTO getPermUpdReqDTO() {
        PermisoRequestDTO permisoReqDTO = new PermisoRequestDTO();
        permisoReqDTO.setNombre(null);
        permisoReqDTO.setDescripcion(null);
        permisoReqDTO.setActivo(null);
        permisoReqDTO.setIdPadre(null);
        permisoReqDTO.setIcono(null);
        return permisoReqDTO;
    }

    public static Permiso getPermisoUpdated() {
        Permiso permiso = new Permiso();
        permiso.setId(1);
        permiso.setNombre("Academico");
        permiso.setDescripcion("Presentacion de examen profesional");
        permiso.setActivo(0);
        permiso.setIdPadre(1);
        permiso.setIcono("/images/icon_school.gif");
        return permiso;
    }
}
