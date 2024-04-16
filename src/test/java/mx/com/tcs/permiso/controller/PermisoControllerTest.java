package mx.com.tcs.permiso.controller;

import mx.com.tcs.permiso.model.response.PermisoDTO;
import mx.com.tcs.permiso.service.IPermisoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PermisoControllerTest {

    @InjectMocks
    private PermisoController controller;

    @Mock
    private IPermisoService service;

    private ResponseEntity<List<PermisoDTO>> response;

    @BeforeEach
    void setUp() {
        List<PermisoDTO> permisoDTOList = new ArrayList<>();

        PermisoDTO permisoDTO = new PermisoDTO();
        permisoDTO.setId(1);
        permisoDTO.setNombre("Academico");
        permisoDTO.setDescripcion("Presentacion de examen profesional");
        permisoDTO.setActivo(1);
        permisoDTO.setIdPadre(1);
        permisoDTO.setIcono("/images/icon_school.gif");

        permisoDTOList.add(permisoDTO);

        response = ResponseEntity.ok(permisoDTOList);
    }

    @DisplayName("Test listAll when result is OK")
    @Test
    void getAll() {
        // Given
        Mockito.when(service.listAll()).thenReturn(response);

        // When
        ResponseEntity responseEntity = controller.getAll();

        // Then
        assertEquals(response.getStatusCode(), responseEntity.getStatusCode(),
                "Test HTTP Status from ResponseEntity fails.");
    }
}