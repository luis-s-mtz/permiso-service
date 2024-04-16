package mx.com.tcs.permiso.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luis
 * @since 1.0
 *
 * Entity class of the table Permiso.
 */
@Entity
@Table
@Getter
@Setter
@ToString
public class Permiso {

    /**
     * Identifier of record.
     */
    @Id
    @Column
    private Integer id;

    /**
     * Name column.
     */
    @Column
    private String nombre;

    /**
     * Description column.
     */
    @Column
    private String descripcion;

    /**
     * Active column.
     */
    @Column
    private Integer activo;

    /**
     * Identifier of Category.
     */
    @Column
    private Integer idPadre;

    /**
     * Icon column.
     */
    @Column
    private String icono;
}
