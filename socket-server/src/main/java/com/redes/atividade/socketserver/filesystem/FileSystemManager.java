package com.redes.atividade.socketserver.filesystem;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import com.redes.atividade.socketserver.property.FileStorageProperties;

@Service
public class FileSystemManager {

	private static FileChannel channel;

	private static Path fileStorageLocation = null;

//	private static Path basePath;

	@Autowired
	public FileSystemManager(FileStorageProperties fileStorageProperties) {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			ex.printStackTrace();
//			throw new Exception("Could not create the directory where the uploaded files will be stored.", ex);
		}
	}

	public static void save(String fileName, String prefix, String sessionId, byte[] data) throws IOException {
		FileSystemManager.save(fileName, prefix, sessionId, ByteBuffer.wrap(data));
	}

	public static void save(String fileName, String prefix, String sessionId, ByteBuffer buffer) throws IOException {
//		basePath = Paths.get(".", "uploads", prefix);

		channel = new FileOutputStream(Paths.get(FileSystemManager.fileStorageLocation.toString(), fileName).toFile(),
				false).getChannel();
		channel.write(buffer);
		channel.close();

	}

	public static Resource read(String fileName) throws FileNotFoundException {
//		RandomAccessFile reader = new RandomAccessFile(filename, "r");
//		channel = new FileInputStream(Paths.get(basePath.toString(), fileName).toFile(), false).getChannel();
//		
//		channel

		Path filePath = FileSystemManager.fileStorageLocation
				.resolve(Paths.get(FileSystemManager.fileStorageLocation.toString(), fileName));
		System.out.println("FILEPATH: " + filePath.toString());
		System.out.println("FILEPATH: " + FileSystemManager.fileStorageLocation.toString());
		Resource resource;
		try {
			resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new FileNotFoundException("File not found: " + fileName);
			}
		} catch (MalformedURLException e) {
			throw new FileNotFoundException("File not found :" + fileName);
		}

	}
}
