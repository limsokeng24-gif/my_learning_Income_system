package com.school_management.overseas_language_centre.property;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "encryption")
@Data
public class EncryptionProperties {
    /**
     * Base64-encoded 32-byte AES-256 key.
     * Generate with: {@code openssl rand -base64 32}
     */
    private String secretKey;
}
