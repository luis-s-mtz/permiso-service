package mx.com.tcs.permiso.model.repository;

import mx.com.tcs.permiso.model.entity.TipoUsuarioPermiso;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author luis
 * @since 1.0
 *
 * Repository interface of the TipoUsuarioPermiso entity.
 */
public interface TipoUsuarioPermisoRepository extends CrudRepository<TipoUsuarioPermiso,Integer> {

    /**
     * Method to find List of Permiso related to Identifier of TipoUsuario where the query parameter are
     * the Identifier of Tipo Usuario and active status.
     * @param idTipoUsuario Identifier of TipoUsuario entity.
     * @param activo Active status of record.
     * @return List of Permiso entities related to Identifier of the TipoUsuario.
     */
    List<TipoUsuarioPermiso> findByIdTipoUsuarioAndActivo(Integer idTipoUsuario, Integer activo);
}
