package com.redes.atividade.socketserver.model;

public class SessionData {
	private String fileName;
	private String sessionid;
	private String uploadId;

	public SessionData(String fileName, String sessionid, String uploadId) {
		super();
		this.fileName = fileName;
		this.sessionid = sessionid;
		this.uploadId = uploadId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

}
