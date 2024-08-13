package com.umc.bwither.breeder.entity;

import com.umc.bwither._base.common.BaseEntity;
import com.umc.bwither.animal.entity.AnimalFile;
import com.umc.bwither.breeder.entity.enums.Animal;
import com.umc.bwither.breeder.entity.enums.EmploymentStatus;
import com.umc.bwither.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Breeder {
    @Id
    private Long breederId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "breeder_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Animal animal;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String species;

    @Column(nullable = false, length = 50)
    private String tradeName;

    @Column(nullable = false, length = 50)
    private String tradePhone;

    @Column(nullable = false, length = 50)
    private String tradeEmail;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String representative;

    @Column(nullable = false, length = 20)
    private String registrationNumber;

    @Column(nullable = false, length = 100)
    private String licenseNumber;

    @Column(length = 100)
    private String snsAddress;

    @Column(length = 100)
    private String animalHospital;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmploymentStatus employmentStatus;

    @Column(nullable = false)
    private Integer trustLevel = 5;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "breeder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BreederFile> breederFiles;

    @OneToMany(mappedBy = "breeder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Breeding> breedingCareer;

}