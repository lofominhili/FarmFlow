package com.lofominhili.farmflow.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lofominhili.farmflow.dto.BasicDTO.ErrorDTO;
import com.lofominhili.farmflow.repository.UserRepository;
import com.lofominhili.farmflow.utils.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableJpaAuditing
@EnableScheduling
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    protected static final String[] ENDPOINTS_WHITELIST = {
            "/api/auth/sign-in"
    };

    protected static final String[] ENDPOINTS_ADMIN = {
            "/api/admin/*",
            "/api/auth/register-user",
            "/api/product/register-product"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JWTAuthenticationFilter jwtAuthFilter,
            JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            AuthenticationProvider authenticationProvider,
            ObjectMapper objectMapper
    ) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(ENDPOINTS_WHITELIST).permitAll();
                    auth.requestMatchers(ENDPOINTS_ADMIN).hasAuthority(Role.ADMIN.toString());
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .exceptionHandling(exception -> {
                    exception.authenticationEntryPoint(jwtAuthenticationEntryPoint);
                    exception.accessDeniedHandler((request, response, e) -> {
                        response.setStatus(HttpStatus.FORBIDDEN.value());
                        response.setContentType(MediaType.APPLICATION_JSON.toString());
                        ErrorDTO errorDTO = new ErrorDTO(HttpStatus.FORBIDDEN.value(), e.getClass().getSimpleName(), "You dont have admin rights!");
                        response.getOutputStream().write(objectMapper.writeValueAsBytes(errorDTO));
                    });
                })
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return email -> userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserRepository userRepository) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService(userRepository));
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityContextLogoutHandler securityContextLogoutHandler() {
        return new SecurityContextLogoutHandler();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserRepository userRepository) {
        return new ProviderManager(authenticationProvider(userRepository));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
