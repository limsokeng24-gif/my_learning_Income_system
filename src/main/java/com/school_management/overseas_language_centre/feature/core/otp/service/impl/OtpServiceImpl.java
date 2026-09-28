package com.school_management.overseas_language_centre.feature.core.otp.service.impl;

import com.school_management.overseas_language_centre.entity.Otp;
import com.school_management.overseas_language_centre.entity.User;
import com.school_management.overseas_language_centre.feature.core.otp.component.OtpGenerator;
import com.school_management.overseas_language_centre.feature.core.otp.dto.request.ResetPasswordRequest;
import com.school_management.overseas_language_centre.feature.core.otp.dto.request.SendOtpRequest;
import com.school_management.overseas_language_centre.feature.core.otp.dto.request.VerifyOtpRequest;
import com.school_management.overseas_language_centre.feature.core.otp.normalizer.OtpNormalizer;
import com.school_management.overseas_language_centre.feature.core.otp.repository.OtpRepository;
import com.school_management.overseas_language_centre.feature.core.otp.service.OtpService;
import com.school_management.overseas_language_centre.feature.core.user.repository.UserRepository;
import com.school_management.overseas_language_centre.feature.intergration.email.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final OtpGenerator otpGenerator;
    private final OtpRepository otpRepository;
    private final OtpNormalizer otpNormalizer;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void sendOtp(SendOtpRequest request) {
        // 1. Normalize
        otpNormalizer.normalize(request);

        String email = request.getEmail();

        // 2. Find user
        User user = userRepository.findByUsername(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Email does not exist"
                        )
                );

        Long userId = user.getId();

        // 3. Generate new OTP
        String code = otpGenerator.generate();

        // 4. Encrypt OTP
        String encryptedOtp = passwordEncoder.encode(code);

        // 5. Create NEW OTP record
        Otp otp = new Otp();
        otp.setUserId(userId);
        otp.setOtpEncrypted(encryptedOtp);
        otp.setVerified(false);
        otp.setSentCount(1);
        otp.setLastSentAt(LocalDateTime.now());
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        otp.setCreatedAt(LocalDateTime.now());
        otp.setUpdatedAt(LocalDateTime.now());

        // 6. Save NEW OTP
        otpRepository.save(otp);

        // 7. Send OTP to email
        emailService.sentOtp(email, code);
    }

    @Override
    public void verifyOtp(VerifyOtpRequest request) {

        otpNormalizer.normalize(request);

        String email = request.getEmail();
        String otpCode = request.getOtp();

        User user = userRepository.findByUsername(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Email does not exist"
                        )
                );

        Long userId = user.getId();


        // 3. Get the latest OTP
        Otp entity = otpRepository
                .findTopByUserIdOrderByIdDesc(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "OTP not found"
                        )
                );

        // 4. Check if already verified
        if (Boolean.TRUE.equals(entity.getVerified())) {
            throw new IllegalStateException(
                    "OTP has already been verified"
            );
        }

        // 5. Check expiration
        if (entity.getExpiresAt() != null &&
                entity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException(
                    "OTP has expired"
            );
        }

        // 6. Compare OTP
        boolean matches = passwordEncoder.matches(
                otpCode,
                entity.getOtpEncrypted()
        );

        // 7. Invalid OTP
        if (!matches) {
            throw new IllegalArgumentException(
                    "Invalid OTP"
            );
        }

        // 8. Mark verified
        entity.setVerified(true);
        entity.setUpdatedAt(LocalDateTime.now());

        // 9. Save
        otpRepository.save(entity);
    }


    @Override
    public void resetPassword(ResetPasswordRequest request) {

    }
}
