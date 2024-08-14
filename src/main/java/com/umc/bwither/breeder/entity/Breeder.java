package com.umc.bwither.breeder.entity;

import com.umc.bwither.breeder.entity.enums.Animal;
import com.umc.bwither.breeder.entity.enums.EmploymentStatus;
import com.umc.bwither.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
//@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Breeder{
    @Id
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Animal animal;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String species;

    @Column(nullable = false, length = 50)
    private String tradeName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String representative;

    @Column(nullable = false, length = 20)
    private String registrationNumber;

    @Column(nullable = false, length = 100)
    private String licenseNumber;

    @Column(nullable = false, length = 100)
    private String snsAddress;

    @Column(nullable = false, length = 100)
    private String animalHospital;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmploymentStatus employmentStatus;

    @Column(nullable = false)
    private Integer trustLevel;

    @Column(columnDefinition = "TEXT")
    private String description;
}

