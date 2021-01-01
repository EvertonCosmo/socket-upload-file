package com.redes.atividade.socketserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class UploadEventListener {

	private static final Logger logger = LoggerFactory.getLogger(UploadEventListener.class);

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

//	@EventListener
//	public void handlerSocketEventListener(SessionConnectedEvent event) {
//		logger.info("new connection in socket received");
//	}

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

		String username = (String) headerAccessor.getSessionAttributes().get("username");
		if (username != null) {
			logger.info("User Disconnected : " + username);

			this.messagingTemplate.convertAndSend("/topic/public", "Desconectado");
		}
	}
}