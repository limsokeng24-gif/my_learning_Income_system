package com.school_management.overseas_language_centre.feature.core.user.mapper;

import com.school_management.overseas_language_centre.entity.Role;
import com.school_management.overseas_language_centre.entity.User;
import com.school_management.overseas_language_centre.feature.core.role.dto.request.RoleRequest;
import com.school_management.overseas_language_centre.feature.core.user.dto.request.UserRequest;
import com.school_management.overseas_language_centre.feature.core.user.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    /** Safe outward DTO: id, username, nickName, enabled only. */
    UserResponse toResponse(User user);
    User toEntity(UserRequest request);
}
