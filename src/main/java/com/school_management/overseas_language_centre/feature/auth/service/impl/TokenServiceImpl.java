package com.school_management.overseas_language_centre.feature.auth.service.impl;

import com.school_management.overseas_language_centre.feature.auth.dto.response.AuthResponse;
import com.school_management.overseas_language_centre.feature.auth.service.TokenService;
import com.school_management.overseas_language_centre.feature.core.user.dto.response.UserResponse;
import com.school_management.overseas_language_centre.feature.intergration.redis.RedisService;
import com.school_management.overseas_language_centre.security.JwtService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final JwtService jwtService;
    private final RedisService redisService;
    public static final String TOKEN_KEY_PREFIX = "token:"; //use for name prefix token is redis in vscode
    @Override
    public AuthResponse issue(UserResponse user) {

        String token = jwtService.generateToken(user.getUsername());
        redisService.save(
                TOKEN_KEY_PREFIX + user.getUsername(), //this one use for redis get token: username
                token,
                jwtService.getExpirationDuration()
        );

        return AuthResponse.of(token, user);
    }
}
