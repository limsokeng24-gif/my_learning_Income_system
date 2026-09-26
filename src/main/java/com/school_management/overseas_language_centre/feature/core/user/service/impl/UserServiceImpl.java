package com.school_management.overseas_language_centre.feature.core.user.service.impl;

import com.school_management.overseas_language_centre.entity.Role;
import com.school_management.overseas_language_centre.entity.User;
import com.school_management.overseas_language_centre.feature.core.role.dto.filter.RoleFilter;
import com.school_management.overseas_language_centre.feature.core.role.dto.request.RoleRequest;
import com.school_management.overseas_language_centre.feature.core.role.mapper.RoleMapper;
import com.school_management.overseas_language_centre.feature.core.role.normalizer.RoleNormalizer;
import com.school_management.overseas_language_centre.feature.core.role.repository.RoleRepository;
import com.school_management.overseas_language_centre.feature.core.role.validator.RoleValidator;
import com.school_management.overseas_language_centre.feature.core.user.dto.request.UserRequest;
import com.school_management.overseas_language_centre.feature.core.user.dto.response.UserResponse;
import com.school_management.overseas_language_centre.feature.core.user.mapper.UserMapper;
import com.school_management.overseas_language_centre.feature.core.user.nomarlizer.UserNormalizer;
import com.school_management.overseas_language_centre.feature.core.user.repository.UserRepository;
import com.school_management.overseas_language_centre.feature.core.user.service.UserService;
import com.school_management.overseas_language_centre.feature.intergration.fileStorage.FileStorageService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserNormalizer userNormalizer;
    private final FileStorageService fileStorageService;
    private final DataFormatter dataFormatter = new DataFormatter();
    private final UserMapper userMapper;
    private static final String PROFILE_IMAGE_DIR = "profiles";
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse create(UserRequest request) {
        userNormalizer.normalize(request);
        // Request DTO -> Entity
        User entity = userMapper.toEntity(request);
        // Encode password
        entity.setPasswordHash(
                passwordEncoder.encode(request.getPasswordHash())
        );
        // Default value
        entity.setEnabled(true);
        User save = userRepository.save(entity);
        // Entity -> DTO and return
        return userMapper.toResponse(save);
    }

    @Override
    public UserResponse getById(Long id) {
        return null;
    }

    @Override
    public UserResponse updateById(Long id, UserRequest request) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    //uploadImage
    @Override
    public String uploadProfileImage(Long userId, MultipartFile file) {
        // 1. Find user from PostgreSQL
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "User not found with id: " + userId
                        )
                );

        // 2. Remember old image
        String oldImageKey = user.getProfileImageKey();

        // 3. Upload NEW image to MinIO
        String newImageKey = fileStorageService.storeImage(
                file,
                PROFILE_IMAGE_DIR + "/" + userId
        );

        try {

            // 4. Save NEW image key to PostgreSQL
            user.setProfileImageKey(newImageKey);
            userRepository.save(user);

            // 5. Delete OLD image from MinIO
            if (oldImageKey != null && !oldImageKey.isBlank()) {
                fileStorageService.deleteObject(oldImageKey);
            }

            // 6. Return new image key
            return newImageKey;

        } catch (Exception e) {
            // If database update fails,
            // remove the newly uploaded image
            fileStorageService.deleteObject(newImageKey);

            throw e;
        }
    }

    //delete image
    @Transactional
    @Override
    public void deleteProfileImage(Long userId) {
        // 1. Find user
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "User not found with id: " + userId
                        )
                );

        // 2. Get current image key from PostgreSQL
        String imageKey = user.getProfileImageKey();

        // 3. If user has no image, nothing to delete
        if (imageKey == null || imageKey.isBlank()) {
            return;
        }

        // 4. Delete image from MinIO
        fileStorageService.deleteObject(imageKey);

        // 5. Remove image key from PostgreSQL
        user.setProfileImageKey(null);
        userRepository.save(user);
    }
}
