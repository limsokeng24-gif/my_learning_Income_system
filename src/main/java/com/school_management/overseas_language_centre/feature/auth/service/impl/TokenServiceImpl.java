package com.school_management.overseas_language_centre.feature.auth.service.impl;

import com.school_management.overseas_language_centre.entity.User;
import com.school_management.overseas_language_centre.feature.auth.dto.response.AuthResponse;
import com.school_management.overseas_language_centre.feature.auth.service.TokenService;
import com.school_management.overseas_language_centre.feature.core.user.dto.response.UserResponse;
import com.school_management.overseas_language_centre.feature.core.user.mapper.UserMapper;
import com.school_management.overseas_language_centre.feature.core.user.repository.UserRepository;
import com.school_management.overseas_language_centre.feature.intergration.redis.RedisService;
import com.school_management.overseas_language_centre.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final JwtService jwtService;
    private final RedisService redisService;
    public static final String TOKEN_KEY_PREFIX = "token:"; //use for name prefix token is redis in vscode
    public static final String REFRESH_TOKEN_KEY_PREFIX = "refresh_token:";

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Override
    public AuthResponse issue(UserResponse user) {
        // Generate access token
        String accessToken =
                jwtService.generateToken(user.getUsername());

        // Generate refresh token
        String refreshToken =
                jwtService.generateRefreshToken(user.getUsername());

        // Save access token in Redis
        redisService.save(
                TOKEN_KEY_PREFIX + user.getUsername(),
                accessToken,
                jwtService.getExpirationDuration()
        );

        // Save refresh token in Redis
//        String token = jwtService.generateToken(user.getUsername());
//        redisService.save(
//                TOKEN_KEY_PREFIX + user.getUsername(), //this one use for redis get token: username
//                token,
//                jwtService.getExpirationDuration()
//        );


        return AuthResponse.of(
                accessToken,
                refreshToken,
                user
        );
    }

    @Override
    public AuthResponse refresh(String refreshToken) {
        // 1. Validate refresh token
        if (!jwtService.validateRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        // 2. Get username from refresh token
        String username = jwtService.getUsernameFromToken(refreshToken);

        // 3. Find user from database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 4. Generate new access token
        String newAccessToken = jwtService.generateToken(username);

        // 5. Save new access token to Redis
        redisService.save(
                TOKEN_KEY_PREFIX + username,
                newAccessToken,
                jwtService.getExpirationDuration()
        );

        // 6. Return user information
        return AuthResponse.of(
                newAccessToken,
                refreshToken,
                userMapper.toResponse(user)
        );
    }
}
