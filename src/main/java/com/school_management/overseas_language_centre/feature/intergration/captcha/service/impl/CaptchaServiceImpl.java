package com.school_management.overseas_language_centre.feature.intergration.captcha.service.impl;

import com.school_management.overseas_language_centre.encryption.EncryptionService;
import com.school_management.overseas_language_centre.feature.intergration.captcha.component.CaptchaImageRenderer;
import com.school_management.overseas_language_centre.feature.intergration.captcha.component.RandomCodeGenerator;
import com.school_management.overseas_language_centre.feature.intergration.captcha.dto.response.CaptchaResponse;
import com.school_management.overseas_language_centre.feature.intergration.captcha.service.CaptchaService;
import com.school_management.overseas_language_centre.feature.intergration.redis.RedisService;
import com.school_management.overseas_language_centre.property.CaptchaProperties;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {
    private final static String CAPTCHA_KEY_PREFIX = "captcha:";
    private final RedisService redisService;
    private final CaptchaProperties properties;
    private final RandomCodeGenerator randomCodeGenerator;
    private final EncryptionService encryptionService;
    private final CaptchaImageRenderer captchaImageRenderer;
    private final CaptchaProperties captchaProperties;

    @Override
    public CaptchaResponse generate() {
        //Generate Code
        //6 =Ss430d
        if (!properties.isEnabled()){
            return CaptchaResponse.builder()
                    .enabled(false)
                    .build();
        }
        String captchaId = UUID.randomUUID().toString();
        String generateCode = randomCodeGenerator.generate(properties.getLength());

        String encryptText = encryptionService.encrypt(generateCode);

        String image = captchaImageRenderer.render(generateCode);

        redisService.save(
                CAPTCHA_KEY_PREFIX + captchaId,
                encryptText,
                Duration.ofMinutes(properties.getTtlMinutes())
        );
        return CaptchaResponse.builder()
                .captchaId(captchaId)
                .imageBase64(image)
                .enabled(true)
                .build();
    }

    @Override
    public void validate(String captchaId, String captchaData) {
        if (!captchaProperties.isEnabled()) {
            return;
        }
        // check captchaId
        String storedCaptchaCode = getStoredCaptchaCode(captchaId);
        // check 1, remove from redis
        // captchaData
        //
        //storedCaptchaCode == captchaData
        if (!storedCaptchaCode.equals(captchaData.trim())){
            throw new ValidationException("Incorrect captcha. Please try again");
        }

    }

    private String getStoredCaptchaCode(String captchaId) {
        String key = CAPTCHA_KEY_PREFIX + captchaId;

        System.out.println("CAPTCHA ID: " + captchaId);
        System.out.println("Redis key: " + key);

        Optional<String> stored = redisService.get(key);

        System.out.println("CAPTCHA exists: " + stored.isPresent());

        if (stored.isEmpty()) {
            throw new ValidationException(
                    "Captcha expired. Please refresh and try again"
            );
        }

        return encryptionService.decrypt(stored.get());
    }
}
