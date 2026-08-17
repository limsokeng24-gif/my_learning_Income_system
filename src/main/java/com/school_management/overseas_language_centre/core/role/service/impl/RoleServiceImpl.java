package com.school_management.overseas_language_centre.core.role.service.impl;
import com.school_management.overseas_language_centre.dto.filter.RoleFilter;
import com.school_management.overseas_language_centre.dto.request.RoleRequest;
import com.school_management.overseas_language_centre.dto.response.RoleResponse;
import com.school_management.overseas_language_centre.entity.Role;
import com.school_management.overseas_language_centre.exception.ResourceNotFoundException;
import com.school_management.overseas_language_centre.core.role.mapper.RoleMapper;
import com.school_management.overseas_language_centre.core.role.normalizer.RoleNormalizer;
import com.school_management.overseas_language_centre.core.role.repository.RoleRepository;
import com.school_management.overseas_language_centre.core.role.service.RoleService;
import com.school_management.overseas_language_centre.core.role.specification.RoleSpecification;
import com.school_management.overseas_language_centre.core.role.validator.RoleValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;

@Service //use this for tell that code this one is use for service logic
//we create this RoleServiceImpl after RoleService
public class RoleServiceImpl implements RoleService { //RoleServiceImpl jea constructor
    //derm bey ach hav repository mk use ban yg trov hav vea mk use
    //so we use dependency rejection
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final RoleNormalizer roleNormalizer;
    private final RoleValidator roleValidator;

    RoleServiceImpl (RoleRepository roleRepository, RoleMapper roleMapper, RoleNormalizer roleNormalizer, RoleValidator roleValidator) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.roleNormalizer = roleNormalizer;
        this.roleValidator = roleValidator;
    }


    @Override
    public RoleResponse create(RoleRequest request) {

        roleNormalizer.normalize(request);
        roleValidator.validateCreate(request);
        /* Pass value RoleRequest */
        // Entity
        // mapper
        // DTO -> Entity
        // validator//normalizer
        Role entity = roleMapper.toEntity(request);
        // No Id = save
        // Has Id = update
        Role save = roleRepository.save(entity);

        // Entity -> DTO and return
        return roleMapper.toResponse(save);

    }

    @Override
    public RoleResponse getById(Long id) {
        // map
        // Role Entity -> role response
        return roleRepository.findById(id)
                .map(roleMapper::toResponse)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Role", id)
                );


    }

    @Override
    public RoleResponse updateById(Long id, RoleRequest request) {

        // Remove leading and trailing spaces
        roleNormalizer.normalize(request);
        // មុខនិង update យើងត្រូវតែQuery ទៅយក Data វាសិន
        Role entity = roleRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Role", id)
                );
        //validator
        roleValidator.validateUpdate(entity.getId(), entity);

        roleMapper.updateEntity(entity, request);

        Role response = roleRepository.save(entity);

        return roleMapper.toResponse(response);
    }

    @Override
    public void deleteById(Long id) {
        Role entity = roleRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("Role Not Found with id " + id )
                );

//        roleRepository.deleteById(id);
        roleRepository.delete(entity);

    }

    @Override
    public List<RoleResponse> getAll(RoleFilter filter) {
        Specification<Role> spec = RoleSpecification.build(filter);
        Sort sort = RoleSpecification.sort(filter);

        return roleRepository.findAll(spec, sort).stream()
                .map(roleMapper::toResponse)
                .toList();


    }

    @Override
    public Page<RoleResponse> getAllPagination(RoleFilter filter) {

        Specification<Role> spec = RoleSpecification.build(filter);
        Pageable pageable =   RoleSpecification.pageable(filter);

        Page<Role> roles = roleRepository.findAll(spec, pageable);

        return roles.map(roleMapper::toResponse);
    }
}
