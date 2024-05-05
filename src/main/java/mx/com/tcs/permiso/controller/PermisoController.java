package mx.com.tcs.permiso.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import mx.com.tcs.permiso.model.request.PermisoRequestDTO;
import mx.com.tcs.permiso.model.response.PermisoDTO;
import mx.com.tcs.permiso.model.response.PermisoTipoUsuarioDTO;
import mx.com.tcs.permiso.service.IPermisoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author luis
 * @since 1.0
 *
 * Controller class to define endpoints of API catpermiso-service.
 */
@Tag(name = "Permiso")
@RestController()
public class PermisoController implements IApiDocPermiso {

    /**
     * Object to implement the funcionality of endpoint methods.
     */
    private final IPermisoService permisoService;

    /**
     * Constructor of the Controller class
     * @param permisoService Parameter used in constructor, to call service layer.
     */
    @Autowired
    public PermisoController(IPermisoService permisoService) {
        this.permisoService = permisoService;
    }

    /**
     * Endpoint used to get all records in Permiso entity.
     * @return a ResponseEntity of the list of Permiso object.
     */
    @Override
    public ResponseEntity<List<PermisoDTO>> getAll() {
        return permisoService.listAll();
    }

    /**
     * Endpoint used to get all records in Permiso entity find by idTipoUsuario.
     *
     * @param idTipoUsuario The identifier of the UserType entity.
     * @return A ResponseEntity of the PermisoTipoUsuarioDTO object.
     */
    @Override
    public ResponseEntity<PermisoTipoUsuarioDTO> findByParams(Integer idTipoUsuario) {
        return permisoService.findByParams(idTipoUsuario);
    }

    /**
     * Method used as entry point of the POST function that save a record in  Permiso catalog.
     *
     * @param permisoReqDTO Request used in the POST method to save a record.
     * @return a ResponseEntity of PermisoDTO object.
     */
    @Override
    public ResponseEntity<PermisoDTO> create(PermisoRequestDTO permisoReqDTO) {
        return permisoService.create(permisoReqDTO);
    }

    /**
     * Method used as entry point of the GET function that return the records find by Id of Permiso catalog.
     *
     * @param id Identifier of the record stored in Permiso catalog.
     * @return The record stored in Permiso catalog find by Id.
     */
    @Override
    public ResponseEntity<PermisoDTO> getById(Integer id) {
        return permisoService.getById(id);
    }

    /**
     * Method used as entry point of the PATCH function that update a record in  Permiso catalog.
     *
     * @param id Identifier of the record to update in Permiso catalog.
     * @param permisoReqDTO Request used in the PATCH method to update a record.
     * @return a ResponseEntity of PermisoDTO object.
     */
    @Override
    public ResponseEntity<PermisoDTO> update(
            @PathVariable("id") Integer id, PermisoRequestDTO permisoReqDTO) {
        return permisoService.update(id,permisoReqDTO);
    }

    /**
     * Method used as entry point of the DELETE function that execute a logic delete of Permiso catalog.
     *
     * @param id Identifier of the record stored in Permiso catalog to delete.
     * @return Empty response when execute the logic delete.
     */
    @Override
    public ResponseEntity<Void> delete(Integer id) {
        return permisoService.delete(id);
    }
}
