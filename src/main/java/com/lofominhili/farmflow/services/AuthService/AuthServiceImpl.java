package com.lofominhili.farmflow.services.AuthService;


import com.lofominhili.farmflow.dto.RequestDTO.SignInRequest;
import com.lofominhili.farmflow.dto.UserDTO;
import com.lofominhili.farmflow.entities.UserEntity;
import com.lofominhili.farmflow.exceptions.AuthenticationFailedException;
import com.lofominhili.farmflow.mappers.UserMapper;
import com.lofominhili.farmflow.repository.HarvestRateRepository;
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

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final HarvestRateRepository harvestRateRepository;

    @Override
    public void registrateUser(UserDTO request) throws AuthenticationFailedException {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new AuthenticationFailedException("User with prompted credentials already exists");
        }
        UserEntity user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);
    }

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
