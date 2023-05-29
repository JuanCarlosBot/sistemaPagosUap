package com.pago.uap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;
public class Config {
    private final Logger log = LoggerFactory.getLogger(getClass());

    public String guardarArchivo(MultipartFile archivo) {
		String uniqueFilename = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();

		Path rootPath = Paths.get("uploads/").resolve(uniqueFilename);
		Path rootAbsolutPath = rootPath.toAbsolutePath();

		log.info("rootPath: " + rootPath);
		log.info("rootAbsolutPath: " + rootAbsolutPath);

		try {

			Files.copy(archivo.getInputStream(), rootAbsolutPath);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return uniqueFilename;
	}
}
