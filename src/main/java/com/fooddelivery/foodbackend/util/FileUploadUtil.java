package com.fooddelivery.foodbackend.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class FileUploadUtil {

    private static final String UPLOAD_DIR = "images/";

    public String uploadFile(MultipartFile file) throws IOException {

        Files.createDirectories(Paths.get(UPLOAD_DIR));

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        Path filePath = Paths.get(UPLOAD_DIR, fileName);

        Files.copy(file.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING);

        return "/images/" + fileName;
    }

    public void deleteFile(String imageUrl) throws IOException {

        if (imageUrl == null || imageUrl.isBlank()) {
            return;
        }

        String fileName = imageUrl.replace("/images/", "");

        Path filePath = Paths.get(UPLOAD_DIR, fileName);

        Files.deleteIfExists(filePath);
    }
}