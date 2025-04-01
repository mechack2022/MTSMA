package com.sms.multitenantschool.model.dto;
import com.sms.multitenantschool.model.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

import jakarta.validation.constraints.*;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudentRequestDTO {
    @NotBlank(message = "Student ID Number is mandatory")
    private String firstName;
    private String middleName;
    @NotBlank(message = "Last Name is mandatory")
    private String lastName;
    @NotNull(message = "Date of Birth is mandatory")
    private LocalDate dateOfBirth;
    @NotBlank(message = "Gender is mandatory")
    private String gender;
    @Email(message = "Invalid email format")
    private String email;
    private String phoneNumber;
    @NotBlank(message = "Grade Level is mandatory")
    private String gradeLevel;
    @NotNull(message = "Enrollment Date is mandatory")
    private LocalDate enrollmentDate;
    private AddressRequestDTO address;
    @NotNull(message = "Guardian ID is mandatory")
    private GuardianRequestDTO guardian;
    private MultipartFile imageFile;
}