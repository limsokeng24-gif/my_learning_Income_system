package com.school_management.overseas_language_centre.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "otp")
@Data
public class OtpProperties {
    private String secretKey;

    /** How long an issued OTP remains valid (minutes). After expiry, verify fails. */
    private int ttlMinutes = 10;

    /** Maximum number of OTP emails allowed per user before temporary lockout. */
    private int maxSendCount = 5;

    /** Minimum wait between consecutive OTP sends for the same user (minutes). */
    private int cooldownMinutes = 5;
}
