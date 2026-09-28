package com.school_management.overseas_language_centre.feature.core.otp.repository;

import com.school_management.overseas_language_centre.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findTopByUserIdOrderByIdDesc(Long userId);

}
