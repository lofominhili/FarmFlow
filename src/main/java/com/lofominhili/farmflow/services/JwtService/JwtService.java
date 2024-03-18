package com.lofominhili.farmflow.services.JwtService;

import com.lofominhili.farmflow.exceptions.TokenValidationException;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {
    String extractEmail(String token);

    String generateToken(UserDetails userDetails);

    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails) throws TokenValidationException;

    @Nullable
    String getToken(HttpServletRequest request);
}
