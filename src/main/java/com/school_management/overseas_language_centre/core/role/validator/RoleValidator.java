package com.school_management.overseas_language_centre.core.role.validator;

import com.school_management.overseas_language_centre.dto.request.RoleRequest;
import com.school_management.overseas_language_centre.entity.Role;
import com.school_management.overseas_language_centre.core.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleValidator { //ke brer validator dermbey pteang ptot data douy oy vea tv tam business logic doch jea name can't null jea derm
    // pel name already exist vea ng show nv knong terminal robos yg
    private final RoleRepository roleRepository;
    public void validateCreate(RoleRequest request) {
        if (roleRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Role name already exists");
        }
        if (roleRepository.existsByDescription(request.getDescription())) {
            throw new IllegalArgumentException("Role description already exists");
        }
    }

    public void validateUpdate (Long id, Role request) {
//        boolean exist = roleRepository.existsByNameAndIdNot(request.getName(), id);
//        if (!exist) {
//            throw new IllegalArgumentException("Role name already exist in validate update");
//        }
        if (roleRepository.existsByNameAndIdNot(request.getName(), id))
            throw new IllegalArgumentException("Role name already exists");

        if (roleRepository.existsByDescriptionAndIdNot(request.getDescription(), id))
            throw new IllegalArgumentException("Role description already exists");
    }


}
