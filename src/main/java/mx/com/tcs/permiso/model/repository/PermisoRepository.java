package mx.com.tcs.permiso.model.repository;

import mx.com.tcs.permiso.model.Permiso;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author luis
 * @since 1.0
 *
 * Repository of the Permiso entity.
 */
@Repository
public interface PermisoRepository  extends CrudRepository<Permiso,Integer> {
}
