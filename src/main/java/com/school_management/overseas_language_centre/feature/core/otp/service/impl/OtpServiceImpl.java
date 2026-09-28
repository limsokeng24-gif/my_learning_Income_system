package com.school_management.overseas_language_centre.feature.core.otp.service.impl;

import com.school_management.overseas_language_centre.feature.core.otp.component.OtpGenerator;
import com.school_management.overseas_language_centre.feature.core.otp.dto.request.ResetPasswordRequest;
import com.school_management.overseas_language_centre.feature.core.otp.dto.request.SendOtpRequest;
import com.school_management.overseas_language_centre.feature.core.otp.dto.request.VerifyOtpRequest;
import com.school_management.overseas_language_centre.feature.core.otp.normalizer.OtpNormalizer;
import com.school_management.overseas_language_centre.feature.core.otp.service.OtpService;
import com.school_management.overseas_language_centre.feature.core.user.repository.UserRepository;
import com.school_management.overseas_language_centre.feature.intergration.email.EmailService;
import com.school_management.overseas_language_centre.feature.intergration.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final OtpGenerator otpGenerator;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final OtpNormalizer otpNormalizer;
    private final RedisService redisService;
    private static final String OTP_PREFIX = "otp:";

    @Override
    public void sendOtp(SendOtpRequest request) {
        //1 normalizer
        otpNormalizer.normalize(request);
        //Get normalized email
        String email = request.getEmail();
        // Now email is trimmed/normalized
        System.out.println("Email = [" + email + "]");

        //2 validator
        //Check email exists in database
        boolean exists = userRepository.existsByUsername(request.getEmail());
        if (!exists) {
            throw new IllegalArgumentException(
                    "Email does not exist"
            );
        }
        // generate code
        String code = otpGenerator.generate();
        System.out.println("Send OTP");
        // 4. Redis key
        String redisKey = "otp:" + email;
        System.out.println("Redis key: [" + redisKey + "]");
        // 5. Save
        redisService.save(redisKey, code);
        emailService.sentOtp(request.getEmail(), code);
    }

    @Override
    public void verifyOtp(VerifyOtpRequest request) {
       //normalizer
        otpNormalizer.normalize(request);

        String email = request.getEmail();
        String otp = request.getOtp();

        // 2. Check user exists
        if (!userRepository.existsByUsername(email)) {
            throw new IllegalArgumentException("Email does not exist");
        }

        // 3. Get OTP from Redis
        String storedOtp = redisService
                .get(OTP_PREFIX + email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "OTP is expired or not found"
                        )
                );

        // 4. Compare OTP
        if (!storedOtp.equals(otp)) {
            throw new IllegalArgumentException("Invalid OTP");
        }

        // 5. OTP is correct
        System.out.println("OTP verified successfully");
    }


    @Override
    public void resetPassword(ResetPasswordRequest request) {

    }
}
