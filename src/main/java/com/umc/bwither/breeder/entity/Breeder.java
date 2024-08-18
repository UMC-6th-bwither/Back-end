package com.umc.bwither.breeder.entity;

import com.umc.bwither._base.common.BaseEntity;
import com.umc.bwither.animal.entity.AnimalFile;
import com.umc.bwither.breeder.entity.enums.AnimalType;
import com.umc.bwither.breeder.entity.enums.EmploymentStatus;
import com.umc.bwither.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Breeder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long breederId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnimalType animal;

    @ElementCollection
    @CollectionTable(name = "breeder_species", joinColumns = @JoinColumn(name = "breeder_id"))
    @Column(name = "species", nullable = false)
    private List<String> species;

    @Column(length = 255)
    private String backgroundImage;

    @Column(nullable = false, length = 50)
    private String tradeName;

    @Column(nullable = false, length = 50)
    private String tradePhone;

    @Column(length = 50)
    private String contactableTime;

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

    //    @Column(nullable = false)
    @Column
    private Integer trustLevel = 5;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String descriptionDetail;

    @Column(length = 50)
    private String schoolName;

    @Column(length = 50)
    private String departmentName;

    @Column
    private LocalDate enrollmentDate;

    @Column
    private LocalDate graduationDate;

    @Column(length = 50)
    private String businessTime;

    @Column(columnDefinition = "TEXT")
    private String questionGuarantee;

    @Column(columnDefinition = "TEXT")
    private String questionPedigree;

    @Column(columnDefinition = "TEXT")
    private String questionBaby;

    @Column(columnDefinition = "TEXT")
    private String questionPeriod;

    @Column(columnDefinition = "TEXT")
    private String questionSupport;

    @OneToMany(mappedBy = "breeder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BreederFile> breederFiles;

    @OneToMany(mappedBy = "breeder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Breeding> breedingCareer;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Column
    private Double averageRating;
    public Breeder(Long breederId) {
        this.breederId = breederId;
    }
}