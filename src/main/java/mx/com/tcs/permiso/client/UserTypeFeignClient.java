package mx.com.tcs.permiso.client;

import mx.com.tcs.permiso.model.client.UserTypeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author luis
 * @since 1.0
 * Interface to create feign client to request tipousuario-service and get the description of the UserType objec.
 */
@FeignClient(name = "tipousuario-service", path = "/api/tipousuario"
        , url = "localhost:8091")
public interface UserTypeFeignClient {

    @GetMapping("/{id}")
    ResponseEntity<UserTypeDTO> getById(@PathVariable(value = "id")Integer id);
}
