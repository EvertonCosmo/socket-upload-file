package com.redes.atividade.socketserver.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUploadInFlight {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private String sessionId;

	private String name;

	private String uploadId;

	ByteArrayOutputStream bos = new ByteArrayOutputStream();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public ByteArrayOutputStream getBos() {
		return bos;
	}

	public void setBos(ByteArrayOutputStream bos) {
		this.bos = bos;
	}

	public void append(ByteBuffer byteBuffer) throws IOException {
		bos.write(byteBuffer.array());
	}

	public FileUploadInFlight(String name, String uploadId) {
		super();
		this.name = name;
		this.uploadId = uploadId;

	}
}
