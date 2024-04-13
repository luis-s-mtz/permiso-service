package mx.com.tcs.permiso.configuration;

import feign.codec.ErrorDecoder;
import mx.com.tcs.permiso.exception.CustomErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author luis 
 * @since 1.0
 * Configuration class to create an ErrorDecoder Bean using the CustomerErrorDecoder.
 */
@Configuration
public class FeignConfiguration {

    /**
     * Method to create the Bean of ErrorDecoder.
     * @return An ErrorDecoder Bean.
     */
    @Bean
    ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

}
