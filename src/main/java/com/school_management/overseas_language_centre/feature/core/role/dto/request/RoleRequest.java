package com.school_management.overseas_language_centre.feature.core.role.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//we need to use dto because some field we need to hide
//so we use it to control which field we want to show and which one to hide because some field we to hide to make it secure
//example : password because if we only use entity it will show all entity to client but, if we use dto we can hide some field
@Data //we need to use lombok
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequest {
    @NotBlank(message = "Name cannot be blank") //when we use this validation //we need to put @Valid in roleController for use it
    private String name;
    @Size(max = 255, message = "Description too long")
    private String description;
}
