package com.sms.multitenantschool.model.entity;

import com.sms.multitenantschool.model.dto.ApplicationCodesetDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "base_application_codeset", schema = "public")
@Builder
public class BaseApplicationCodeset extends BaseEntity {

    @Basic
    @Column(name = "codeset_group")
    private String codesetGroup;

    @Basic
    @Column(name = "display")
    private String display;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "version")
    private String version;

    @Basic
    @Column(name = "code", updatable = false)
    private String code;

    @Basic
    @Column(name = "archived") // 1 is archived, 0 is unarchived, 2 is deactivate
    private Integer archived;

    public static BaseApplicationCodeset fromDto(ApplicationCodesetDTO dto) {
        return BaseApplicationCodeset.builder()
                .code(dto.getCode())
                .version(dto.getVersion())
                .codesetGroup(dto.getCodesetGroup())
                .display(dto.getDisplay())
                .description(dto.getDescription())
                .archived(dto.getArchived())
                .build();
    }
}
