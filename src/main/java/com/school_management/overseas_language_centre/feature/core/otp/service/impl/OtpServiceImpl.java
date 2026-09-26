package com.school_management.overseas_language_centre.feature.core.otp.service.impl;

import com.school_management.overseas_language_centre.feature.core.otp.component.OtpGenerator;
import com.school_management.overseas_language_centre.feature.core.otp.dto.request.ResetPasswordRequest;
import com.school_management.overseas_language_centre.feature.core.otp.dto.request.SendOtpRequest;
import com.school_management.overseas_language_centre.feature.core.otp.dto.request.VerifyOtpRequest;
import com.school_management.overseas_language_centre.feature.core.otp.service.OtpService;
import com.school_management.overseas_language_centre.feature.intergration.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final OtpGenerator otpGenerator;
    private final EmailService emailService;

    @Override
    public void sendOtp(SendOtpRequest request) {
        //1 normalizer
        //2 validator
        // 3 check email have in db
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
