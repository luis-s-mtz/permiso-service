package mx.com.tcs.permiso.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author luis
 * @since 1.0
 *
 * Entity class of the relation between TipoUsuario and Permiso entities.
 */
@Entity
@Table(name = "Tipo_Usuario_Permiso")
@Getter
@Setter
@ToString
public class TipoUsuarioPermiso {

    /**
     * Identifier of record.
     */
    @Id
    @Column
    private Integer id;

    /**
     * Identifier of TipoUsuario entity.
     */
    @Column(name = "id_Tipo_Usuario")
    private Integer idTipoUsuario;

    /**
     * Identifier of Permiso entity.
     */
    @Column(name = "id_Permiso")
    private Integer idPermiso;

    /**
     * Active column.
     */
    @Column
    private Integer activo;
}
