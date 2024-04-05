package mx.com.tcs.permiso.service;

import mx.com.tcs.permiso.exception.ItemNotFoundException;
import mx.com.tcs.permiso.model.Permiso;
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
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    private List<Permiso> permisoList;
    private PermisoDTO permisoDTO;

    @BeforeEach
    void setUp() {
        permisoList = new ArrayList<>();

        Permiso permiso = new Permiso();
        permiso.setId(1);
        permiso.setNombre("Academico");
        permiso.setDescripcion("Presentacion de examen profesional");
        permiso.setActivo(1);
        permiso.setIdPadre(1);
        permiso.setIcono("/images/icon_school.gif");

        permisoList.add(permiso);

        permisoDTO = new PermisoDTO();
        permisoDTO.setId(1);
        permisoDTO.setNombre("Academico");
        permisoDTO.setDescripcion("Presentacion de examen profesional");
        permisoDTO.setActivo(1);
        permisoDTO.setIdPadre(1);
        permisoDTO.setIcono("/images/icon_school.gif");
    }

    @DisplayName("Test listAll when result is OK")
    @Test
    void listAll() {
        // Given
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
        Mockito.when(repository.findAll()).thenReturn(permisoList);

        // When and Then
        assertThrows(ItemNotFoundException.class, () ->  service.listAll());
    }
}