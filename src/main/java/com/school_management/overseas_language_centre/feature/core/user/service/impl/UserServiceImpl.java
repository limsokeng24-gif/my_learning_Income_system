package com.school_management.overseas_language_centre.feature.core.user.service.impl;

import com.school_management.overseas_language_centre.entity.User;
import com.school_management.overseas_language_centre.feature.core.user.repository.UserRepository;
import com.school_management.overseas_language_centre.feature.core.user.service.UserService;
import com.school_management.overseas_language_centre.feature.intergration.fileStorage.FileStorageService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    private static final String PROFILE_IMAGE_DIR = "profiles";
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
