package com.school_management.overseas_language_centre.feature.core.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //we need to use lombok
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotBlank(message = "Username cannot be blank") //when we use this validation //we need to put @Valid in roleController for use it
    private String username;
    @Size(max = 50, message = "Nickname too long")
    private String nickName;
    @NotBlank(message = "Password cannot be blank") //when we use this validation //we need to put @Valid in roleController for use it
    private String passwordHash;

    private Boolean enabled;

}
