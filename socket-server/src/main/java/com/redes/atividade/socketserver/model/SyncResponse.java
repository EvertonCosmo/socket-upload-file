package com.redes.atividade.socketserver.model;

import java.net.URI;

public class SyncResponse {

	private URI uri;
	private String fileName;

	public SyncResponse(String fileName, URI uri) {
		super();
		this.uri = uri;
		this.fileName = fileName;
	}

	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
