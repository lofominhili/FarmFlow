package com.lofominhili.farmflow.utils;

import com.lofominhili.farmflow.entities.UserEntity;
import com.lofominhili.farmflow.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Component responsible for creating an initial admin user.
 *
 * @author daniel
 */
@Component
@AllArgsConstructor
public class AdminCreator {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Method executed after the bean has been instantiated and all dependencies have been injected.
     * It creates an initial admin user if it does not already exist in the user repository.
     */
    @PostConstruct
    public void createAdmin() {
        UserEntity user = new UserEntity();
        user.setRating(null);
        user.setFired(false);
        user.setPassword(passwordEncoder.encode("12345678"));
        user.setEmail("admin@gmail.com");
        user.setRole(Role.ADMIN);
        user.setName("Admin");
        user.setSurname("Adminov");
        user.setPatronymic("Adminovich");
        if (userRepository.findByEmail(user.getEmail()).isEmpty())
            userRepository.save(user);
    }
}
