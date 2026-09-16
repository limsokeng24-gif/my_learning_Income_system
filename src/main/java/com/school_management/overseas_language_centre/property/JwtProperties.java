package com.school_management.overseas_language_centre.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration // register as a Spring bean
@ConfigurationProperties(prefix = "jwt") //map jwt.secret, jwt.expiration-ms
@Data //getter //setter //binding
public class JwtProperties {
    /* HMAC signing key - must match between sign and verify. */
    private String secret; // secret this one is from application.yml

    /* Access-token lifetime in milliseconds (default 24 hour).*/
    private long expirationMs = 86400000L; //24hour //expiration is from application.yml

    /* Refresh token use for */
    private long refreshExpirationMs;
}
