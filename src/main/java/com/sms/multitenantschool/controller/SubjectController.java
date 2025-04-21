package com.sms.multitenantschool.controller;

import com.sms.multitenantschool.model.dto.*;
import com.sms.multitenantschool.service.serviceImpl.SubjectServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/subject")
@AllArgsConstructor
public class SubjectController {

    private final SubjectServiceImpl subjectService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<List<SubjectResponseDTO>>> register(@RequestBody List<SubjectRequestDTO> req) {
        try {
            List<SubjectResponseDTO> res = subjectService.createSubjects(req);
            return ResponseEntity.ok(new ApiResponse<>(
                    res,
                    "Subject " + (res.size() > 1 ? "s" : "") + "registered sucessfully"
            ));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(
                            "Fail to register subject"+ (req.size() > 1 ? "s" : ""),
                            e.getMessage()
                    ));
        }
    }
}
