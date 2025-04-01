package com.sms.multitenantschool.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    /**
     * Uploads a file and returns its stored path.
     * @param entityId Unique identifier for the entity (e.g., staffId, studentId)
     * @param fileType Category of the file (e.g., "cv", "photo", "document")
     * @param file The file to upload
     * @return The stored file path or URL
     * @throws IOException If upload fails
     */
    default String uploadFile(String entityId, String fileType, MultipartFile file) throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    };

   default String uploadFile(String fileType, MultipartFile file) throws IOException {
       throw new UnsupportedOperationException("Not implemented");
   };

    /**
     * Downloads a file as a Resource.
     * @param filePath The stored path or URL of the file
     * @return The file as a Resource
     * @throws IOException If download fails
     */
    default Resource downloadFile(String filePath) throws IOException{
        throw new UnsupportedOperationException("Not implemented");
    };

    /**
     * Deletes a file.
     * @param filePath The stored path or URL of the file
     * @throws IOException If deletion fails
     */
    default void deleteFile(String filePath) throws IOException{
        throw new UnsupportedOperationException("Not implemented");
    };

    /**
     * Generates a unique filename.
     * @param originalFileName The original filename from the uploaded file
     * @return A unique filename
     */
    default String generateUniqueFileName(String originalFileName){
        throw new UnsupportedOperationException("Not implemented");
    }
    ;
}
