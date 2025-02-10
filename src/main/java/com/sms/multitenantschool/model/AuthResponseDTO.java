package com.sms.multitenantschool.model;

import lombok.Data;

@Data
public class AuthResponseDTO {

    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}


