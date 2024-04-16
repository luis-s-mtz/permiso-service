package mx.com.tcs.permiso.client;

import mx.com.tcs.permiso.configuration.FeignConfiguration;
import mx.com.tcs.permiso.exception.CustomErrorDecoder;
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
@FeignClient(name = "tipousuario-service", path = "/api/tipousuario",
        configuration = FeignConfiguration.class, fallback = UserTypeFallback.class)
public interface UserTypeFeignClient {

    /**
     * Method used to get UserType record find by Id.
     * @param id Identifier of UserType objet to find.
     * @return An ResponseEntity object of UserTypeDTO.
     */
    @GetMapping("/{id}")
    ResponseEntity<UserTypeDTO> getById(@PathVariable(value = "id")Integer id);
}
