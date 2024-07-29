package com.umc.bwither.animal.entity;

import com.umc.bwither._base.common.BaseEntity;
import com.umc.bwither.animal.entity.enums.FileType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AnimalFile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long animalFileId;

    @ManyToOne
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileType type; // Enum 정의 필요

    @Column(nullable = false, columnDefinition = "TEXT")
    private String animalFilePath;
}

