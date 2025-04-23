package com.sms.multitenantschool.mapper;
import com.sms.multitenantschool.model.dto.*;
import com.sms.multitenantschool.model.entity.timeTable.ClazzSubjectTeacher;
import com.sms.multitenantschool.model.entity.timeTable.Subject;
import com.sms.multitenantschool.model.entity.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface TeacherMapper {

    // Map TeacherRequestDTO to Teacher entity
    @Mapping(target = "id", ignore = true) // ID is auto-generated
    @Mapping(target = "classAssignments", ignore = true) // Handled separately
    @Mapping(target = "staff", ignore = true) // Requires Staff entity, set in service
    @Mapping(target = "createdAt", ignore = true) // Handled by BaseEntity
    @Mapping(target = "updatedAt", ignore = true) // Handled by BaseEntity
    @Mapping(target = "archived", ignore = true) // Handled by BaseEntity
    Teacher toEntity(TeacherRequestDTO requestDTO);

    // Update existing Teacher entity from TeacherRequestDTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "classAssignments", ignore = true)
    @Mapping(target = "staff", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "archived", ignore = true)
    void updateEntityFromDTO(TeacherRequestDTO requestDTO, @MappingTarget Teacher teacher);

    // Map Teacher entity to TeacherResponseDTO
    @Mapping(source = "id", target = "id")
    @Mapping(source = "tenantUuid", target = "tenantUuid")
    @Mapping(source ="staff.email" , target = "staffEmail")
    @Mapping(source = "qualification", target = "qualification")
    @Mapping(source = "archived",  target = "archived")
    @Mapping(source = "staff.staffUuid", target = "staffUuid", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "subjectTaughtIds", expression = "java(teacher.getClassAssignments() != null ? teacher.getClassAssignments().stream().map(assignment -> assignment.getSubject().getId()).collect(java.util.stream.Collectors.toSet()) : new java.util.HashSet<>())")
    TeacherResponseDTO toResponseDTO(Teacher teacher);

    // Map ClazzSubjectTeacherRequestDTO to ClazzSubjectTeacher entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "clazz", ignore = true)
    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "periodAssignments", ignore = true)
    ClazzSubjectTeacher toClazzSubjectTeacherEntity(ClazzSubjectTeacherRequestDTO requestDTO);

    // Map ClazzSubjectTeacher entity to ClazzSubjectTeacherResponseDTO
    @Mapping(source = "clazz.classUuid", target = "clazzId")
    @Mapping(source = "subject.id", target = "subjectId")
    @Mapping(source = "teacher.id", target = "teacherId")
    @Mapping(target = "periodIds", expression = "java(entity.getPeriodAssignments().stream().map(cstp -> cstp.getPeriod().getPeriodUuid()).toList())")
    ClazzSubjectTeacherResponseDTO toClazzSubjectTeacherResponseDTO(ClazzSubjectTeacher entity);

    // Map SubjectRequestDTO to Subject entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "classAssignments", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "archived", ignore = true)
    Subject toSubjectEntity(SubjectRequestDTO requestDTO);

    // Map Subject entity to SubjectResponseDTO
    @Mapping(source = "id", target = "id")
//    @Mapping(target = "classAssignments", source = "classAssignments", qualifiedByName = "toSummaryDTOList")
    SubjectResponseDTO toSubjectResponseDTO(Subject subject);


}