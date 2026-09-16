package com.school_management.overseas_language_centre.feature.intergration.captcha.service;

import com.school_management.overseas_language_centre.feature.intergration.captcha.dto.response.CaptchaResponse;

public interface CaptchaService {
    CaptchaResponse generate();
    void validate(String captchaId, String captchaData);
}
