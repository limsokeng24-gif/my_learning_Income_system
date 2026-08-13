package com.school_management.overseas_language_centre.dto.filter;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

@Data
public abstract class BaseFilter implements PageSortFilter{
    //this BaseFilter has relationship with pagesortfilter
    private String sortBy;
    //DESC, ASC
    // boilerplate code
    private String direction;
    private Integer page;
    private Integer size;
}
