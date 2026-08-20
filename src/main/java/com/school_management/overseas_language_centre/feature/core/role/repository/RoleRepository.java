package com.school_management.overseas_language_centre.feature.core.role.repository;

import com.school_management.overseas_language_centre.entity.Role;
import jakarta.persistence.Id;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository //repository is data access layer
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    boolean existsByName(String name);
    boolean existsByDescription(String description);
    boolean existsByNameAndIdNot(String name, Long id);
    boolean existsByDescriptionAndIdNot(String description, Long id);
}
