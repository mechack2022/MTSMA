package com.sms.multitenantschool.controller;

import com.sms.multitenantschool.model.dto.ApplicationCodesetDTO;
import com.sms.multitenantschool.service.ApplicationCodesetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/codesets")
public class ApplicationCodesetController {

    private final ApplicationCodesetService applicationCodesetService;

    public ApplicationCodesetController(ApplicationCodesetService applicationCodesetService) {
        this.applicationCodesetService = applicationCodesetService;
    }

    @PreAuthorize("hasRole('ADIN')")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("fileType") String fileType) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Uploaded file is empty.");
            }
            System.out.println("Uploaded file name: " + file.getOriginalFilename());
            System.out.println("Uploaded file size: " + file.getSize());

            List<ApplicationCodesetDTO> codesetDTOList = applicationCodesetService.readFileData(file, fileType);
            applicationCodesetService.saveCodesets(codesetDTOList);
            return ResponseEntity.ok("File uploaded and codesets saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while processing the file: " + e.getMessage());
        }
    }
}