package com.udesc.KeyControl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.udesc.KeyControl.models.Usuario;

@SpringBootApplication
public class KeyControlApplication {

	public static Usuario actualUser;

	public static void main(String[] args) {
		SpringApplication.run(KeyControlApplication.class, args);
	}

}
