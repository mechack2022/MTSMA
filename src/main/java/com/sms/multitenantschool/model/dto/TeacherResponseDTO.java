package com.sms.multitenantschool.model.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherResponseDTO {

    private Long id;

    private UUID staffUuid;

    private String staffEmail;

    private UUID tenantUuid;

    private String qualification;

    private LocalDate joiningDate;

    private Boolean isClassTeacher;

    private Set<Long> subjectTaughtIds;

    private int archived;
}
