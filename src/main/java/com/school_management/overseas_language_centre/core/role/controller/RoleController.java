package com.school_management.overseas_language_centre.core.role.controller;


import com.school_management.overseas_language_centre.base.BaseApi;
import com.school_management.overseas_language_centre.base.BaseApiPagination;
import com.school_management.overseas_language_centre.dto.filter.RoleFilter;
import com.school_management.overseas_language_centre.dto.pagination.PageDTO;
import com.school_management.overseas_language_centre.dto.request.RoleRequest;
import com.school_management.overseas_language_centre.dto.response.RoleResponse;
import com.school_management.overseas_language_centre.core.role.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController //use for handle request and response
@RequestMapping("api/role") // use for create route or URl
public class RoleController {
    //Dependency rejection
    private final RoleService roleService;

    //jak injection roleService jol knong RoleController
    RoleController(RoleService roleService){
        this.roleService = roleService;
    }

    //DTO : data transfer object /1. transfer from dto => entity /2. transfer from entity to dto
    //now we can call that service for use
    @PostMapping("/create") //use this mapping for insert data to table
//    public ResponseEntity<?> create(@RequestBody RoleRequest request) {
    //<?> response any contain // <BaseApi<RoleResponse>> response roleResponse only
    public ResponseEntity<BaseApi<RoleResponse>> create(@Valid @RequestBody RoleRequest request) { //@valid from RoleRequest
        RoleResponse roleResponse = roleService.create(request);
        return ResponseEntity.ok(
                BaseApi.<RoleResponse>builder()
                        .status(true)
                        .code(HttpStatus.OK.value())
                        .message("Success")
                        .timestamp(LocalDateTime.now())
                        .data(roleResponse)
                        .build()
        );
    }
    //1 Homework
// 2 បញ្ចូល Data កុំអោយមាន Space មុខក្រោយ, ចង់បាន អក្សធំទាំងអស់ នៅក្នុង field Name
    //3 Insert data cannot duplicate
    // 4 Update data cannot exist with another id
    @GetMapping("{id}")
    public ResponseEntity<BaseApi<RoleResponse>> getById(@PathVariable Long id) {
        RoleResponse roleResponse = roleService.getById(id);

        return ResponseEntity.ok(
                BaseApi.<RoleResponse>builder()
                        .status(true)
                        .code(HttpStatus.OK.value())
                        .message("Success")
                        .timestamp(LocalDateTime.now())
                        .data(roleResponse)
                        .build()
        );
    }

    // ? = unknow type ?
    @GetMapping("all")
    public ResponseEntity<BaseApi<List<RoleResponse>>> getAll(RoleFilter filter) {

        List<RoleResponse> all = roleService.getAll(filter);

        return ResponseEntity.ok(
                BaseApi.<List<RoleResponse>>builder()
                        .status(true)
                        .code(HttpStatus.OK.value())
                        .message("Success")
                        .timestamp(LocalDateTime.now())
                        .data(all)
                        .build()
        );
    }


    @PutMapping("{id}")
//    public ResponseEntity<?> updateById(@PathVariable Long id, @RequestBody RoleRequest request) {
    public ResponseEntity<BaseApi<RoleResponse>> updateById(@Valid @PathVariable Long id, @RequestBody RoleRequest request) {
        RoleResponse roleResponse = roleService.updateById(id, request);

        return ResponseEntity.ok(
                BaseApi.<RoleResponse>builder()
                        .status(true)
                        .code(HttpStatus.OK.value())
                        .message("Success")
                        .timestamp(LocalDateTime.now())
                        .data(roleResponse)
                        .build()
        );

    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {

        roleService.deleteById(id);

        return ResponseEntity.ok(
                BaseApi.<RoleResponse>builder()
                        .status(true)
                        .code(HttpStatus.OK.value())
                        .message("Success")
                        .timestamp(LocalDateTime.now())
                        .data(null)
                        .build()
        );
    }

    //page 0 = element 1
    @GetMapping("pagination")
    public ResponseEntity<BaseApiPagination<RoleResponse>> getPagination(RoleFilter filter) {

        Page<RoleResponse> allPagination = roleService.getAllPagination(filter);
        PageDTO pageDTO = new PageDTO(allPagination);
        return ResponseEntity.ok(
                BaseApiPagination.<RoleResponse>builder()
                        .status(true)
                        .code(HttpStatus.OK.value())
                        .message("Success")
                        .timestamp(LocalDateTime.now())
                        .pagination(pageDTO.getPagination())
                        .data((List<RoleResponse>) pageDTO.getItems())
                        .build()
        );
    }
}
