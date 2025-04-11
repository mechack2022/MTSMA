package com.sms.multitenantschool.model.entity;

import com.sms.multitenantschool.model.entity.timeTable.Subject;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "teachers", schema = "public")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Teacher extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "staff_uuid", referencedColumnName = "staff_uuid", nullable = false)
    private Staff staff;

    @ManyToMany
    @JoinTable(
            name = "teacher_subjects",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "id")
    )
    private List<Subject> subjectsTaught;

    @Column(name = "qualification")
    private String qualification;

    @Column(name = "department")
    private String department;

    @Column(name = "joining_date")
    private LocalDate joiningDate;

    @Column(name = "is_class_teacher", nullable = false)
    private Boolean isClassTeacher;
}
