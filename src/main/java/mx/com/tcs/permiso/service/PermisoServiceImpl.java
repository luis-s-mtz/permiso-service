package mx.com.tcs.permiso.service;

import lombok.extern.slf4j.Slf4j;
import mx.com.tcs.permiso.exception.ItemNotFoundException;
import mx.com.tcs.permiso.exception.PermisoSrvInternalServErrorException;
import mx.com.tcs.permiso.model.Permiso;
import mx.com.tcs.permiso.model.repository.PermisoRepository;
import mx.com.tcs.permiso.model.response.PermisoDTO;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    public PermisoServiceImpl(PermisoRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    /**
     * Implements the method to get all records of Permiso entity.
     * @return a response entity of the list of the Permiso object.
     */
    @Override
    public ResponseEntity<List<PermisoDTO>> listAll() {
        return ResponseEntity.ok(getAllPermiso());
    }

    /**
     * Method to query from Permiso entity.
     * @return a list of Permiso object.
     */
    private List<PermisoDTO> getAllPermiso() {

        try {
            List<Permiso> permisoList = (List<Permiso>) repository.findAll();

            if (permisoList.isEmpty()) {
                throw new ItemNotFoundException("The query to permiso table return empty list.");
            }

            return permisoList.
                    stream().
                    map(permiso -> modelMapper.map(permiso, PermisoDTO.class)).
                    toList();
        } catch(Exception ex) {
            log.error("Error in Service permiso API: {}",ex.getMessage());
            throw new PermisoSrvInternalServErrorException(ex.getMessage());
        }
    }
}
