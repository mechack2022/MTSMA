package com.sms.multitenantschool.model.entity;

import com.sms.multitenantschool.enums.EnrollmentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "students", schema = "public")
@Setter
@Getter
public class Student extends BaseEntity {

    @Column(name = "student_uuid", nullable = false, unique = true, updatable = false)
    private UUID studentUuid;

    @Column(name = "student_id_number", nullable = false, unique = true, updatable = false)
    private String studentIdNumber;

    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "grade_level", nullable = false)
    private String gradeLevel;

    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "enrollment_status", nullable = false)
    private EnrollmentStatus enrollmentStatus = EnrollmentStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "guardian_id", nullable = false)
    private Guardian guardian;

    @Embedded
    private Address address;

    @Column(name = "image_file_name")
    private String imageFileName;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Column(name = "image_file_path")
    private String imageFilePath;

    @Override
    protected void prePersistCustom() {
        if (studentUuid == null) {
            studentUuid = UUID.randomUUID();
        }
    }
}
