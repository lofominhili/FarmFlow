package com.lofominhili.farmflow.utils;

import com.lofominhili.farmflow.dto.BasicDTO.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for handling exceptions across all controllers.
 * This class is annotated with {@link ControllerAdvice} to define global exception handling.
 *
 * @author daniel
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Exception handler method for handling all types of exceptions.
     * It returns a ResponseEntity containing an ErrorDTO with details about the occurred exception.
     *
     * @param e The exception to handle.
     * @return A ResponseEntity containing an ErrorDTO with error details.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleException(Exception e) {
        ErrorDTO errorDTO = new ErrorDTO(HttpStatus.BAD_REQUEST.value(), e.getClass().getSimpleName(), e.getMessage());
        return ResponseEntity.badRequest().body(errorDTO);
    }

    /**
     * Utility method for handling validation errors.
     * It constructs an error message string based on the validation results.
     *
     * @param validationResult The validation result object containing validation errors.
     * @return A string containing formatted validation error messages.
     */
    public static String handleValidationResults(BindingResult validationResult) {
        StringBuilder message = new StringBuilder("Validation errors:");
        for (FieldError error : validationResult.getFieldErrors()) {
            message.append(" ").append(error.getField()).append(": ").append(error.getDefaultMessage()).append(";");
        }
        return message.toString();
    }
}