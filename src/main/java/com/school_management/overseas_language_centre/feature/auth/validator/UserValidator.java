package com.school_management.overseas_language_centre.feature.auth.validator;

import com.school_management.overseas_language_centre.entity.User;
import com.school_management.overseas_language_centre.feature.core.user.repository.UserRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 8 digits Update,Lower  digits, Symble
    // Admin@123
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$");

    private static final int PASSWORD_MIN = 8;
    private static final int PASSWORD_MAX = 100;
    /** At least one lower, upper, digit, and special character (after trim). */
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{8,}$");

    public User validateLoginCredentials(String username, String rawPassword) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            throw new ValidationException("Invalid credentials");
        }
        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new ValidationException("Account is disabled");
        }
        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new ValidationException("Invalid credentials");
        }
        return user;
    }
}
