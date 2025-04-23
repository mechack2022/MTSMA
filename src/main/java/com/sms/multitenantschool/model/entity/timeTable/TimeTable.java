package com.sms.multitenantschool.model.entity.timeTable;

import com.sms.multitenantschool.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "timetables", schema = "public")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TimeTable extends BaseEntity {

    @Column(name = "timetable_uuid", nullable = false, unique = true, updatable = false)
    private UUID timetableUuid;

    @Column(nullable = false, unique = true)
    private String timetableName;

    @Column(nullable = false)
    private String academicYear;

    @Column(nullable = false)
    private String term;

    @Column(name = "effective_from")
    private LocalDate effectiveFrom;

    @Column(name = "effective_until")
    private LocalDate effectiveUntil;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "timeTable", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TimeTableEntry> entries = new HashSet<>();

    // Methods to manage entries
    public void addEntry(ClazzSubjectTeacherPeriod cstPeriod, String notes) {
        TimeTableEntry entry = new TimeTableEntry(this, cstPeriod, notes);
        entries.add(entry);
    }

    public void removeEntry(TimeTableEntry entry) {
        if (entries.contains(entry)) {
            entries.remove(entry);
            entry.setTimeTable(null);
        }
    }
}

