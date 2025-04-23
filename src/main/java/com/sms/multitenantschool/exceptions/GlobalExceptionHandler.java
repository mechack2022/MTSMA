package com.sms.multitenantschool.exceptions;

import com.sms.multitenantschool.model.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Mapping of constraint names to user-friendly error messages
    private static final Map<String, String> CONSTRAINT_MESSAGES = new HashMap<>();

    static {
        CONSTRAINT_MESSAGES.put("uk_subjects_name_year_tenant",
                "A subject with the provided name and year level already exists for this tenant");
        CONSTRAINT_MESSAGES.put("ukb26p2tc3all9n775wst7v7t81",
                "A subject with the provided name and year level already exists for this tenant");
        CONSTRAINT_MESSAGES.put("subjects_subject_code_key",
                "Subject code is already in use for this tenant");
        CONSTRAINT_MESSAGES.put("uk_teacher_teachers_id_tenant", "Teacher ID is already in use for this tenant");
        CONSTRAINT_MESSAGES.put("uk_teachers_staff_email_tenant", "Email is already in use for this tenant");
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(Exception ex) {
        logger.error("Unhandled exception: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ApiResponse<>(
                        "An error occurred",
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        logger.error("Resource Not Found Exception: {}", ex.getMessage(), ex);
        String message = ex.getMessage();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ApiResponse<>(
                        message,
                        String.format("The requested %s resource was not found", ex.getResourceName().toLowerCase())
                ));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    @ResponseBody
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(Exception ex) {
        logger.error("MethodArgumentNotValid Exception: {}", ex.getMessage(), ex);
        Map<String, String> validationErrors = new HashMap<>();
        if (ex instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
            methodArgumentNotValidException.getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String message = error.getDefaultMessage();
                validationErrors.put(fieldName, message);
            });
        } else if (ex instanceof jakarta.validation.ConstraintViolationException constraintViolationException) {
            constraintViolationException.getConstraintViolations().forEach(violation -> {
                String fieldName = violation.getPropertyPath().toString();
                String message = violation.getMessage();
                validationErrors.put(fieldName, message);
            });
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ApiResponse<>(
                        validationErrors,
                        "Validation failed",
                        "Invalid input provided"
                ));
    }


    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Object>> handleBadRequestException(BadRequestException ex) {
        logger.error("Bad Request Exception: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ApiResponse<>(
                        ex.getMessage(),
                        String.format("Invalid value provided for %s", ex.getField())
                ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        logger.error("Data integrity violation: {}", ex.getMessage(), ex);
        String message = "Failed to create resource due to a data integrity issue";
        String details = message;

        if (ex.getCause() instanceof ConstraintViolationException constraintEx) {
            String constraintName = constraintEx.getConstraintName();
            if (constraintName != null) {
                details = CONSTRAINT_MESSAGES.getOrDefault(constraintName,
                        "A resource with the provided unique fields already exists");
                message = details;
            }
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ApiResponse<>(
                        message,
                        details
                ));
    }

//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
//        log.error("Data integrity violation: {}", ex.getMessage(), ex);
//        String message = "Failed to create resource due to a data integrity issue";
//        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException constraintEx) {
//            String constraintName = constraintEx.getConstraintName();
//            message = CONSTRAINT_MESSAGES.getOrDefault(constraintName, message);
//        }
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body(new ApiResponse<>(message, ex.getMessage()));
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
//        log.error("Unexpected error: {}", ex.getMessage(), ex);
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(new ApiResponse<>("An unexpected error occurred", ex.getMessage()));
//    }
}