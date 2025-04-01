package com.sms.multitenantschool.controller;

import com.sms.multitenantschool.model.dto.ApiResponse;
import com.sms.multitenantschool.model.entity.TenantSettings;
import com.sms.multitenantschool.service.TenantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/tenant")
public class TenantController {

    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/add-settings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<TenantSettings>> addTenantSettings(
            @RequestBody TenantSettings settings,
            @RequestParam String tenantUuid) {

        try {
            TenantSettings updatedSettings = tenantService.addTenantSetting(settings, tenantUuid);
            return ResponseEntity.ok(new ApiResponse<>(updatedSettings, "Tenant settings updated successfully."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    null,
                    "Invalid input: " + e.getMessage()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(
                    null,
                    "Failed to update tenant settings: " + e.getMessage()
            ));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/update-settings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<TenantSettings>> updateTenantSettings(
            @RequestParam Long tenantId,
            @RequestBody TenantSettings settings) {

        try {
            TenantSettings updatedSettings = tenantService.updateTenantSettings(tenantId, settings);
            return ResponseEntity.ok(new ApiResponse<>(updatedSettings, "Tenant settings updated successfully."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    null,
                    "Invalid input: " + e.getMessage()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(
                    null,
                    "Failed to update tenant settings: " + e.getMessage()
            ));
        }
    }


}
