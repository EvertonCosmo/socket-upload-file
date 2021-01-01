package com.redes.atividade.socketserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.redes.atividade.socketserver.property.FileStorageProperties;

@EnableConfigurationProperties({ FileStorageProperties.class })
@SpringBootApplication
public class SocketServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocketServerApplication.class, args);
	}

}
