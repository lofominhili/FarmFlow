package com.lofominhili.farmflow.controllers;

import com.lofominhili.farmflow.dto.BasicDTO.SuccessDTO;
import com.lofominhili.farmflow.dto.RequestDTO.SignInRequest;
import com.lofominhili.farmflow.dto.UserDTO;
import com.lofominhili.farmflow.exceptions.AuthenticationFailedException;
import com.lofominhili.farmflow.exceptions.RequestDataValidationFailedException;
import com.lofominhili.farmflow.services.AuthService.AuthService;
import com.lofominhili.farmflow.utils.GlobalExceptionHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registrate-user")
    public ResponseEntity<SuccessDTO<String>> registrateUser(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult validationResult
    ) throws RequestDataValidationFailedException, AuthenticationFailedException {
        if (validationResult.hasErrors()) {
            throw new RequestDataValidationFailedException(GlobalExceptionHandler.handleValidationResults(validationResult));
        }
        authService.registrateUser(userDTO);
        return new ResponseEntity<>(
                new SuccessDTO<>(
                        HttpStatus.CREATED.value(),
                        "Sign Up",
                        "Successfully registrated!"
                ), HttpStatus.CREATED);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SuccessDTO<String>> signIn(
            @Valid @RequestBody SignInRequest request,
            BindingResult validationResult
    ) throws RequestDataValidationFailedException, AuthenticationFailedException {
        if (validationResult.hasErrors()) {
            throw new RequestDataValidationFailedException(GlobalExceptionHandler.handleValidationResults(validationResult));
        }
        return new ResponseEntity<>(
                new SuccessDTO<>(
                        HttpStatus.OK.value(),
                        "Sign In",
                        String.format("Successfully signed in! Use this token for further operations: %s", authService.signIn(request))
                ), HttpStatus.OK);
    }

}
