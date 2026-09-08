package com.school_management.overseas_language_centre.feature.auth.dto.response;


import com.school_management.overseas_language_centre.feature.core.user.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String type;
    private String refreshToken;

    private UserResponse user;

    //we use of instead of new
    public static AuthResponse of(
            String token,
            String refreshToken,
            UserResponse user) {
//        return AuthResponse.builder()
//                .token(token)
//                .refreshToken(refreshToken)
//                .user(user)
//                .build();
            AuthResponse response = new AuthResponse();
            response.setToken(token);
            response.setType("Bearer");
            response.setRefreshToken(refreshToken);
            response.setUser(user);
            return response;
    }
}
