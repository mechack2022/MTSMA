package com.sms.multitenantschool.model.dto;

import com.sms.multitenantschool.model.entity.BaseApplicationCodeset;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationCodesetDTO {
    private Long id;
    private String code;
    private String version;
    private String codesetGroup;
    private String display;
    private String description;
    private Integer archived;

    public static ApplicationCodesetDTO fromEntity(BaseApplicationCodeset entity) {
        return ApplicationCodesetDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .version(entity.getVersion())
                .codesetGroup(entity.getCodesetGroup())
                .display(entity.getDisplay())
                .description(entity.getDescription())
                .archived(entity.getArchived())
                .build();
    }
}