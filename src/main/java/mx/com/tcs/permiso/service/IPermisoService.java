package mx.com.tcs.permiso.service;

import mx.com.tcs.permiso.model.response.PermisoDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author luis
 * @since 1.0
 *
 * Interface to define the methods used in each endpoints of the API catpermiso-service.
 */
public interface IPermisoService {

    /**
     * Method to return all records of Permiso object.
     * @return a response entity of list or Permiso object.
     */
    ResponseEntity<List<PermisoDTO>> listAll();

}
