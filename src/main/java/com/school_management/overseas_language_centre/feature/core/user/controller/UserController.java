package com.school_management.overseas_language_centre.feature.core.user.controller;

import com.school_management.overseas_language_centre.base.BaseApi;
import com.school_management.overseas_language_centre.entity.User;
import com.school_management.overseas_language_centre.feature.core.role.dto.request.RoleRequest;
import com.school_management.overseas_language_centre.feature.core.role.dto.response.RoleResponse;
import com.school_management.overseas_language_centre.feature.core.user.dto.request.UserRequest;
import com.school_management.overseas_language_centre.feature.core.user.dto.response.UserResponse;
import com.school_management.overseas_language_centre.feature.core.user.service.UserService;
import com.school_management.overseas_language_centre.feature.intergration.fileStorage.FileStorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Map;

@RequiredArgsConstructor
@RestController //use for handle request and response
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;

    //DTO : data transfer object /1. transfer from dto => entity /2. transfer from entity to dto
    //now we can call that service for use
    @PostMapping("/create") //use this mapping for insert data to table
//    public ResponseEntity<?> create(@RequestBody RoleRequest request) {
    //<?> response any contain // <BaseApi<RoleResponse>> response roleResponse only
    public ResponseEntity<BaseApi<UserResponse>> create(@Valid @RequestBody UserRequest request) { //@valid from RoleRequest
        UserResponse userResponse = userService.create(request);
        return ResponseEntity.ok(
                BaseApi.<UserResponse>builder()
                        .status(true)
                        .code(HttpStatus.OK.value())
                        .message("Success")
                        .timestamp(LocalDateTime.now())
                        .data(userResponse)
                        .build()
        );
    }

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
