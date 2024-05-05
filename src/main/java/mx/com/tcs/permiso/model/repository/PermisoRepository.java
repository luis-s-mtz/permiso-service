package mx.com.tcs.permiso.model.repository;

import mx.com.tcs.permiso.model.entity.Permiso;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author luis
 * @since 1.0
 *
 * Repository of the Permiso entity.
 */
@Repository
public interface PermisoRepository  extends CrudRepository<Permiso,Integer> {

    /**
     * Method to find List of Permiso where the query parameter are the Identifiers and active status.
     * @param ids List of Identifiers of Permiso.
     * @param activo Active status of record.
     * @return List of Permiso entities.
     */
    List<Permiso> findByIdInAndActivo(List<Integer> ids, Integer activo);

    /**
     * Method to find Permiso where the query parameter are the Identifier and active status.
     * @param id Identifier of Permiso.
     * @param activo Active status of record.
     * @return List of Permiso entities.
     */
    Permiso findByIdAndActivo(Integer id, Integer activo);
}
