package com.sms.multitenantschool.model.entity.timeTable;

import com.sms.multitenantschool.model.entity.BaseEntity;
import com.sms.multitenantschool.model.entity.Teacher;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "timetable_entries", schema = "public")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TimeTableEntry extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private Clazz classEntity;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private Period period;

    @Column(nullable = false)
    private String day; // e.g., "Monday"

    @Column(nullable = false)
    private String schoolYear; // e.g., "2025-2026"

    @Column(nullable = false)
    private String term; // e.g., "Term 1"
}
