package com.sms.multitenantschool.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClazzSubjectTeacherRequestDTO {

    @NotNull(message = "Class UUID is required")
    private UUID clazzId;

    @NotNull(message = "Subject ID is required")
    private Long subjectId;
    // Optional: Include period IDs if assigning periods at the same time
    private List<UUID> periodIds;
}
