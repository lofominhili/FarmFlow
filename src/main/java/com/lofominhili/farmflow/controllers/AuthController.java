package com.lofominhili.farmflow.controllers;

import com.lofominhili.farmflow.dto.BasicDTO.SuccessDTO;
import com.lofominhili.farmflow.dto.EntityDTO.UserDTO;
import com.lofominhili.farmflow.dto.RequestDTO.SignInRequest;
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

/**
 * REST controller for handling authentication operations.
 * This controller provides endpoints for user registration and user sign-in.
 * <p>
 * This controller is mapped to "/api/auth" base path.
 * It requires an instance of {@link AuthService} to be injected via constructor.
 *
 * @author daniel
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint for registering a new user.
     * This method validates the incoming {@link UserDTO} using {@link Valid} annotation.
     * If validation fails, it throws a {@link RequestDataValidationFailedException}.
     * Otherwise, it delegates the registration operation to {@link AuthService#registerUser(UserDTO)}.
     *
     * @param userDTO          The {@link UserDTO} containing information about the user to be registered.
     * @param validationResult The result of validation performed by Spring's {@link BindingResult}.
     * @return A {@link ResponseEntity} containing a success message if the registration operation is successful.
     * @throws RequestDataValidationFailedException If the incoming data fails validation.
     * @throws AuthenticationFailedException        If the registration operation fails.
     */
    @PostMapping("/register-user")
    public ResponseEntity<SuccessDTO<String>> registerUser(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult validationResult
    ) throws RequestDataValidationFailedException, AuthenticationFailedException {
        if (validationResult.hasErrors()) {
            throw new RequestDataValidationFailedException(GlobalExceptionHandler.handleValidationResults(validationResult));
        }
        authService.registerUser(userDTO);
        return new ResponseEntity<>(
                new SuccessDTO<>(
                        HttpStatus.CREATED.value(),
                        "register",
                        "Successfully registered!"
                ), HttpStatus.CREATED);
    }

    /**
     * Endpoint for user sign-in.
     * This method validates the incoming {@link SignInRequest} using {@link Valid} annotation.
     * If validation fails, it throws a {@link RequestDataValidationFailedException}.
     * Otherwise, it delegates the sign-in operation to {@link AuthService#signIn(SignInRequest)}.
     *
     * @param request          The {@link SignInRequest} containing information about the user's email and password.
     * @param validationResult The result of validation performed by Spring's {@link BindingResult}.
     * @return A {@link ResponseEntity} containing a success message with a token if the sign-in operation is successful.
     * @throws RequestDataValidationFailedException If the incoming data fails validation.
     * @throws AuthenticationFailedException        If the sign-in operation fails.
     */
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
