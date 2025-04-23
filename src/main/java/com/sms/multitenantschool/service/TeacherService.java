package com.sms.multitenantschool.service;

import com.sms.multitenantschool.model.dto.TeacherRequestDTO;
import com.sms.multitenantschool.model.dto.TeacherResponseDTO;

public interface TeacherService {
    TeacherResponseDTO create(TeacherRequestDTO req);
}
