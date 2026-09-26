package com.school_management.overseas_language_centre.feature.core.user.normalizer;

import com.school_management.overseas_language_centre.component.StringNormalizer;
import com.school_management.overseas_language_centre.feature.core.role.dto.request.RoleRequest;
import com.school_management.overseas_language_centre.feature.core.user.dto.request.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserNormalizer {
    private final StringNormalizer stringNormalizer;
    public UserRequest normalize(UserRequest request){
        request.setUsername(stringNormalizer.normalizer(request.getUsername()));
        request.setNickName(stringNormalizer.normalizer(request.getNickName()));
        request.setPasswordHash(stringNormalizer.normalizer(request.getPasswordHash()));
        return request;
    }
}
