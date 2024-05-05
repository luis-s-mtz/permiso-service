package mx.com.tcs.permiso.controller;

import mx.com.tcs.permiso.model.request.PermisoRequestDTO;
import mx.com.tcs.permiso.model.response.PermisoDTO;
import mx.com.tcs.permiso.model.response.PermisoTipoUsuarioDTO;
import mx.com.tcs.permiso.service.IPermisoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
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
    private ResponseEntity<PermisoTipoUsuarioDTO> respFindByParams;
    private PermisoRequestDTO permisoReqDTO;
    private ResponseEntity<PermisoDTO> responseCreate;
    private ResponseEntity<PermisoDTO> responseById;
    private PermisoRequestDTO permisoUpdtReqDTO;

    @BeforeEach
    void setUp() {
        List<PermisoDTO> permisoDTOList = new ArrayList<>();

        PermisoDTO permisoDTO = getPermisoDTOAcademico();
        permisoDTOList.add(permisoDTO);

        response = ResponseEntity.ok(permisoDTOList);
        respFindByParams = getRespFindByParams();

        permisoReqDTO = getPermisoRequestDTO();
        responseCreate = getResponseCreate();

        responseById = getResponseById();

        permisoUpdtReqDTO = getPermisoRequestDTO();
    }

    @DisplayName("Test listAll when result is OK")
    @Test
    void getAll() {
        // Given
        Mockito.when(service.listAll()).thenReturn(response);

        // When
        ResponseEntity<List<PermisoDTO>> responseEntity = controller.getAll();

        // Then
        assertEquals(response.getStatusCode(), responseEntity.getStatusCode(),
                "Test HTTP Status from ResponseEntity fails.");
    }

    @DisplayName("Test findByParams when result is OK")
    @Test
    void testfindByParams() {
        // Given
        int paramUserType = 2;
        Mockito.when(service.findByParams(Mockito.anyInt())).thenReturn(respFindByParams);

        // When
        ResponseEntity<PermisoTipoUsuarioDTO> responseEntity = controller.findByParams(paramUserType);

        // Then
        assertEquals(respFindByParams.getStatusCode(), responseEntity.getStatusCode(),
                "Test HTTP Status from ResponseEntity fails.");
    }

    @DisplayName("Test create method when result is OK")
    @Test
    void createIsOk() {
        // Given
        Mockito.when(
                service.create(Mockito.any(PermisoRequestDTO.class))
        ).thenReturn(responseCreate);

        // When
        ResponseEntity<PermisoDTO> responseEntity = controller.create(permisoReqDTO);

        // Then
        assertEquals(responseCreate.getStatusCode(), responseEntity.getStatusCode(),
                "Test HTTP Status from ResponseEntity fails.");
    }

    @DisplayName("Test findById when result is OK")
    @Test
    void testfindById() {
        // Given
        int id = 3;
        Mockito.when(service.getById(Mockito.anyInt())).thenReturn(responseById);

        // When
        ResponseEntity<PermisoDTO> response = controller.getById(id);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode(),
                "Test HTTP Status from ResponseEntity fails.");
    }

    @DisplayName("Test update when result is OK")
    @Test
    void update() {
        // Given
        int id = 3;
        permisoUpdtReqDTO.setActivo(0);
        Mockito.when(
                service.update(Mockito.anyInt(),Mockito.any(PermisoRequestDTO.class))
        ).thenReturn(responseById);

        // When
        ResponseEntity<PermisoDTO> response = controller.update(id,permisoUpdtReqDTO);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode(),
                "Test HTTP Status from ResponseEntity fails.");
    }

    private PermisoDTO getPermisoDTOAcademico() {
        PermisoDTO permisoDTO = new PermisoDTO();
        permisoDTO.setId(1);
        permisoDTO.setNombre("Academico");
        permisoDTO.setDescripcion("Presentacion de examen profesional");
        permisoDTO.setActivo(1);
        permisoDTO.setIdPadre(1);
        permisoDTO.setIcono("/images/icon_school.gif");
        return permisoDTO;
    }

    private ResponseEntity<PermisoTipoUsuarioDTO> getRespFindByParams() {
        PermisoTipoUsuarioDTO permTipoUsu = new PermisoTipoUsuarioDTO();

        List<PermisoDTO> permisoDTOList = new ArrayList<>();

        PermisoDTO permisoDTO = getPermisoDTOAcademicoExam();
        permisoDTOList.add(permisoDTO);
        permisoDTO = getPermisoDTOEnfermedad();
        permisoDTOList.add(permisoDTO);

        permTipoUsu.setTipoUsuario("Permanente");
        permTipoUsu.setPermisos(permisoDTOList);

        return ResponseEntity.ok(permTipoUsu);
    }

    private  PermisoDTO getPermisoDTOAcademicoExam() {
        PermisoDTO permisoDTO = new PermisoDTO();
        permisoDTO.setId(3);
        permisoDTO.setNombre("Permiso academ por examen");
        permisoDTO.setDescripcion("Presentacion de examenes");
        permisoDTO.setActivo(1);
        permisoDTO.setIdPadre(1);
        permisoDTO.setIcono("/images/icon_school_quiz.gif");
        return permisoDTO;
    }

    private PermisoDTO getPermisoDTOEnfermedad() {
        PermisoDTO permisoDTO = new PermisoDTO();
        permisoDTO.setId(4);
        permisoDTO.setNombre("Permiso por enfermedad");
        permisoDTO.setDescripcion("Consulta en Clinica");
        permisoDTO.setActivo(1);
        permisoDTO.setIdPadre(2);
        permisoDTO.setIcono("/images/icon_check_health.gif");
        return permisoDTO;
    }

    private ResponseEntity<PermisoDTO> getResponseCreate() {
        PermisoDTO permisoDTO = getPermisoDTOAcademico();
        return ResponseEntity.ok(permisoDTO);
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

    private ResponseEntity<PermisoDTO> getResponseById() {
        return ResponseEntity.ok(getPermisoDTOAcademicoExam());
    }
}