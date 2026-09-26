package com.school_management.overseas_language_centre.feature.core.otp.component;

import org.springframework.stereotype.Component;
import java.security.SecureRandom;

/**
 * Generates a 6-digit numeric OTP (100000–999999) for the forgot-password flow.
 * Uses {@link SecureRandom} — a predictable code would defeat the whole mechanism.
 */
@Component
public class OtpGenerator {
    // Cryptographically strong RNG — preferred over java.util.Random for security codes
    private final SecureRandom random = new SecureRandom();

    /**
     * Random 6-digit code in range [100000, 999999].
     * The +100000 offset guarantees no leading zero is lost when stringified.
     */
    public String generate() {
        // nextInt(900000) → [0, 899999]; +100000 → [100000, 999999]
        return String.valueOf(random.nextInt(900000) + 100000);
    }
}
