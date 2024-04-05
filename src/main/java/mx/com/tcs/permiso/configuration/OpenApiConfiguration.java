package mx.com.tcs.permiso.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration class to add properties related to API of catpermiso-service
 */
@Configuration
public class OpenApiConfiguration {

    /**
     * Property to get server in DEV environment.
     */
    @Value("${permiso.openapi.dev-url}")
    private String devUrl;

    /**
     * Method to create the Bean of OpenAPI documentation.
     * @return an OpenAPI object.
     */
    @Bean
    public OpenAPI permisoOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        Contact contact = new Contact();
        contact.setEmail("luis.sanmartz@gmail.com");
        contact.setName("Luis Sanchez");

        License apacheLicense = new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0.html");

        Info info = new Info()
                .title("catpermiso-service")
                .version("1.0")
                .contact(contact)
                .description("API para el servicio catpermiso de Estructura de Usuarios")
                .license(apacheLicense);

        return new OpenAPI().info(info).servers(List.of(devServer));
    }
}
