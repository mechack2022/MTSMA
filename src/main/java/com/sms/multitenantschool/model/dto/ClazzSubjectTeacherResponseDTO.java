package com.sms.multitenantschool.model.dto;

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
public class ClazzSubjectTeacherResponseDTO {

    private Long id;

    private UUID clazzId;

    private Long subjectId;

    private Long teacherId;

    private List<UUID> periodIds;
}
