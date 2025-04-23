package com.sms.multitenantschool.controller;


import com.sms.multitenantschool.model.dto.ApiResponse;
import com.sms.multitenantschool.model.dto.TeacherRequestDTO;
import com.sms.multitenantschool.model.dto.TeacherResponseDTO;
import com.sms.multitenantschool.service.serviceImpl.TeacherServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/teacher")
@Slf4j
public class TeacherController {

    private final TeacherServiceImpl teacherService;

    public TeacherController(TeacherServiceImpl teacherService) {
        this.teacherService = teacherService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<TeacherResponseDTO>> save(@RequestBody TeacherRequestDTO req) {
        log.info("Received request to create teacher: teacherId={}", req.getTeacherId());
        TeacherResponseDTO res = teacherService.create(req);
        return ResponseEntity.ok(new ApiResponse<>(res, "New Teacher created"));
    }


}
