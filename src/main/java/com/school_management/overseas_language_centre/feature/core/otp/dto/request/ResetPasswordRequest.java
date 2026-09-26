package com.school_management.overseas_language_centre.feature.core.otp.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Final step of the forgot-password flow: prove ownership with the OTP and set a new password.
 * Named ResetPassword to avoid clashing with {@link ChangeOwnPasswordRequest}, which is the
 * signed-in "I know my current password" case.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {
    /** Account email — must match the address used in step 1. */
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    /** 6-digit code from the email — verified again even if step 2 was skipped. */
    @NotBlank(message = "OTP is required")
    @Size(min = 6, max = 6, message = "OTP must be 6 digits")
    private String otp;

    /** New plaintext password — hashed with BCrypt before persistence. Strength checked in OtpValidator. */
    @NotBlank(message = "New password is required")
    @Size(min = 8, max = 100, message = "Password must be at least 8 characters")
    private String newPassword;
}
