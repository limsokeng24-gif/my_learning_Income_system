package com.school_management.overseas_language_centre.feature.core.user.repository;

import com.school_management.overseas_language_centre.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
//    boolean existsByNameAndIdNot(String name, Long id);
}
