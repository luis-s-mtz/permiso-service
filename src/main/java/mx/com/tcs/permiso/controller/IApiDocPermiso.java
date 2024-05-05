package mx.com.tcs.permiso.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import mx.com.tcs.permiso.model.request.PermisoRequestDTO;
import mx.com.tcs.permiso.model.response.PermisoDTO;
import mx.com.tcs.permiso.model.response.PermisoTipoUsuarioDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Operation(
            summary = "findByParams",
            description = "Obtiene lista de registros de tabla permiso consultada por Id Tipo de Usuario."
    )
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = PermisoTipoUsuarioDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @GetMapping("/api/permiso/findByParams/{idTipoUsuario}")
    ResponseEntity<PermisoTipoUsuarioDTO> findByParams(@PathVariable("idTipoUsuario") Integer idTipoUsuario);

    /**
     * Method used as entry point of the POST function that save a record in  Permiso catalog.
     * @param permisoReqDTO Request used in the POST method to save a record.
     * @return a ResponseEntity of PermisoDTO object.
     */
    @Operation(
            summary = "create",
            description = "Agrega un registro al catalogo permisos."
    )
    @ApiResponse(responseCode = "201", content = {
            @Content(schema = @Schema(implementation = PermisoDTO.class),
                    mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @PostMapping("/api/permiso")
    ResponseEntity<PermisoDTO> create(@RequestBody PermisoRequestDTO permisoReqDTO);

    /**
     * Method used as entry point of the GET function that return the records find by Id of Permiso catalog.
     *
     * @param id Identifier of the record stored in Permiso catalog.
     * @return The record stored in Permiso catalog find by Id.
     */
    @Operation(
            summary = "show",
            description = "Consultar registro de catalogo permiso por Id."
    )
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = PermisoDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @GetMapping("/api/permiso/{id}")
    ResponseEntity<PermisoDTO> getById(@PathVariable("id") Integer id);

    /**
     * Method used as entry point of the PATCH function that update a record in  Permiso catalog.
     *
     * @param id Identifier of the record to update in Permiso catalog.
     * @param permisoReqDTO Request used in the PATCH method to update a record.
     * @return a ResponseEntity of PermisoDTO object.
     */
    @Operation(
            summary = "update",
            description = "Actualiza el registro en el catalogo permisos."
    )
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = PermisoDTO.class),
                    mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @PatchMapping("/api/permiso/{id}")
    ResponseEntity<PermisoDTO> update(@PathVariable("id") Integer id, @RequestBody PermisoRequestDTO permisoReqDTO);

    /**
     * Method used as entry point of the DELETE function that execute a logic delete of Permiso catalog.
     *
     * @param id Identifier of the record stored in Permiso catalog to delete.
     * @return Empty response when execute the logic delete.
     */
    @Operation(
            summary = "delete",
            description = "Opcion realizada para baja logica del registro."
    )
    @ApiResponse(responseCode = "204", content = {
            @Content(schema = @Schema(implementation = Void.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @DeleteMapping("/api/permiso/{id}")
    ResponseEntity<Void> delete(@PathVariable("id") Integer id);
}
