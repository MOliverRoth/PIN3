package com.udesc.KeyControl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.udesc.KeyControl.models.Usuario;

@SpringBootApplication
public class KeyControlApplication {

	public static Usuario actualUser;

	public static void main(String[] args) {
		SpringApplication.run(KeyControlApplication.class, args);
	}

	@Bean
    public WebMvcConfigurer configCORS() {
        return new WebMvcConfigurer() {
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("*")
                        .allowedOrigins("http://localhost:5173");
            }
        };
    }

}
