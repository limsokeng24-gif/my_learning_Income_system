package com.school_management.overseas_language_centre.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(length = 100)
    private String nickName;

    @Column(nullable = false)
    private String passwordHash;

    private Boolean enabled;

    @Column(name = "profile_image_key")
    private String profileImageKey;

}
