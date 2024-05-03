package mx.com.tcs.permiso.client;

import mx.com.tcs.permiso.exception.PermisoSrvInternalServErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserTypeFallbackTest {

    @InjectMocks
    private UserTypeFallback fallback;

    @BeforeEach
    void setUp() {
    }

    @DisplayName("Test getById in fallback implementation when call feign client.")
    @Test
    void getById() {
        // Given
        int userTypeParam = 2;

        // When and Then
        assertThrows(PermisoSrvInternalServErrorException.class,
                () ->  fallback.getById(userTypeParam));
    }
}