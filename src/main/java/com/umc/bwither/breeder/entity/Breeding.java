package com.umc.bwither.breeder.entity;

import com.umc.bwither._base.common.BaseEntity;
import com.umc.bwither.breeder.entity.enums.EmploymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Breeding extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long breedingId;

    @Column(nullable = false, length = 50)
    private String tradeName;

    @Column(nullable = false)
    private LocalDate joinDate;

    @Column
    private LocalDate leaveDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmploymentStatus currentlyEmployed;  // 재직 중 여부

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "breeder_id", nullable = false)
    private Breeder breeder;
}