package com.school_management.overseas_language_centre.feature.core.user.controller;

import com.school_management.overseas_language_centre.base.BaseApi;
import com.school_management.overseas_language_centre.feature.core.user.service.UserService;
import com.school_management.overseas_language_centre.feature.intergration.fileStorage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RequiredArgsConstructor
@RestController //use for handle request and response
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/{id}/image")
    public ResponseEntity<?> uploadProfileImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {

        String imageKey = userService.uploadProfileImage(
                id,
                file
        );

        return ResponseEntity.ok(
                Map.of(
                        "message", "Profile image uploaded successfully",
                        "imageKey", imageKey
                )
        );
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<BaseApi<Void>> deleteProfileImage(
            @PathVariable Long id
    ) {
        userService.deleteProfileImage(id);

        return ResponseEntity.ok(
                BaseApi.<Void>builder()
                        .status(true)
                        .code(HttpStatus.OK.value())
                        .message("Profile image deleted successfully")
                        .build()
        );
    }
}
