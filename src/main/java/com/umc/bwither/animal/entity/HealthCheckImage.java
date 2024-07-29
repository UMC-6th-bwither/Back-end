package com.umc.bwither.animal.entity;

import com.umc.bwither._base.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class HealthCheckImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long healthCheckImageId;

    @ManyToOne
    @JoinColumn(name = "animal_parents_id", nullable = false)
    private AnimalParents animalParents;

    @ManyToOne
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String filePath;
}
