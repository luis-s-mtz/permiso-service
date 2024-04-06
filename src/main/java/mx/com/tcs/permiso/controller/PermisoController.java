package mx.com.tcs.permiso.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import mx.com.tcs.permiso.model.response.PermisoDTO;
import mx.com.tcs.permiso.service.IPermisoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author luis
 * @since 1.0
 *
 * Controller class to define endpoints of API catpermiso-service.
 */
@Tag(name = "Permiso", description = "")
@RestController()
public class PermisoController implements IApiDocPermiso {

    /**
     * Object to implement the funcionality of endpoint methods.
     */
    private final IPermisoService permisoService;

    /**
     * Constructor of the Controller class
     * @param permisoService
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
}
