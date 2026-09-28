package com.school_management.overseas_language_centre.feature.core.otp.service.impl;

import com.school_management.overseas_language_centre.feature.core.otp.component.OtpGenerator;
import com.school_management.overseas_language_centre.feature.core.otp.dto.request.ResetPasswordRequest;
import com.school_management.overseas_language_centre.feature.core.otp.dto.request.SendOtpRequest;
import com.school_management.overseas_language_centre.feature.core.otp.dto.request.VerifyOtpRequest;
import com.school_management.overseas_language_centre.feature.core.otp.normalizer.OtpNormalizer;
import com.school_management.overseas_language_centre.feature.core.otp.service.OtpService;
import com.school_management.overseas_language_centre.feature.core.user.repository.UserRepository;
import com.school_management.overseas_language_centre.feature.intergration.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final OtpGenerator otpGenerator;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final OtpNormalizer otpNormalizer;

    @Override
    public void sendOtp(SendOtpRequest request) {
        //1 normalizer
        otpNormalizer.normalize(request);
        //Get normalized email
        String email = request.getEmail();
        // Now email is trimmed/normalized
        System.out.println("Email = [" + email + "]");
        //2 validator
        // 1. Check email exists in database
        boolean exists = userRepository.existsByUsername(request.getEmail());
        if (!exists) {
            throw new IllegalArgumentException(
                    "Email does not exist"
            );
        }
        // 4 encryption

        // generate code
        String code = otpGenerator.generate();
        System.out.println("Send OTP");
        //send code to email
        emailService.sentOtp(request.getEmail(), code);
    }

    @Override
    public void verifyOtp(VerifyOtpRequest request) {

    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {

    }
}
