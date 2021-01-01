package com.redes.atividade.socketserver.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import com.redes.atividade.socketserver.model.FileUploadInFlight;

public abstract class SessionFiles {
	private static Map<String, FileUploadInFlight> sessionFiles = new WeakHashMap<>();
	private static List<FileUploadInFlight> files = new ArrayList();

	public static Map<String, FileUploadInFlight> getSessionFiles() {
		return sessionFiles;
	}

	public static void setSessionFiles(Map<String, FileUploadInFlight> sessionFiles) {
		SessionFiles.sessionFiles = sessionFiles;
	}

	public static void add(String session, FileUploadInFlight file) {
		sessionFiles.put(session, file);
		files.add(file);

	}

	public static void remove(String session, String uploadId) {
		sessionFiles.remove(session);
		files.remove(files.stream().filter(item -> item.getUploadId().equals(uploadId)).findFirst());
	}

	public static FileUploadInFlight get(String session) {
		return sessionFiles.get(session);
	}

	public static List<FileUploadInFlight> getAll() {
		return files;
	}

}
