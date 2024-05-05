package mx.com.tcs.permiso.service;

import mx.com.tcs.permiso.client.UserTypeFeignClient;
import mx.com.tcs.permiso.exception.PermisoSrvInternalServErrorException;
import mx.com.tcs.permiso.model.client.UserTypeDTO;
import mx.com.tcs.permiso.model.entity.Permiso;
import mx.com.tcs.permiso.model.entity.TipoUsuarioPermiso;
import mx.com.tcs.permiso.model.repository.PermisoRepository;
import mx.com.tcs.permiso.model.repository.TipoUsuarioPermisoRepository;
import mx.com.tcs.permiso.model.response.PermisoTipoUsuarioDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PermisoServiceImplFindByParmsTest {

    @InjectMocks
    private PermisoServiceImpl service;
    @Mock
    private UserTypeFeignClient userTypeFeignClient;
    @Mock
    private TipoUsuarioPermisoRepository tipUsuPermRepository;
    @Mock
    private PermisoRepository repository;
    @Spy
    private ModelMapper mapper;

    private ResponseEntity<UserTypeDTO> respUserTypeDTO;
    private List<TipoUsuarioPermiso> usrTypePermisoList;
    private List<Permiso> permisosList;

    @BeforeEach
    void setUp() {
        respUserTypeDTO = getRespEntityUserTypeDTO();
        usrTypePermisoList = getUserTypePermisoList();
        permisosList = getPermisosList();
    }

    @DisplayName("Test findByParams when result is OK")
    @Test
    void testFindByParamsOk() {
        // Given
        int userTypeParam = 2;
        Mockito.when(userTypeFeignClient.getById(Mockito.anyInt()))
                .thenReturn(respUserTypeDTO);
        Mockito.when(
                tipUsuPermRepository.findByIdTipoUsuarioAndActivo(
                        Mockito.anyInt(),Mockito.anyInt())
                ).thenReturn(usrTypePermisoList);
        Mockito.when(repository.findByIdInAndActivo(Mockito.anyList(),Mockito.anyInt()))
                .thenReturn(permisosList);

        // When
        ResponseEntity<PermisoTipoUsuarioDTO> respPermTipoUsuario = service.findByParams(userTypeParam);

        //Then
        assertEquals(HttpStatus.OK,respPermTipoUsuario.getStatusCode(),
                "Test StatusCode from ResponseEntity fails.");
        assertEquals(permisosList.get(0).getId(),
                Objects.requireNonNull(
                        respPermTipoUsuario.getBody()
                ).getPermisos().get(0).getId(),
                "Test id of first element from ResponseEntity permisos list fails.");
    }

    @DisplayName("Test findByParams when result is OK and UserType service response body is null.")
    @Test
    void testFindByParamsOkUsrTypeBodyRespIsNull() {
        // Given
        int userTypeParam = 2;
        respUserTypeDTO = getRespEntityUsrTypDTOBodyNull();
        Mockito.when(userTypeFeignClient.getById(Mockito.anyInt()))
                .thenReturn(respUserTypeDTO);

        // When and Then
        assertThrows(PermisoSrvInternalServErrorException.class,
                () ->  service.findByParams(userTypeParam));
    }

    @DisplayName("Test findByParams when result is OK and UserType service response body in description is null.")
    @Test
    void testFindByParamsOkUsrTypeDTOInBodyDescIsNull() {
        // Given
        int userTypeParam = 2;
        respUserTypeDTO = getRespEntityUsrTypDTOPropDescIsNull();
        Mockito.when(userTypeFeignClient.getById(Mockito.anyInt()))
                .thenReturn(respUserTypeDTO);
        Mockito.when(
                tipUsuPermRepository.findByIdTipoUsuarioAndActivo(
                        Mockito.anyInt(),Mockito.anyInt())
        ).thenReturn(usrTypePermisoList);
        Mockito.when(repository.findByIdInAndActivo(Mockito.anyList(),Mockito.anyInt()))
                .thenReturn(permisosList);

        // When
        ResponseEntity<PermisoTipoUsuarioDTO> respPermTipoUsuario = service.findByParams(userTypeParam);

        //Then
        assertEquals(HttpStatus.OK,respPermTipoUsuario.getStatusCode(),
                "Test StatusCode from ResponseEntity fails.");
        assertEquals(permisosList.get(0).getId(),
                Objects.requireNonNull(
                        respPermTipoUsuario.getBody()
                ).getPermisos().get(0).getId(),
                "Test id of first element from ResponseEntity permisos list fails.");
    }

    @DisplayName("Test findByParams when userType DTO is null and throws an exception.")
    @Test
    void testFindByParamsThrowsInternalServException() {
        // Given
        int userTypeParam = 2;
        respUserTypeDTO = ResponseEntity.internalServerError().build();
        Mockito.when(userTypeFeignClient.getById(Mockito.anyInt()))
                .thenReturn(respUserTypeDTO);


        // When and Then
        assertThrows(PermisoSrvInternalServErrorException.class,
                () ->  service.findByParams(userTypeParam));
    }

    @DisplayName("Test findByParams when throw an exception in repository ")
    @Test
    void testFindByParamsThrowsInternalServExceptionInRepository() {
        // Given
        int userTypeParam = 2;
        Mockito.when(userTypeFeignClient.getById(Mockito.anyInt()))
                .thenReturn(respUserTypeDTO);
        Mockito.when(
                tipUsuPermRepository.findByIdTipoUsuarioAndActivo(
                        Mockito.anyInt(),Mockito.anyInt())
        ).thenReturn(usrTypePermisoList);
        Mockito.when(
                repository.findByIdInAndActivo(
                        Mockito.anyList(),Mockito.anyInt()
                )).thenAnswer(
                        thr -> {
                            throw new RuntimeException("SQL test error.");
                        });

        // When and Then
        assertThrows(PermisoSrvInternalServErrorException.class,
                () ->  service.findByParams(userTypeParam));
    }

    @DisplayName("Test findByParams when feign client response is 404 Not Found.")
    @Test
    void testFindByParamsWhenFeignClientIsNotFound() {
        // Given
        int userTypeParam = 2;
        respUserTypeDTO = ResponseEntity.notFound().build();
        Mockito.when(userTypeFeignClient.getById(Mockito.anyInt()))
                .thenReturn(respUserTypeDTO);

        // When and Then
        assertThrows(PermisoSrvInternalServErrorException.class,
                () ->  service.findByParams(userTypeParam));
    }

    private ResponseEntity<UserTypeDTO> getRespEntityUserTypeDTO() {
        UserTypeDTO usrTypeDTO = new UserTypeDTO();
        usrTypeDTO.setId(2);
        usrTypeDTO.setDescription("Permanente");
        usrTypeDTO.setStatus(1);
        return ResponseEntity.ok(usrTypeDTO);
    }

    private ResponseEntity<UserTypeDTO> getRespEntityUsrTypDTOBodyNull() {
        return ResponseEntity.ok(null);
    }

    private ResponseEntity<UserTypeDTO> getRespEntityUsrTypDTOPropDescIsNull() {
        UserTypeDTO usrTypeDTO = new UserTypeDTO();
        return ResponseEntity.ok(usrTypeDTO);
    }

    private List<TipoUsuarioPermiso> getUserTypePermisoList() {
        List<TipoUsuarioPermiso> userTypePermList = new ArrayList<>();

        TipoUsuarioPermiso tipoUsuarioPerm = new TipoUsuarioPermiso();
        tipoUsuarioPerm.setId(1);
        tipoUsuarioPerm.setIdPermiso(3);
        tipoUsuarioPerm.setIdTipoUsuario(2);
        tipoUsuarioPerm.setActivo(1);

        userTypePermList.add(tipoUsuarioPerm);
        return userTypePermList;
    }

    private List<Permiso> getPermisosList() {
        List<Permiso> permisos = new ArrayList<>();

        Permiso permiso = new Permiso();
        permiso.setId(3);
        permiso.setNombre("Permiso academ por examen");
        permiso.setDescripcion("Presentacion de examenes");
        permiso.setActivo(1);
        permiso.setIdPadre(1);
        permiso.setIcono("/images/icon_school_quiz.gif");

        permisos.add(permiso);
        return permisos;
    }
}
