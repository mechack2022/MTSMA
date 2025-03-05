package com.sms.multitenantschool.service.serviceImpl;

import com.sms.multitenantschool.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalFileServiceImpl implements FileService {

    private final Path baseUploadDir;

    public LocalFileServiceImpl(@Value("${upload.directory:${user.home}/Desktop/uploads}") String uploadDirPath) {
        this.baseUploadDir = Paths.get(uploadDirPath);
        try {
            Files.createDirectories(baseUploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create base upload directory", e);
        }
    }


    @Override
    public String uploadFile(String entityId, String fileType, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null; // or throw an appropriate exception
        }
        Path typeDir = baseUploadDir.resolve(fileType);
        Files.createDirectories(typeDir);
        String uniqueFileName = generateUniqueFileName(file.getOriginalFilename());
        Path filePath = typeDir.resolve(uniqueFileName);
        Files.write(filePath, file.getBytes());
        return filePath.toString();
    }

    @Override
    public Resource downloadFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Resource resource = new UrlResource(path.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            throw new IOException("File not found or unreadable: " + filePath);
        }
        return resource;
    }

    @Override
    public void deleteFile(String filePath) throws IOException {
        Files.deleteIfExists(Paths.get(filePath));
    }

    @Override
    public String generateUniqueFileName(String originalFileName) {
        int dotIndex = originalFileName.lastIndexOf('.');
        String baseName = (dotIndex == -1) ? originalFileName : originalFileName.substring(0, dotIndex);
        String extension = (dotIndex == -1) ? "" : originalFileName.substring(dotIndex);
        return UUID.randomUUID() + "_" + baseName + extension;
    }
}
