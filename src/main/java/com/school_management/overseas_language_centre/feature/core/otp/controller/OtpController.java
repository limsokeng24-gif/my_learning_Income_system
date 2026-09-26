package com.school_management.overseas_language_centre.feature.core.otp.controller;

import com.school_management.overseas_language_centre.feature.core.otp.dto.request.SendOtpRequest;
import com.school_management.overseas_language_centre.feature.core.otp.service.OtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController       // JSON REST controller
@RequestMapping("/api/otp") // base path for every method below
@RequiredArgsConstructor
public class OtpController {
    // Business logic lives in the service — controller stays thin
    private final OtpService otpService;

    @PostMapping("/send") // POST /api/otp/send
    public ResponseEntity<?> sendOtp(
            @Valid // run Bean Validation on the request body (shape only)
            @RequestBody SendOtpRequest request) {

        // Throws ValidationException on failure → GlobalExceptionHandler → 400
        otpService.sendOtp(request);

        // No data payload — just { status: 200, title: "OK", … }
        return ResponseEntity.ok(null);
    }
}
