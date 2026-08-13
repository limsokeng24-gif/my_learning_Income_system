package com.school_management.overseas_language_centre.dto.filter;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleFilter extends BaseFilter {
    //we use baseFilter for abstract becuase we it for implement only
    //we can't call it for use again
    private String name;
    private String description;

}
