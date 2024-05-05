package mx.com.tcs.permiso.service;

import mx.com.tcs.permiso.model.request.PermisoRequestDTO;
import mx.com.tcs.permiso.model.response.PermisoDTO;
import mx.com.tcs.permiso.model.response.PermisoTipoUsuarioDTO;
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

    /**
     * Method to return all records of Permiso object find by Id TipoUsuario.
     * @param idTipoUsuario The identifier of TipoUsuario entity object.
     * @return A PermisoTipoUsuarioDTO object.
     */
    ResponseEntity<PermisoTipoUsuarioDTO> findByParams(Integer idTipoUsuario);

    /**
     * Method to add a record in the Permiso catalog.
     *
     * @param permisoReqDTO The DTO object with information to add record in the Permiso catalogue.
     * @return The Permiso object added to the database.
     */
    ResponseEntity<PermisoDTO> create(PermisoRequestDTO permisoReqDTO);

    /**
     * Method to get a record by Id in the Permiso catalog.
     *
     * @param id The identifier to find a record in Permiso catalog.
     * @return The Permiso object find by Id.
     */
    ResponseEntity<PermisoDTO> getById(Integer id);

    /**
     * Method to execute a partial update of the record in the Permiso catalog find by Id.
     *
     * @param id The identifier to find a record to update in Permiso catalog.
     * @param permisoReqDTO The DTO object with partial information to update the record in the Permiso catalogue.
     * @return The Permiso object added to the database.
     */
    ResponseEntity<PermisoDTO> update(Integer id, PermisoRequestDTO permisoReqDTO);

    /**
     * Method to get a logic delete of the record, using the identifier in the Permiso catalog.
     *
     * @param id The identifier to execute a logic delete of the record in Permiso catalog.
     * @return Empty response.
     */
    ResponseEntity<Void> delete(Integer id);
}
