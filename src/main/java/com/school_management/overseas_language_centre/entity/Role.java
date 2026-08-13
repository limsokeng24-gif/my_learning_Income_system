package com.school_management.overseas_language_centre.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;

@Entity //we need to create entity first, then we create repository for data access layer
@Table(name = "role")
//we use @Data for Auto generate /Getter & Setter........to String
@Data //this one is from lombok
// Getter , //Setter....toString
//eliminates boilerplate code
//reduce boilerplate code
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //use for auto increasment
    private Long id;

    @Column(unique = true, nullable = true)
    private String name;

    private String description;




//    public void setName(String name) {
//        this.name = name.trim();
//        this.description = name.trim();
//    }

//    private LocalDateTime createdDate;
//
//    private LocalDateTime updatedDate;



}
