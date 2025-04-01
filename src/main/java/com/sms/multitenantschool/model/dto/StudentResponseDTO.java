package com.sms.multitenantschool.model.dto;

import com.sms.multitenantschool.enums.EnrollmentStatus;
import com.sms.multitenantschool.model.entity.Address;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class StudentResponseDTO {
    private Long id;
    private UUID studentUuid;
    private String studentIdNumber;
    private Long tenantId;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String email;
    private String phoneNumber;
    private String gradeLevel;
    private LocalDate enrollmentDate;
    private EnrollmentStatus enrollmentStatus;
    private GuardianRequestDTO guardian;
    private Address address;
    private String imageFileName;
    private String imageContentType;
    private String imageFilePath;
}
