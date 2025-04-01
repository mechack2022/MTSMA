package com.sms.multitenantschool.service.serviceImpl;

import com.sms.multitenantschool.Utils.CommonUtils;
import com.sms.multitenantschool.Utils.SecurityUtils.SecurityUtil;
import com.sms.multitenantschool.exceptions.BadRequestException;
import com.sms.multitenantschool.exceptions.ResourceNotFoundException;
import com.sms.multitenantschool.mapper.StudentMapper;
import com.sms.multitenantschool.model.dto.GuardianRequestDTO;
import com.sms.multitenantschool.model.dto.StudentRequestDTO;
import com.sms.multitenantschool.model.dto.StudentResponseDTO;
import com.sms.multitenantschool.model.entity.Guardian;
import com.sms.multitenantschool.model.entity.Student;
import com.sms.multitenantschool.model.entity.Tenant;
import com.sms.multitenantschool.model.entity.User;
import com.sms.multitenantschool.repository.StudentRepository;
import com.sms.multitenantschool.security.CustomUserDetailsService;
import com.sms.multitenantschool.service.FileService;
import com.sms.multitenantschool.service.StudentService;
import com.sms.multitenantschool.service.TenantService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private static final String IMAGE_UPLOAD_DIRECTORY = "student-images";

    private final StudentRepository studentRepository;
    private final TenantService tenantService;
    private final GuardianServiceImpl guardianService;
    private final FileService fileService;
    private final StudentMapper studentMapper;
    private final CustomUserDetailsService userService;

    public StudentServiceImpl(StudentRepository studentRepository, TenantService tenantService,
                              GuardianServiceImpl guardianService, FileService fileService,
                              StudentMapper studentMapper,
                              CustomUserDetailsService userService
    ) {
        this.studentRepository = studentRepository;
        this.tenantService = tenantService;
        this.guardianService = guardianService;
        this.fileService = fileService;
        this.studentMapper = studentMapper;
        this.userService = userService;
    }

    @Transactional
    @Override
    public StudentResponseDTO create(StudentRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        Optional<String> currentUser = SecurityUtil.getCurrentLoginTenant();
        if(currentUser.isEmpty()){
            throw new BadRequestException("Login is Required", "User");
        }
        String username = currentUser.get();
        //use the currentLoginTenant to get the ADMIN
        User admin = userService.getUserByUsername(username);
        Tenant tenant = tenantService.getTenantUuid(admin.getTenantUuid());
        if(tenant == null){
            throw new ResourceNotFoundException("Tenant", "Tenant not found", username);
        }
        // Get the username safely
        studentRepository.findByEmail(request.getEmail())
                .ifPresent(student -> {
                    throw new BadRequestException(request.getEmail(), "Email already used.");
                });

        String studentIdNumber = CommonUtils.generateStudentIdNumber(
                tenant.getTenantAbbr(), request.getFirstName(),
                request.getMiddleName(), request.getLastName()
        );
        Guardian guardian = handleGuardian(request.getGuardian());
        Student newStudent = studentMapper.toEntity(request, tenant, guardian );
        newStudent.setStudentIdNumber(studentIdNumber);
        newStudent.setTenant(tenant);
        newStudent.setGuardian(guardian);
        Student savedStudent = studentRepository.save(newStudent);
//        Handle image upload (optional)
        if (request.getImageFile() != null && !request.getImageFile().isEmpty()) {
            try {
                String imageFilePath = fileService.uploadFile(
                        savedStudent.getId().toString(),
                        IMAGE_UPLOAD_DIRECTORY,
                        request.getImageFile()
                );
                savedStudent.setImageFileName(request.getImageFile().getOriginalFilename());
                savedStudent.setImageContentType(request.getImageFile().getContentType());
                savedStudent.setImageFilePath(imageFilePath);
                savedStudent = studentRepository.save(savedStudent);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload student image", e);
            }
        }
        return studentMapper.toResponseDTO(savedStudent);
    }

    private Guardian handleGuardian(GuardianRequestDTO guardianRequest) {
        return guardianService.getGuardianByEmail(guardianRequest.getEmail())
                .orElseGet(() -> guardianService.save(guardianRequest));
    }
}
