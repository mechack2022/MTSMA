package com.sms.multitenantschool.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.multitenantschool.Utils.ValidationUtils;
import com.sms.multitenantschool.model.dto.ApiResponse;
import com.sms.multitenantschool.model.dto.StudentRequestDTO;
import com.sms.multitenantschool.model.dto.StudentResponseDTO;
import com.sms.multitenantschool.service.serviceImpl.StudentServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("api/v1/student")
@RestController
@AllArgsConstructor
public class StudentController {

    private final StudentServiceImpl studentService;
    private final StudentServiceImpl studentServiceImpl;
    private final ObjectMapper objectMapper;
    private final ValidationUtils validationUtils;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<StudentResponseDTO>> createStudent(
             @RequestPart("student") String studentJson,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            StudentRequestDTO studentRequestDTO = objectMapper.readValue(studentJson, StudentRequestDTO.class);
            if (imageFile != null) {
                studentRequestDTO.setImageFile(imageFile);
            }
            // validate request
            validationUtils.validate(studentRequestDTO);
            StudentResponseDTO res = studentServiceImpl.create(studentRequestDTO);
            return ResponseEntity.ok(new ApiResponse<>(res, "Student created Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    null,
                    "Invalid input: " + e.getMessage()
            ));
        } catch (RuntimeException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(
                    null,
                    "Failed to create new student: " + e.getMessage()
            ));
        }
    }
}
