package com.redes.atividade.socketserver.handlers;

import java.nio.ByteBuffer;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import com.google.common.base.Splitter;
import com.redes.atividade.socketserver.filesystem.FileSystemManager;
import com.redes.atividade.socketserver.model.FileUploadInFlight;
import com.redes.atividade.socketserver.model.SessionData;
import com.redes.atividade.socketserver.repository.SessionFiles;

@Component
@Controller
public class SocketUploadHandler extends BinaryWebSocketHandler {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public boolean supportsPartialMessages() {
		return true;
	}

	@EventListener
	public void handlerSocketEventListener(SessionConnectedEvent event) {
		logger.info("new connection in socket received");
	}

	// add new FileUpload in sessionFiles when connection is established
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("User connected");
		var sessionData = getSessionId(session);

		var decriptedSessionFile = new FileUploadInFlight(sessionData.getFileName(), sessionData.getUploadId());

		logger.info("session:{} ", session);
		SessionFiles.add(sessionData.getSessionid(), decriptedSessionFile);

	}

	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
		var sessionData = getSessionId(session);
		ByteBuffer payload = message.getPayload();

		FileUploadInFlight sessionFile = SessionFiles.get(sessionData.getSessionid());

		if (sessionFile == null) {
			throw new IllegalStateException("Not expected");
		}
		System.out.println(sessionFile.getName());
		sessionFile.append(payload);
		// because supportsPartialMessages is true
		if (message.isLast()) {
			FileSystemManager.save(sessionFile.getName(), "websocket", sessionData.getSessionid(),
					sessionFile.getBos().toByteArray());
			session.sendMessage(new TextMessage("UPLOAD:" + sessionFile.getName()));
			session.close();
//			SessionFiles.remove(sessionData.getSessionid(),sessionData.getUploadId());
			logger.info("file uploaded" + sessionFile.getName());

		}

	}

	// getSocketStomp ID
	private SessionData getSessionId(WebSocketSession session) {
		String query = session.getUri().getQuery();
		logger.info("query :  {}", query.split("=")[1]);

		String uploadSessionIdBase64 = query.split("=")[1];

		String uploadSessionId = new String(Base64Utils.decodeUrlSafe(uploadSessionIdBase64.getBytes()));

		List<String> sessionIdentifiers = Splitter.on("\\").splitToList(uploadSessionId);

		return new SessionData(sessionIdentifiers.get(1), sessionIdentifiers.get(2), sessionIdentifiers.get(0));

	}
}
