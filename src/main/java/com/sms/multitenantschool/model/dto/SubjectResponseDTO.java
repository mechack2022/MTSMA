package com.sms.multitenantschool.model.dto;

import com.sms.multitenantschool.model.entity.timeTable.ClazzSubjectTeacher;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectResponseDTO {

    private Long id;

    private UUID tenantUuid;

    private String subjectName;

    private String yearLevel;

    private String subjectCode;

    private String description;

    private Integer credits;

    private Boolean isElective;
    // Optional: Include IDs of assigned classes and teachers for reference
    private List<ClazzSubjectTeacher> classAssignments;
}
