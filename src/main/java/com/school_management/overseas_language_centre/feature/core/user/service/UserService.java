package com.school_management.overseas_language_centre.feature.core.user.service;

import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    String uploadProfileImage(Long userId, MultipartFile file);
    void deleteProfileImage(Long userId);
}
