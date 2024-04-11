package mx.com.tcs.permiso.service;

import lombok.extern.slf4j.Slf4j;
import mx.com.tcs.permiso.exception.ItemNotFoundException;
import mx.com.tcs.permiso.exception.PermisoSrvInternalServErrorException;
import mx.com.tcs.permiso.model.Permiso;
import mx.com.tcs.permiso.model.repository.PermisoRepository;
import mx.com.tcs.permiso.model.response.PermisoDTO;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
public class PermisoServiceImpl implements  IPermisoService {

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

    public PermisoServiceImpl(PermisoRepository repository, ModelMapper modelMapper,
                              CircuitBreakerFactory circtBreakFactory) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.circtBreakFactory = circtBreakFactory;
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
