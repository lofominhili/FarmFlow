package com.lofominhili.farmflow.services.JwtService;

import com.lofominhili.farmflow.exceptions.TokenValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service implementation of {@link JwtService} for JWT (JSON Web Token) operations.
 * This service provides methods for generating, extracting, and validating JWT tokens.
 * This service requires configuration properties for the secret key and token lifetime
 * to be injected via Spring's {@code @Value} annotation.
 *
 * @author daniel
 */
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";

    @Value("${jwt.secret_key}")
    private String secretKey;
    @Value("${jwt.access_token.lifetime}")
    private Integer tokenLifetime;

    /**
     * Retrieves the JWT token from the HTTP request.
     * This method extracts the token from the {@code Authorization} header in the request.
     *
     * @param request The {@link HttpServletRequest} object representing the incoming HTTP request.
     * @return The JWT token extracted from the {@code Authorization} header, or {@code null} if not present or invalid.
     */
    @Nullable
    @Override
    public String getToken(HttpServletRequest request) {
        String header = request.getHeader(HEADER_NAME);
        if (header == null || !header.startsWith(BEARER_PREFIX)) return null;
        return header.split(" ")[1].trim();
    }

    /**
     * Extracts the email from the JWT token.
     * This method extracts the subject (email) claim from the JWT token.
     *
     * @param token The JWT token from which to extract the email.
     * @return The email address extracted from the JWT token.
     */
    @Override
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a claim from the JWT token using a claims resolver function.
     *
     * @param token          The JWT token from which to extract the claim.
     * @param claimsResolver A function to resolve the desired claim from the JWT claims.
     * @param <T>            The type of the claim to be extracted.
     * @return The extracted claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generates a JWT token for the specified UserDetails.
     *
     * @param userDetails The {@link UserDetails} object representing the user for whom the token is generated.
     * @return The generated JWT token.
     */
    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generates a JWT token for the specified UserDetails with additional claims.
     *
     * @param extraClaims Additional claims to be included in the JWT token.
     * @param userDetails The {@link UserDetails} object representing the user for whom the token is generated.
     * @return The generated JWT token.
     */
    @Override
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + tokenLifetime))
                .signWith(getSignInKey())
                .compact();
    }

    /**
     * Validates whether a JWT token is valid for the specified UserDetails.
     *
     * @param token       The JWT token to validate.
     * @param userDetails The {@link UserDetails} object representing the user against which the token is validated.
     * @return {@code true} if the token is valid for the specified user, {@code false} otherwise.
     * @throws TokenValidationException If an error occurs during token validation.
     */
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) throws TokenValidationException {
        try {
            final String username = extractEmail(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            throw new TokenValidationException(e.getMessage());
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean isTokenExpired(String token) {
        Date expirationDate = Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
        return expirationDate.before(new Date());
    }
}