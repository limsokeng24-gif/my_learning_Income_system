package com.school_management.overseas_language_centre.dto.response;

import lombok.Data;

// DTO mapper Entity
@Data //we need to use lombok
public class RoleResponse {
    private Long id;
    private String name;
    private String description;
}
