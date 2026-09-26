package com.school_management.overseas_language_centre.feature.core.user.service;

import com.school_management.overseas_language_centre.feature.core.role.dto.filter.RoleFilter;
import com.school_management.overseas_language_centre.feature.core.role.dto.request.RoleRequest;
import com.school_management.overseas_language_centre.feature.core.role.dto.response.RoleResponse;
import com.school_management.overseas_language_centre.feature.core.user.dto.request.UserRequest;
import com.school_management.overseas_language_centre.feature.core.user.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    UserResponse create(UserRequest request);
    UserResponse getById(Long id);
    UserResponse updateById(Long id, UserRequest request);
    void deleteById(Long id);

    //for uploadimage
    String uploadProfileImage(Long userId, MultipartFile file);
    void deleteProfileImage(Long userId);
}
