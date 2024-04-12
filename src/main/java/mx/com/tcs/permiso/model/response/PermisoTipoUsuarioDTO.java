package mx.com.tcs.permiso.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author luis
 * @since 1.0
 *
 * Object PermisoTipoUsuario used in the API response of permiso-service to get list of Permiso object find by Id TipoUsuario.
 */
@Schema(name = "PermisoTipoUsuario")
@Setter
@Getter
@ToString
public class PermisoTipoUsuarioDTO {

    /**
     * Name of Permiso data.
     */
    @Schema(type = "string", description = "Tipo de usuario.", example = "Eventual")
    @JsonProperty("tipoUsuario")
    private String tipoUsuario;

    /**
     *
     */
    @Schema(type = "permiso", description = "Lista permisos.")
    @JsonProperty("permisos")
    List<PermisoDTO> permisos;
}
