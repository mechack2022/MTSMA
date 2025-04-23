package com.sms.multitenantschool.model.entity.timeTable;

import com.sms.multitenantschool.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "timetable_entries", schema = "public",
        uniqueConstraints = @UniqueConstraint(columnNames = {"timetable_id", "clazz_subject_teacher_period_id"}))
@Builder
@NoArgsConstructor
@Setter
@Getter
public class TimeTableEntry extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "timetable_id", nullable = false)
    private TimeTable timeTable;

    @ManyToOne
    @JoinColumn(name = "clazz_subject_teacher_period_id", nullable = false)
    private ClazzSubjectTeacherPeriod clazzSubjectTeacherPeriod;

    @Column(name = "notes")
    private String notes;

    public TimeTableEntry(TimeTable timeTable, ClazzSubjectTeacherPeriod cstPeriod, String notes) {
        this.timeTable = timeTable;
        this.clazzSubjectTeacherPeriod = cstPeriod;
        this.notes = notes;
    }
}
