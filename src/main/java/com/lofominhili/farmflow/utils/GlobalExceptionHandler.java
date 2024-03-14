package com.lofominhili.farmflow.utils;

import com.lofominhili.farmflow.dto.BasicDTO.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleException(Exception e) {
        ErrorDTO errorDTO = new ErrorDTO(HttpStatus.BAD_REQUEST.value(), e.getClass().getSimpleName(), e.getMessage());
        return ResponseEntity.badRequest().body(errorDTO);
    }

    public static String handleValidationResults(BindingResult validationResult) {
        StringBuilder message = new StringBuilder("Validation errors:");
        for (FieldError error : validationResult.getFieldErrors()) {
            message.append(" ").append(error.getField()).append(": ").append(error.getDefaultMessage()).append(";");
        }
        return message.toString();
    }
}
