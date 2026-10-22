package com.school_management.overseas_language_centre.feature.auth.service.impl;

import com.school_management.overseas_language_centre.entity.User;
import com.school_management.overseas_language_centre.feature.auth.dto.request.LoginRequest;
import com.school_management.overseas_language_centre.feature.auth.dto.request.RefreshTokenRequest;
import com.school_management.overseas_language_centre.feature.auth.dto.response.AuthResponse;
import com.school_management.overseas_language_centre.feature.auth.service.AuthService;
import com.school_management.overseas_language_centre.feature.auth.service.TokenService;
import com.school_management.overseas_language_centre.feature.auth.validator.AuthValidator;
import com.school_management.overseas_language_centre.feature.core.user.mapper.UserMapper;
import com.school_management.overseas_language_centre.feature.intergration.captcha.service.CaptchaService;
import com.school_management.overseas_language_centre.feature.intergration.redis.RedisService;
import com.school_management.overseas_language_centre.property.CaptchaProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthValidator authValidator;
    private final TokenService tokenService;
    private final UserMapper userMapper;
    private final CaptchaService captchaService;
    private final CaptchaProperties captchaProperties;

    @Override
    public AuthResponse login(LoginRequest request) {
        // 1. Verify CAPTCHA first
        if (captchaProperties.isEnabled()) {
            captchaService.validate(
                    request.getCaptchaId(),
                    request.getCaptchaData()
            );
        }

        User user = authValidator.validateLoginCredentials(request.getUsername(), request.getPassword());

        return tokenService.issue(userMapper.toResponse(user));
    }

    @Override
    public AuthResponse refresh(RefreshTokenRequest request) {
        return tokenService.refresh(request.getRefreshToken());
    }


}
