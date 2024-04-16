package mx.com.tcs.permiso.service;

import lombok.extern.slf4j.Slf4j;
import mx.com.tcs.permiso.client.UserTypeFeignClient;
import mx.com.tcs.permiso.exception.ItemNotFoundException;
import mx.com.tcs.permiso.exception.PermisoSrvInternalServErrorException;
import mx.com.tcs.permiso.model.entity.Permiso;
import mx.com.tcs.permiso.model.client.UserTypeDTO;
import mx.com.tcs.permiso.model.entity.TipoUsuarioPermiso;
import mx.com.tcs.permiso.model.repository.PermisoRepository;
import mx.com.tcs.permiso.model.repository.TipoUsuarioPermisoRepository;
import mx.com.tcs.permiso.model.response.PermisoDTO;
import mx.com.tcs.permiso.model.response.PermisoTipoUsuarioDTO;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luis
 * @since 1.0
 *
 * Class to implement methods used in the service layer of API catpermiso-service.
 */
@Slf4j
@Service
public class PermisoServiceImpl implements IPermisoService {

    private static final Integer ACTIVE_ROWS = 1;
    /**
     * Bean of the repository.
     */
    private final PermisoRepository repository;

    /**
     *  Bean of the modelMapper.
     */
    private final ModelMapper modelMapper;

    /**
     * Bean of the circuitBreakerFactory.
     */
    private final CircuitBreakerFactory circtBreakFactory;

    /**
     * Bean of the TipoUsuarioPermisoRepository.
     */
    private final TipoUsuarioPermisoRepository tipUsuPermRepository;

    /**
     * Bean of the UserTypeFeignClient.
     */
    private final UserTypeFeignClient userTypeFeignClient;

    public PermisoServiceImpl(PermisoRepository repository, ModelMapper modelMapper,
                              CircuitBreakerFactory circtBreakFactory,
                              UserTypeFeignClient userTypeFeignClient,
                              TipoUsuarioPermisoRepository tipUsuPermRepository) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.circtBreakFactory = circtBreakFactory;
        this.userTypeFeignClient = userTypeFeignClient;
        this.tipUsuPermRepository = tipUsuPermRepository;
    }

    /**
     * Implements the method to get all records of Permiso entity.
     * @return a response entity of the list of the Permiso object.
     */
    @Override
    public ResponseEntity<List<PermisoDTO>> listAll() {
        CircuitBreaker circtBreakListAll = circtBreakFactory.create("circtBreakListAll");

        List<PermisoDTO> permisoDTOList = circtBreakListAll.run(
                this::getAllPermiso,throwable -> getDefaultAllPermisoList());

        if (permisoDTOList.isEmpty()) {
            throw new ItemNotFoundException("The query to permiso table return empty list.");
        }
        return ResponseEntity.ok(permisoDTOList);
    }

    /**
     * Method to return all records of Permiso object find by Id TipoUsuario.
     *
     * @param idTipoUsuario The identifier of TipoUsuario entity object.
     * @return A PermisoTipoUsuarioDTO object.
     */
    @Override
    public ResponseEntity<PermisoTipoUsuarioDTO> findByParams(Integer idTipoUsuario) {

        PermisoTipoUsuarioDTO permisoTipoUsuarioDTO = getTipoUsuarioDTO(idTipoUsuario);

        if (permisoTipoUsuarioDTO == null){
            log.error("Error in [{}] findByParams(): {}",
                    this.getClass().getName(),"Error al consultar informacion de feign client.");
            throw new PermisoSrvInternalServErrorException("Error al consultar informacion de feign client.");
        } else {
            List<TipoUsuarioPermiso> relationList = findPermisoByIdTipoUsuario(idTipoUsuario);

            List<Integer> ids = new ArrayList<>();
            relationList.stream().
                    map(tipUsrPerm -> ids.add(tipUsrPerm.getIdPermiso())).
                    toList();

            permisoTipoUsuarioDTO.setPermisos(getPermisosByIds(ids));
        }
        return ResponseEntity.ok(permisoTipoUsuarioDTO);
    }

    /**
     * Method to build a {@code PermisoTipoUsuarioDTO} object when call {@code UserTypeFeignClient} and use of the
     * response the {@code description} field into the {@code tipoUsuario} attribute.
     * @param idTipoUsuario Is the identifier of TipoUsuario entity and is the parametor to search the Permiso entites list.
     * @return A PermisoTipoUsuario object with the tipoUsuario attribute filled by description field of response of UserTypeFeignClient.
     */
    private PermisoTipoUsuarioDTO getTipoUsuarioDTO(Integer idTipoUsuario) {

        PermisoTipoUsuarioDTO permisoTipoUsuarioDTO = null;
        ResponseEntity<UserTypeDTO> respUserTypeDTO = userTypeFeignClient.getById(idTipoUsuario);

        if(HttpStatus.OK.equals(respUserTypeDTO.getStatusCode())) {
            permisoTipoUsuarioDTO = new PermisoTipoUsuarioDTO();
            permisoTipoUsuarioDTO.setTipoUsuario(respUserTypeDTO.getBody().getDescription());
        }
        return permisoTipoUsuarioDTO;
    }

    /**
     * Method used to get the Identifiers of Permiso related with Identifier of TipoUsuario.
     * @param idTipoUsuario The Identifier of TipoUsuario.
     * @return List of TipoUsuarioPermiso entity.
     */
    private List<TipoUsuarioPermiso> findPermisoByIdTipoUsuario(Integer idTipoUsuario) {
        return tipUsuPermRepository.findByIdTipoUsuarioAndActivo(idTipoUsuario,ACTIVE_ROWS);
    }

    /**
     * Method to query from Permiso entity.
     * @return a list of Permiso object.
     */
    private List<PermisoDTO> getAllPermiso() {

        try {
            List<Permiso> permisoList = (List<Permiso>) repository.findAll();

            return permisoList.
                    stream().
                    map(permiso -> modelMapper.map(permiso, PermisoDTO.class)).
                    toList();
        } catch(Exception ex) {
            log.error("Error in [{}] in getAllPermiso: {}",this.getClass().getName(),ex.getMessage());
            throw new PermisoSrvInternalServErrorException(ex.getMessage());
        }
    }

    /**
     * Method to query from Permiso entity filter by list of TipoUsuario identifiers.
     * @return a list of Permiso object.
     */
    private List<PermisoDTO> getPermisosByIds(List<Integer> ids) {

        try {
            List<Permiso> permisoList = (List<Permiso>) repository.findByIdInAndActivo(ids, ACTIVE_ROWS);

            return permisoList.
                    stream().
                    map(permiso -> modelMapper.map(permiso, PermisoDTO.class)).
                    toList();
        } catch(Exception ex) {
            log.error("Error in [{}] in getPermisoByIds: {}",this.getClass().getName(),ex.getMessage());
            throw new PermisoSrvInternalServErrorException(ex.getMessage());
        }
    }

    /**
     * Fallback response used when throw an exception and create a default DTO object from Permiso entity.
     * @return List of DTO permiso object.
     */
    private List<PermisoDTO> getDefaultAllPermisoList() {
        List<PermisoDTO> permisoDTOList = new ArrayList<>();
        permisoDTOList.add(getDefaultPermisoDTO());
        return permisoDTOList;
    }

    /**
     * Create a default DTO object of Permiso entity.
     * @return DTO object from Permiso entity.
     */
    private PermisoDTO getDefaultPermisoDTO() {
        PermisoDTO permisoDTO = new PermisoDTO();
        permisoDTO.setId(1);
        permisoDTO.setNombre("Default Permiso");
        permisoDTO.setDescripcion("Esta es una descripcion para la respuesta por default Permiso.");
        permisoDTO.setIdPadre(1);
        permisoDTO.setActivo(1);
        permisoDTO.setIcono("/images/icon_default_perm.gif");
        return permisoDTO;
    }
}
