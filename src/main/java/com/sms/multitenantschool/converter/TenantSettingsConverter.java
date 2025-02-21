package com.sms.multitenantschool.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.sms.multitenantschool.model.entity.TenantSettings;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.IOException;

@Converter
public class TenantSettingsConverter implements AttributeConverter<TenantSettings, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(TenantSettings settings) {
        if (settings == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(settings);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting TenantSettings to JSON", e);
        }
    }

    @Override
    public TenantSettings convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(dbData, TenantSettings.class);
        } catch (IOException e) {
            throw new RuntimeException("Error converting JSON to TenantSettings", e);
        }
    }
}