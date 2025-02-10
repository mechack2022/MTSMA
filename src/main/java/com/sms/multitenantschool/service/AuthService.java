package com.sms.multitenantschool.service;

import com.sms.multitenantschool.model.DTO.LoginDTO;

public interface AuthService {
    String login(LoginDTO loginDto);
}
