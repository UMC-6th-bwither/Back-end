package com.umc.bwither.animal.entity;

import com.umc.bwither._base.common.BaseEntity;
import com.umc.bwither.animal.entity.enums.ParentType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AnimalParents extends BaseEntity {
    @Id
    @Column(length = 255)
    private String animalParentsId;

    @ManyToOne
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParentType type;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String breed;

    @Column(nullable = false)
    private Date birthDate;

    @Column(nullable = false, length = 50)
    private String hereditary;

    @Column(nullable = false, columnDefinition = "TEXT", name = "`character`")
    private String character;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String healthCheck;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String imageUrl;
}

