package com.school_management.overseas_language_centre.feature.core.user.dto.filter;

import com.school_management.overseas_language_centre.dto.filter.BaseFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFilter extends BaseFilter {
    private String username;
    private String nickName;
    private String passwordHash;
    private Boolean enabled;
}
