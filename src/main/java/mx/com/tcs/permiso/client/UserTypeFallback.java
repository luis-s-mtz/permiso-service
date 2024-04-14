package mx.com.tcs.permiso.client;

import mx.com.tcs.permiso.exception.PermisoSrvInternalServErrorException;
import mx.com.tcs.permiso.model.client.UserTypeDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author luis
 * @since 1.0
 *
 * Component class to implements fallback when UserTypeFeignClient use the CircuitBreaker
 * pattern.
 */
@Component
public class UserTypeFallback implements UserTypeFeignClient{
    /**
     * Method used to get UserType record find by Id.
     * @param id Identifier of UserType objet to find.
     * @return An ResponseEntity object of UserTypeDTO.
     */
    @Override
    public ResponseEntity<UserTypeDTO> getById(Integer id) {
        throw new PermisoSrvInternalServErrorException("When request to UserTypeFeignClient.");
    }
}
