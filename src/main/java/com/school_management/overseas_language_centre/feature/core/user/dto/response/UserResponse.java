package com.school_management.overseas_language_centre.feature.core.user.dto.response;

import com.school_management.overseas_language_centre.feature.core.role.dto.response.RoleResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String nickName;
    private String passwordHash;
    private Boolean enabled;
}
