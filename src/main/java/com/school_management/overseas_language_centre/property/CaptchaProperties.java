package com.school_management.overseas_language_centre.property;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration // register as a Spring bean
@ConfigurationProperties(prefix = "captcha") // maps captcha.enabled, captcha.length, …
@Data
public class CaptchaProperties {
    /** When false, generate returns enabled=false and validate is a no-op. */
    private boolean enabled;

    /** Number of characters in the captcha code (RandomCodeGenerator enforces min 3). */
    private int length;

    /** How long the Redis entry lives, in minutes. */
    private int ttlMinutes;

    /**
     * Dev/test-only master code that always passes validation.
     * Leave empty in production.
     */
    private String masterCode;
}
