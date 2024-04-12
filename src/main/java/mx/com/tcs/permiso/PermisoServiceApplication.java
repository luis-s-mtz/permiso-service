package mx.com.tcs.permiso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class PermisoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PermisoServiceApplication.class, args);
	}

}
