package com.sms.multitenantschool.model.entity;

import com.sms.multitenantschool.model.entity.timeTable.ClazzSubjectTeacher;
import com.sms.multitenantschool.model.entity.timeTable.Subject;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "teachers", schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_teachers_id_tenant",
                        columnNames = {"teacher_id", "tenant_uuid"}),
                @UniqueConstraint(name = "uk_teachers_staff_tenant",
                        columnNames = {"staff_uuid", "tenant_uuid"})
        })
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Teacher extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "staff_uuid", referencedColumnName = "staff_uuid", nullable = false)
    private Staff staff;

    @Column(name = "teacher_id", nullable = false)
    private String teacherId;

    @Column(name = "tenant_uuid", nullable = false)
    private UUID tenantUuid;

    @Column(name = "qualification")
    private String qualification;

    @Column(name = "joining_date")
    private LocalDate joiningDate;

    @Column(name = "is_class_teacher", nullable = false)
    private Boolean isClassTeacher;

    @ManyToMany
    @JoinTable(
            name = "teacher_subjects",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private List<Subject> subjects = new ArrayList<>();
//
//    @OneToMany(mappedBy = "teacher")
//    private List<ClazzSubjectTeacher> classAssignments = new ArrayList<>();
//
//    public void assignToClassSubject(ClazzSubjectTeacher assignment) {
//        if (classAssignments == null) classAssignments = new ArrayList<>();
//        classAssignments.add(assignment);
//    }

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClazzSubjectTeacher> classAssignments = new ArrayList<>();

    public void assignToClassSubject(ClazzSubjectTeacher assignment) {
        if (this.classAssignments == null) {
            this.classAssignments = new ArrayList<>();
        }
        this.classAssignments.add(assignment);
        assignment.setTeacher(this);
    }

}
