package com.school_management.overseas_language_centre.feature.intergration.captcha.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CaptchaRequest {
    private String captchaId;
    @NotBlank
    private String captchaData;
}
