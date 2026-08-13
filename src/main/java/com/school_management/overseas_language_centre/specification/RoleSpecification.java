package com.school_management.overseas_language_centre.specification;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.school_management.overseas_language_centre.dto.filter.RoleFilter;
import com.school_management.overseas_language_centre.entity.Role;
import com.school_management.overseas_language_centre.util.PageUtil;
public final class RoleSpecification {
    private RoleSpecification(){

    }
    // 3 fiels
    // 2 field
    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final List<String> ALLOWED_SORT_FIELDS = List.of(FIELD_NAME, FIELD_ID);

    private static Specification<Role> hasName(String name) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(name)) {
                return null;
            }

            return cb.like(
                    cb.lower(root.get(FIELD_NAME)),
                    "%" + name.trim().toLowerCase() + "%"
            );
        };
    }

    //trim // normalizer

    //



    /** WHERE clause: case-insensitive contains on name and/or code. */
    public static Specification<Role> build(RoleFilter filter) {
        if (filter == null) {
            return Specification.allOf(
                    hasName(null)
            );
        }

        return Specification.allOf(
                hasName(filter.getName())
        );
    }

    public static Sort sort(RoleFilter filter) {
        return PageUtil.sort(filter, FIELD_NAME, ALLOWED_SORT_FIELDS);
    }

    public static Pageable pageable(RoleFilter filter) {
        return PageUtil.pageable(filter, FIELD_NAME, ALLOWED_SORT_FIELDS);
    }
}
