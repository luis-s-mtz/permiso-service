package mx.com.tcs.permiso.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import mx.com.tcs.permiso.model.response.PermisoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author luis
 * @since 1.0
 *
 * Inteface to define methods used in the Controller class.
 */
public interface IApiDocPermiso {

    /**
     * Method used as entry point of the GET function that return all records of Permiso catalog.
     * @return a ResponseEntity of List of PermisoDTO object.
     */
    @Operation(
            summary = "index",
            description = "Obtiene lista de registros de catpermiso"
    )
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = PermisoDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @GetMapping("/api/permiso")
    ResponseEntity<List<PermisoDTO>> getAll();
}
