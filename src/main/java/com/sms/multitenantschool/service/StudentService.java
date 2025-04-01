package com.sms.multitenantschool.service;

import com.sms.multitenantschool.model.dto.StudentRequestDTO;
import com.sms.multitenantschool.model.dto.StudentResponseDTO;
import jakarta.transaction.Transactional;

public interface StudentService {


    @Transactional
    StudentResponseDTO create(StudentRequestDTO request);
}
