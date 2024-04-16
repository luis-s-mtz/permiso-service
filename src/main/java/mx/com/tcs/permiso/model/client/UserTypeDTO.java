package mx.com.tcs.permiso.model.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author luis
 * @since 1.0
 *
 * DTO object of UserType used in the response of tipousuario-service feign client.
 */
@Schema(name = "TipoUsuario")
@Setter
@Getter
@ToString
public class UserTypeDTO {

    /**
     * Identifier of TipoUsuario data.
     */
    @Schema(type = "integer", description = "Id del catalogo", example = "1")
    @JsonProperty("id")
    private Integer id;

    /**
     * Description of name.
     */
    @Schema(type = "string", description = "Descripcion del tipo de usuario", example = "Eventual")
    @JsonProperty("descripcion")
    private String description;

    /**
     * Attribute to identify if it is active (1) or not (0)
     */
    @Schema(type = "integer", description = "Bandera de baja logica", example = "1")
    @JsonProperty("activo")
    private Integer status;
}
