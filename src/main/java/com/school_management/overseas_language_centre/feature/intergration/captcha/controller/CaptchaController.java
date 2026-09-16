package com.school_management.overseas_language_centre.feature.intergration.captcha.controller;

import com.school_management.overseas_language_centre.feature.intergration.captcha.service.CaptchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/captcha")
@RequiredArgsConstructor
public class CaptchaController {
    private final CaptchaService captchaService;
    @GetMapping // GET /api/captcha
    public ResponseEntity<?> generate() {
        // Delegate to service, wrap payload in the standard API envelope
        return ResponseEntity.ok(captchaService.generate());
    }
}
