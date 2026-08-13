package com.school_management.overseas_language_centre.dto.filter;

public interface PageSortFilter {
    String getSortBy();
    String getDirection();
    Integer getPage();
    Integer getSize();

}
