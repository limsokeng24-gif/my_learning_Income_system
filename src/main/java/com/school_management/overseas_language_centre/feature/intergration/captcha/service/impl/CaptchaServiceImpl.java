package com.school_management.overseas_language_centre.feature.intergration.captcha.service.impl;

import com.school_management.overseas_language_centre.feature.intergration.captcha.component.RandomCodeGenerator;
import com.school_management.overseas_language_centre.feature.intergration.captcha.dto.response.CaptchaResponse;
import com.school_management.overseas_language_centre.feature.intergration.captcha.service.CaptchaService;
import com.school_management.overseas_language_centre.feature.intergration.redis.RedisService;
import com.school_management.overseas_language_centre.property.CaptchaProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {
    private final static String CAPTCHA_KEY_PREFIX = "captcha:";
    private final RedisService redisService;
    private final CaptchaProperties properties;
    private final RandomCodeGenerator randomCodeGenerator;
    @Override
    public CaptchaResponse generate() {
        //Generate Code
        //6 =Ss430d
        String captchaId = UUID.randomUUID().toString();
        String generateCode = randomCodeGenerator.generate(properties.getLength());

        redisService.save(
                CAPTCHA_KEY_PREFIX + captchaId,
                generateCode,
                Duration.ofMinutes(properties.getTtlMinutes())
        );
        return CaptchaResponse.builder()
                .captchaId(captchaId)
                .imageBase64(generateCode)
                .enabled(true)
                .build();
    }

    @Override
    public void validate(String captchaId, String captchaData) {

    }
}
