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
import mx.com.tcs.permiso.model.request.PermisoRequestDTO;
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

        if(HttpStatus.OK.equals(respUserTypeDTO.getStatusCode()) && respUserTypeDTO.hasBody()) {
            permisoTipoUsuarioDTO = new PermisoTipoUsuarioDTO();

            UserTypeDTO usrTypeDTO = respUserTypeDTO.getBody();

            if (StringUtils.isNotEmpty(usrTypeDTO.getDescription())) {
                permisoTipoUsuarioDTO.setTipoUsuario(usrTypeDTO.getDescription());
            }
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
            List<Permiso> permisoList = repository.findByIdInAndActivo(ids, ACTIVE_ROWS);

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
     * Method to add a record in the Permiso catalog.
     *
     * @param permisoReqDTO The DTO object with information to add record in the Permiso catalogue.
     * @return The Permiso object added to the database.
     */
    @Override
    public ResponseEntity<PermisoDTO> create(PermisoRequestDTO permisoReqDTO) {
        CircuitBreaker circtBreakCreate = circtBreakFactory.create("circtBreakCreate");

        Permiso permiso = circtBreakCreate.run(
                () -> getSave(permisoReqDTO), throwable -> {
                    throw new PermisoSrvInternalServErrorException("Creation in permiso table fails.");}
        );

        return ResponseEntity.ok(modelMapper.map(permiso,PermisoDTO.class));
    }

    /**
     * Method to get a record by Id in the Permiso catalog.
     *
     * @param id The identifier to find a record in Permiso catalog.
     * @return The Permiso object find by Id.
     */
    @Override
    public ResponseEntity<PermisoDTO> getById(Integer id) {
        CircuitBreaker circtBreakCreate = circtBreakFactory.create("circtBreakGetById");

        return ResponseEntity.ok(
                circtBreakCreate.run(
                        () -> getPermisoById(id), throwable -> {
                            throw new PermisoSrvInternalServErrorException(
                                    "Query by Id in permiso table fails.");
                        }
                )
        );
    }

    /**
     * Method to execute a partial update of the record in the Permiso catalog find by Id.
     *
     * @param id            The identifier to find a record to update in Permiso catalog.
     * @param permisoReqDTO The DTO object with partial information to update the record in the Permiso catalogue.
     * @return The Permiso object added to the database.
     */
    @Override
    public ResponseEntity<PermisoDTO> update(
            Integer id, PermisoRequestDTO permisoReqDTO) {
        CircuitBreaker circtBreakCreate = circtBreakFactory.create("circtBreakUpdate");

        return ResponseEntity.ok(
                circtBreakCreate.run(
                        () -> partialUpdate(id, permisoReqDTO),
                        throwable -> {
                            throw new PermisoSrvInternalServErrorException(
                                    "Update in permiso table fails.");
                        }
                )
        );
    }

    private PermisoDTO partialUpdate(
            Integer id, PermisoRequestDTO permisoUpdReqDTO){
        Permiso permiso = findById(id);

        setUpdateValues(permiso, permisoUpdReqDTO);
        save(permiso);
        return modelMapper.map(permiso,PermisoDTO.class);
    }

    /**
     * Add values come in PATCH request to use in the permiso entity to update.
     * @param permiso Permiso object find by Id to add properties to update.
     * @param permisoUpdReqDTO The request object to get values to add into entity to update.
     * @return Permiso object updated.
     */
    private Permiso setUpdateValues(
            Permiso permiso, PermisoRequestDTO permisoUpdReqDTO){

        if(permisoUpdReqDTO.getNombre() != null) {
            permiso.setNombre(permisoUpdReqDTO.getNombre());
        }

        if(permisoUpdReqDTO.getDescripcion() != null) {
            permiso.setDescripcion(permisoUpdReqDTO.getDescripcion());
        }

        if(permisoUpdReqDTO.getIdPadre() != null) {
            permiso.setIdPadre(permisoUpdReqDTO.getIdPadre());
        }

        if(permisoUpdReqDTO.getActivo() != null) {
            permiso.setActivo(permisoUpdReqDTO.getActivo());
        }

        if(permisoUpdReqDTO.getIcono() != null) {
            permiso.setIcono(permisoUpdReqDTO.getIcono());
        }

        return permiso;
    }

    /**
     * Method to query from Permiso entity by id.
     * @return A DTO of Permiso object.
     */
    private PermisoDTO getPermisoById(Integer id) {

        try {
            return modelMapper.map(findById(id), PermisoDTO.class);
        } catch(Exception ex) {
            log.error("Error in getPermisoById: {}", ex.getMessage());
            throw new PermisoSrvInternalServErrorException(ex.getMessage());
        }
    }

    /**
     * Method to execute repository function findById using identifier.
     * @param id Identifier to find a record in the table Permiso.
     * @return Permiso object obtained from database.
     */
    private Permiso findById(Integer id) {
        try {
            return repository.findByIdAndActivo(id, ACTIVE_ROWS);
        } catch(Exception ex) {
            log.error("Error when findByIdAndActivo: {}", ex.getMessage());
            throw new PermisoSrvInternalServErrorException(ex.getMessage());
        }
    }

    /**
     * Method to store a record in Permiso table.
     * @param permisoReqDTO Request object to store in the Permiso table.
     * @return A Permiso entity stored in the database.
     */
    private Permiso getSave(PermisoRequestDTO permisoReqDTO) {
        return save(modelMapper.map(permisoReqDTO, Permiso.class));
    }

    /**
     * Method to store a record in Permiso table.
     * @param permiso Permiso object to store in table.
     * @return A Permiso entity stored in the database.
     */
    private Permiso save(Permiso permiso) {
        try {
            return repository.save(permiso);
        } catch(Exception ex) {
            log.error("Error when save: {}", ex.getMessage());
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
