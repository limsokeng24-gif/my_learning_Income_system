package com.school_management.overseas_language_centre.feature.core.role.normalizer;

import com.school_management.overseas_language_centre.component.StringNormalizer;
import com.school_management.overseas_language_centre.feature.core.role.dto.request.RoleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RoleNormalizer {
    private final StringNormalizer stringNormalizer;
    public RoleRequest normalize(RoleRequest request){
        request.setName(stringNormalizer.normalizerUpperCase(request.getName()));
        request.setDescription(stringNormalizer.normalizer(request.getDescription()));
        return request;
    }
}
