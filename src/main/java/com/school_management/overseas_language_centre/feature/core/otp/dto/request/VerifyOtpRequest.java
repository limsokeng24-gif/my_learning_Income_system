package com.school_management.overseas_language_centre.feature.core.otp.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Step 2 request body — confirm the emailed code without changing the password.
 * Optional client-side step; step 3 re-verifies the OTP anyway.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyOtpRequest {
    /** Account email — must match the address used in step 1. */
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    /** 6-digit code from the email. Bean Validation checks length; OtpValidator checks digits. */
    @NotBlank(message = "OTP is required")
    @Size(min = 6, max = 6, message = "OTP must be 6 digits")
    private String otp;
}
