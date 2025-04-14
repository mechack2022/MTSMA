package com.sms.multitenantschool.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sms.multitenantschool.model.entity.timeTable.ClazzSubjectTeacher;
import com.sms.multitenantschool.model.entity.timeTable.Subject;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

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

    @Column(name = "tenant_uuid", nullable = false)
    private UUID tenantUuid;

    @Column(name = "qualification")
    private String qualification;

    @Column(name = "joining_date")
    private LocalDate joiningDate;

    @Column(name = "is_class_teacher", nullable = false)
    private Boolean isClassTeacher;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private List<ClazzSubjectTeacher> teachingAssignments = new ArrayList<>();

    public void assignToClassSubject(ClazzSubjectTeacher assignment) {
        if (teachingAssignments == null) teachingAssignments = new ArrayList<>();
        teachingAssignments.add(assignment);
    }

}
