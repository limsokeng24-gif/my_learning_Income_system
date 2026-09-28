package com.school_management.overseas_language_centre.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "otp")
@NoArgsConstructor
@AllArgsConstructor
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable = false, unique = true)
    private Long userId;

    @Column( length = 512)
    private String otpEncrypted;

    @Column
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private Integer sentCount = 0;

    @Column
    private LocalDateTime lastSentAt;

    @Column(nullable = false)
    private Boolean verified = false;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

}
