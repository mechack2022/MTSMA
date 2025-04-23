package com.sms.multitenantschool.model.entity.timeTable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sms.multitenantschool.model.entity.BaseEntity;
import com.sms.multitenantschool.model.entity.Teacher;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Table(name = "clazz_subject_teachers", schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_clazz_subject_teacher",
                        columnNames = {"tenant_uuid", "clazz_id", "subject_id", "teacher_id"})
        })
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ClazzSubjectTeacher extends BaseEntity {

    @Column(name = "tenant_uuid", nullable = false)
    private UUID tenantUuid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "clazz_id")
    private Clazz clazz;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @JsonIgnore
    @OneToMany(mappedBy = "clazzSubjectTeacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ClazzSubjectTeacherPeriod> periodAssignments = new HashSet<>();

    public void addPeriod(Period period) {
        ClazzSubjectTeacherPeriod cstPeriod = new ClazzSubjectTeacherPeriod(this, period);
        periodAssignments.add(cstPeriod);
    }


}



