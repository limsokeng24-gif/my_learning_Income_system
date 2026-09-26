package com.school_management.overseas_language_centre.feature.core.otp.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Step 1 request body — ask the server to email a one-time code.
 * Only the account email is needed; no password or OTP yet.
 */
@Data // Lombok: getters, setters, equals, hashCode, toString
@NoArgsConstructor
@AllArgsConstructor
public class SendOtpRequest {
    /** Account email (username in this project). Normalized to lowercase before lookup. */
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
}
