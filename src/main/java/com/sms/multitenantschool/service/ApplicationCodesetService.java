package com.sms.multitenantschool.service;

import com.sms.multitenantschool.model.dto.ApplicationCodesetDTO;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ApplicationCodesetService {
    @Transactional
    List<ApplicationCodesetDTO> readFileData(MultipartFile file, String fileType) throws IOException;

    @Transactional
    List<ApplicationCodesetDTO> saveCodesets(List<ApplicationCodesetDTO> codesetDTOList);
}
