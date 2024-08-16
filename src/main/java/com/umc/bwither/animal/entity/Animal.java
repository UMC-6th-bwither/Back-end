package com.umc.bwither.animal.entity;

import com.umc.bwither._base.common.BaseEntity;
import com.umc.bwither.animal.dto.AnimalRequestDTO.AnimalCreateDTO;
import com.umc.bwither.animal.entity.enums.AnimalType;
import com.umc.bwither.animal.entity.enums.Gender;
import com.umc.bwither.animal.entity.enums.Status;
import com.umc.bwither.breeder.entity.Breeder;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Animal extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long animalId;

    @Column(nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnimalType type; // Enum 정의 필요

    @Column(nullable = false, length = 50)
    private String breed;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender; // Enum 정의 필요

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false, name = "`character`")
    private String character;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String feature;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String feeding;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String vaccination;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String virusCheck;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String parasitic;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String healthCheck;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status; // Enum 정의 필요

    @ManyToOne
    @JoinColumn(name = "breeder_id", nullable = false)
    private Breeder breeder;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnimalFile> animalFiles;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnimalParents> animalParents;

    @Column
    private Integer animalMemberCount;

    public Integer getAge() {
        return Period.between(this.birthDate, LocalDate.now()).getYears();
    }

    public void update(AnimalCreateDTO animalCreateDTO) {
        this.name = animalCreateDTO.getName();
        this.type = animalCreateDTO.getType();
        this.breed = animalCreateDTO.getBreed();
        this.gender = animalCreateDTO.getGender();
        this.birthDate = animalCreateDTO.getBirthDate();
        this.character = animalCreateDTO.getCharacter();
        this.feature = animalCreateDTO.getFeature();
        this.feeding = animalCreateDTO.getFeeding();
        this.vaccination = animalCreateDTO.getVaccination();
        this.virusCheck = animalCreateDTO.getVirusCheck();
        this.parasitic = animalCreateDTO.getParasitic();
        this.healthCheck = animalCreateDTO.getHealthCheck();
    }
}