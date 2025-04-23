package com.sms.multitenantschool.mapper;


import com.sms.multitenantschool.model.dto.SubjectRequestDTO;
import com.sms.multitenantschool.model.dto.SubjectResponseDTO;
import com.sms.multitenantschool.model.entity.timeTable.Subject;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface SubjectMapper {

    // Map SubjectRequestDTO to Subject entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "classAssignments", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "archived", ignore = true)
    @Mapping(target = "tenantUuid", ignore = true)
    Subject toEntity(SubjectRequestDTO requestDTO);

    // Update existing Subject entity from SubjectRequestDTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "classAssignments", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "archived", ignore = true)
    @Mapping(target = "tenantUuid", ignore = true)
    void updateEntityFromDTO(SubjectRequestDTO requestDTO, @MappingTarget Subject subject);

    // Map Subject entity to SubjectResponseDTO
    @Mapping(source = "id", target = "id")
    SubjectResponseDTO toResponseDTO(Subject subject);

}