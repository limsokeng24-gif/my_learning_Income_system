package com.school_management.overseas_language_centre.feature.core.otp.normalizer;

import com.school_management.overseas_language_centre.component.StringNormalizer;
import com.school_management.overseas_language_centre.feature.core.otp.dto.request.SendOtpRequest;
import com.school_management.overseas_language_centre.feature.core.otp.dto.request.VerifyOtpRequest;
import com.school_management.overseas_language_centre.feature.core.user.dto.request.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OtpNormalizer {
    private final StringNormalizer stringNormalizer;
    public SendOtpRequest normalize(SendOtpRequest request){
        request.setEmail(stringNormalizer.normalizer(request.getEmail()));
        return request;
    }
    public VerifyOtpRequest normalize(VerifyOtpRequest request) {
        request.setEmail(
                stringNormalizer.normalizerLowerCase(
                        request.getEmail()
                )
        );
        request.setOtp(
                request.getOtp().trim()
        );
        return request;
    }
}
