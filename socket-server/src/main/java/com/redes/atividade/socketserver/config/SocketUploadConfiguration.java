package com.redes.atividade.socketserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import com.redes.atividade.socketserver.handlers.SocketUploadHandler;

@Configuration
@EnableWebSocket
public class SocketUploadConfiguration implements WebSocketConfigurer {
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(new SocketUploadHandler(), "/upload").setAllowedOrigins("*");
	}

	@Bean
	public ServletServerContainerFactoryBean createSocketContainer() {
		ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
		container.setMaxBinaryMessageBufferSize(1024000); // unlimited
		return container;
	}

}
