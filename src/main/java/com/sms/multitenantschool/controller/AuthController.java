package com.sms.multitenantschool.controller;

import com.sms.multitenantschool.model.AuthResponseDTO;
import com.sms.multitenantschool.model.DTO.ApiResponse;
import com.sms.multitenantschool.model.DTO.LoginDTO;
import com.sms.multitenantschool.model.DTO.TenantSignUpDTO;
import com.sms.multitenantschool.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value="/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<AuthResponseDTO>> login(@RequestBody LoginDTO loginDto) {
        try{
            String token = authService.login(loginDto);
            AuthResponseDTO authResponseDto = new AuthResponseDTO();
            authResponseDto.setAccessToken(token);
            return ResponseEntity.ok(new ApiResponse<>(
                    authResponseDto,
                    "Login successful"
            ));
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(
                            "Login failed",
                            e.getMessage()
                    ));
        }
    }

    @PostMapping(value="/tenant-signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<AuthResponseDTO>> signup(@RequestBody TenantSignUpDTO tenantSignUpDTO) {
        try{
            String token = authService.createTenant(tenantSignUpDTO);
            AuthResponseDTO authResponseDto = new AuthResponseDTO();
            authResponseDto.setAccessToken(token);
            return ResponseEntity.ok( new ApiResponse<>(authResponseDto,"Tenant Sign up successfully." ));
        }
        catch(Exception e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(
                            "Login failed",
                            e.getMessage()
                    ));
        }

    }

    @GetMapping(value="/verify-email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<String>> verifyEmail(@RequestParam("token") String token) {
        try {
            String response = authService.verifyEmail(token);
            return ResponseEntity.ok(new ApiResponse<>(response, "Mail Verification Success"));
        } catch(Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("Mail Verification Failed", e.getMessage()));
        }
    }

    @GetMapping(value="/testing", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<String>> testingComm() {
        String response = "We are connected";
        return ResponseEntity.ok(new ApiResponse<>(response, "Connection successful"));
    }
}