package com.lofominhili.farmflow.services.AuthService;


import com.lofominhili.farmflow.dto.EntityDTO.UserDTO;
import com.lofominhili.farmflow.dto.RequestDTO.SignInRequest;
import com.lofominhili.farmflow.entities.UserEntity;
import com.lofominhili.farmflow.exceptions.AuthenticationFailedException;
import com.lofominhili.farmflow.mappers.UserMapper;
import com.lofominhili.farmflow.repository.UserRepository;
import com.lofominhili.farmflow.services.JwtService.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service implementation of {@link AuthService} for authentication-related operations.
 * This service provides methods for user registration and user sign-in.
 * <p>
 * This service requires instances of {@link UserRepository}, {@link PasswordEncoder}, {@link JwtService},
 * {@link UserMapper}, and {@link AuthenticationManager} to be injected via constructor.
 *
 * @author daniel
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new user with the provided user information.
     * This method checks if a user with the given email already exists in the repository.
     * If not, the user information is converted to a {@link UserEntity}, the password is encoded,
     * and the user entity is saved in the repository.
     *
     * @param request The {@link UserDTO} containing information about the user to be registered.
     *                It should include the user's email, password, and any other relevant information.
     * @throws AuthenticationFailedException If a user with the provided email already exists in the repository.
     *                                       This exception indicates a registration failure due to duplicate user credentials.
     */
    @Override
    public void registerUser(UserDTO request) throws AuthenticationFailedException {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new AuthenticationFailedException("User with prompted credentials already exists");
        }
        UserEntity user = userMapper.toEntity(request);
        user.setFired(false);
        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);
    }

    /**
     * Authenticates a user with the provided sign-in credentials.
     * This method attempts to authenticate the user using the {@link AuthenticationManager}.
     * If authentication succeeds, a JWT token is generated using the user's principal details.
     *
     * @param credentials The {@link SignInRequest} containing the user's email and password for authentication.
     * @return A JWT token representing the authenticated user session.
     * @throws AuthenticationFailedException If authentication fails due to invalid credentials.
     *                                       This exception indicates a failed sign-in attempt.
     */
    @Override
    public String signIn(SignInRequest credentials) throws AuthenticationFailedException {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(credentials.email(), credentials.password());
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(token);
        } catch (AuthenticationException e) {
            throw new AuthenticationFailedException(e.getMessage());
        }
        return jwtService.generateToken((UserDetails) authentication.getPrincipal());
    }
}