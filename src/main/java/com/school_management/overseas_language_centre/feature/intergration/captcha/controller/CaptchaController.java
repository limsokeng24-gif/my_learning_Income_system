package com.school_management.overseas_language_centre.feature.intergration.captcha.controller;

import com.school_management.overseas_language_centre.feature.intergration.captcha.dto.request.CaptchaRequest;
import com.school_management.overseas_language_centre.feature.intergration.captcha.service.CaptchaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
@Tag(name = "Captcha", description = "Captcha API")
@RequestMapping("/api/captcha")
@RequiredArgsConstructor
public class CaptchaController {
    private final CaptchaService captchaService;
    @GetMapping // GET /api/captcha
    public ResponseEntity<?> generate() {
        // Delegate to service, wrap payload in the standard API envelope
        return ResponseEntity.ok(captchaService.generate());
    }
    @PostMapping("/validate") // POST /api/captcha/validate
    public ResponseEntity<?> validate(
            @Valid // run Bean Validation on the request body
            @RequestBody CaptchaRequest request) {
        captchaService.validate(request.getCaptchaId(), request.getCaptchaData());
        return ResponseEntity.ok(null);
    }

}
