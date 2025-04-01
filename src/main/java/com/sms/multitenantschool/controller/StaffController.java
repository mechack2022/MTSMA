package com.sms.multitenantschool.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.multitenantschool.Utils.ValidationUtils;
import com.sms.multitenantschool.model.dto.ApiResponse;
import com.sms.multitenantschool.model.dto.StaffRequestDTO;
import com.sms.multitenantschool.model.dto.StaffResponseDTO;
import com.sms.multitenantschool.service.serviceImpl.StaffServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffServiceImpl staffService;
    private final ValidationUtils validationUtils;
    private final ObjectMapper objectMapper;
//    ObjectMapper objectMapper = new ObjectMapper();

    public StaffController(StaffServiceImpl staffService, ValidationUtils validationUtils,ObjectMapper objectMapper) {
        this.staffService = staffService;
        this.validationUtils = validationUtils;
        this.objectMapper = objectMapper;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<StaffResponseDTO>> createStaff(
            @RequestParam Long tenantId,
            @RequestPart("staff") String staffJson,
            @RequestPart(value = "cvFile", required = false) MultipartFile cvFile,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {


        StaffRequestDTO staffRequestDto = objectMapper.readValue(staffJson, StaffRequestDTO.class);
        if (cvFile != null) {
            staffRequestDto.setCvFile(cvFile);
        }
        if (imageFile != null) {
            staffRequestDto.setImageFile(imageFile);
        }
        // Validate using the utility class
        validationUtils.validate(staffRequestDto);
        StaffResponseDTO createdStaff = staffService.createStaff(tenantId, staffRequestDto);
        return ResponseEntity.ok(new ApiResponse<>(createdStaff, "Staff created successfully"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/{staffId}/cv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<StaffResponseDTO>> uploadCv(
            @PathVariable Long staffId,
            @RequestParam("file") MultipartFile cvFile) throws IOException {
        StaffResponseDTO updatedStaff = staffService.uploadCv(staffId, cvFile);
        return ResponseEntity.ok(new ApiResponse<>(updatedStaff, "CV uploaded successfully"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/{staffId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<StaffResponseDTO>> uploadImage(
            @PathVariable Long staffId,
            @RequestParam("file") MultipartFile imageFile) throws IOException {
        StaffResponseDTO updatedStaff = staffService.uploadImage(staffId, imageFile);
        return ResponseEntity.ok(new ApiResponse<>(updatedStaff, "Image uploaded successfully"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{staffId}/cv")
    public ResponseEntity<Resource> downloadCv(
            @PathVariable Long staffId,
            HttpServletResponse response) throws IOException {
        Resource file = staffService.downloadCv(staffId);
        StaffResponseDTO staffDto = staffService.findById(staffId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(staffDto.getCvContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + staffDto.getCvFileName() + "\"")
                .body(file);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{staffId}/image")
    public ResponseEntity<Resource> downloadImage(
            @PathVariable Long staffId,
            HttpServletResponse response) throws IOException {
        Resource file = staffService.downloadImage(staffId);
        StaffResponseDTO staffDto = staffService.findById(staffId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(staffDto.getImageContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + staffDto.getImageFileName() + "\"")
                .body(file);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/{staffId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<StaffResponseDTO>> updateStaff(
            @PathVariable Long staffId,
            @RequestPart("staff") String staffJson,
            @RequestPart(value = "cvFile", required = false) MultipartFile cvFile,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {

        StaffRequestDTO staffRequestDto = objectMapper.readValue(staffJson, StaffRequestDTO.class);

        if (cvFile != null) {
            staffRequestDto.setCvFile(cvFile);
        }
        if (imageFile != null) {
            staffRequestDto.setImageFile(imageFile);
        }

        StaffResponseDTO updatedStaff = staffService.updateStaff(staffId, staffRequestDto);
        return ResponseEntity.ok(new ApiResponse<>(updatedStaff, "Staff updated successfully"));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{staffId}")
    public ResponseEntity<ApiResponse<String>> archiveStaff(@PathVariable Long staffId) {
        String message = staffService.deleteStaff(staffId);
        return ResponseEntity.ok(new ApiResponse<>(message, "Staff archived successfully"));
    }
}