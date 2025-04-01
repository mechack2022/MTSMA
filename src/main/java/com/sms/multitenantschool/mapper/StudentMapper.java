package com.sms.multitenantschool.mapper;

import com.sms.multitenantschool.model.dto.StudentRequestDTO;
import com.sms.multitenantschool.model.dto.StudentResponseDTO;
import com.sms.multitenantschool.model.entity.Guardian;
import com.sms.multitenantschool.model.entity.Student;
import com.sms.multitenantschool.model.entity.Tenant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StudentMapper {

    // Convert StudentRequestDTO to Student entity
    public  Student toEntity(StudentRequestDTO dto, Tenant tenant, Guardian guardian) {
        Student student = new Student();
        student.setTenant(tenant);
        student.setFirstName(dto.getFirstName());
        student.setMiddleName(dto.getMiddleName());
        student.setLastName(dto.getLastName());
        student.setDateOfBirth(dto.getDateOfBirth());
        student.setGender(dto.getGender());
        student.setEmail(dto.getEmail());
        student.setPhoneNumber(dto.getPhoneNumber());
        student.setGradeLevel(dto.getGradeLevel());
        student.setEnrollmentDate(dto.getEnrollmentDate());
        student.setGuardian(guardian);
        student.setAddress(dto.getAddress() != null ? dto.getAddress().toEntity() : null);

        if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
            student.setImageFileName(dto.getImageFile().getOriginalFilename());
            student.setImageContentType(dto.getImageFile().getContentType());
        }

        return student;
    }

    // Convert Student entity to StudentResponseDTO
    public  StudentResponseDTO toResponseDTO(Student student) {
        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(student.getId());
        dto.setStudentUuid(student.getStudentUuid());
        dto.setStudentIdNumber(student.getStudentIdNumber());
        dto.setTenantId(student.getTenant().getId());
        dto.setFirstName(student.getFirstName());
        dto.setMiddleName(student.getMiddleName());
        dto.setLastName(student.getLastName());
        dto.setDateOfBirth(student.getDateOfBirth());
        dto.setGender(student.getGender());
        dto.setEmail(student.getEmail());
        dto.setPhoneNumber(student.getPhoneNumber());
        dto.setGradeLevel(student.getGradeLevel());
        dto.setEnrollmentDate(student.getEnrollmentDate());
        dto.setEnrollmentStatus(student.getEnrollmentStatus());
        dto.setGuardian(student.getGuardian() != null ?
                GuardianMapper.toRequestDTO(student.getGuardian()) : null);
        dto.setAddress(student.getAddress());
        dto.setImageFileName(student.getImageFileName());
        dto.setImageContentType(student.getImageContentType());
        dto.setImageFilePath(student.getImageFilePath());

        return dto;
    }

    // Update existing Student entity from StudentRequestDTO
    public void updateEntityFromDTO(Student student, StudentRequestDTO dto) {
        student.setFirstName(dto.getFirstName());
        student.setMiddleName(dto.getMiddleName());
        student.setLastName(dto.getLastName());
        student.setDateOfBirth(dto.getDateOfBirth());
        student.setGender(dto.getGender());
        student.setEmail(dto.getEmail());
        student.setPhoneNumber(dto.getPhoneNumber());
        student.setGradeLevel(dto.getGradeLevel());
        student.setEnrollmentDate(dto.getEnrollmentDate());
        student.setAddress(dto.getAddress() != null ? dto.getAddress().toEntity() : null);

        if (dto.getGuardian() != null) {
            // Assuming GuardianMapper exists to convert GuardianRequestDTO to Guardian
            student.setGuardian(GuardianMapper.toEntity(dto.getGuardian()));
        }
        if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
            student.setImageFileName(dto.getImageFile().getOriginalFilename());
            student.setImageContentType(dto.getImageFile().getContentType());
            // Note: imageFilePath would typically be updated after saving the new file
        }
    }

    // Convert List of Students to List of StudentResponseDTOs
    public  List<StudentResponseDTO> toResponseDTOList(List<Student> students) {
        return students.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}