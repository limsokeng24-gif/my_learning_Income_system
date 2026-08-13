package com.school_management.overseas_language_centre.service;
//this RoleService we create after RoleRepository


import com.school_management.overseas_language_centre.dto.filter.RoleFilter;
import com.school_management.overseas_language_centre.dto.request.RoleRequest;
import com.school_management.overseas_language_centre.dto.response.RoleResponse;
import com.school_management.overseas_language_centre.entity.Role;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RoleService {
    RoleResponse create(RoleRequest request);
    RoleResponse getById(Long id);
    RoleResponse updateById(Long id, RoleRequest request);
    void deleteById(Long id);

    List<RoleResponse> getAll(RoleFilter filter);
    Page<RoleResponse> getAllPagination(RoleFilter filter);
    //getAllPaginationFilter
    // collection framework

    // size= 2 record នៅក្នុង 2 page
    // 1 id name , description
    // 2


    // getById
    //update
    // deleteById,

    // getAllFiler


    // getAllPaginationFilter
}
