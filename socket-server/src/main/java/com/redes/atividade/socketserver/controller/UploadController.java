package com.redes.atividade.socketserver.controller;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.redes.atividade.socketserver.filesystem.FileSystemManager;
import com.redes.atividade.socketserver.model.FileUploadInFlight;
import com.redes.atividade.socketserver.model.SyncResponse;
import com.redes.atividade.socketserver.repository.SessionFiles;

@Controller
public class UploadController {
	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

	@Autowired
	private SimpMessagingTemplate template;

	@Autowired
	private FileSystemManager fileSystemManager;

	private Gson gson = new Gson();

	@MessageMapping("/upload.sendData")
	@SendTo("/topic/public")
	public String FileMessage(@Payload String message) {
		System.out.println("MESSAGE: " + message);
//		this.template.convertAndSend("/topic/public", message);
		var response = new SyncResponse(message, URI.create("#s"));
		return gson.toJson(response);

	}

	@MessageMapping("/upload.syncData")
	@SendToUser("/queue/reply")
//	@SendTo("/topic/public")
	public String getUploads(@Payload String message, Principal user, SimpMessageHeaderAccessor acessor)
			throws IOException {
//		logger.info("received message {} from {} ", message, user.getName());
		logger.info("acessor:{} ", acessor);
		var files = SessionFiles.getAll();
		List<SyncResponse> filesResource = new ArrayList<>();
		for (FileUploadInFlight fileUploadInFlight : files) {
			var fileResource = FileSystemManager.read(fileUploadInFlight.getName());
			filesResource.add(new SyncResponse(fileResource.getFilename(), fileResource.getURI()));

		}

		return gson.toJson(filesResource);

	}

}
