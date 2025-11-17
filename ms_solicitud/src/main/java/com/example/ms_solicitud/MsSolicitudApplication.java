package com.example.ms_solicitud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {
        SecurityAutoConfiguration.class,
})
public class MsSolicitudApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsSolicitudApplication.class, args);
	}

}
