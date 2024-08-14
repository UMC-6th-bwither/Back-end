package com.umc.bwither.animal.entity;

import com.umc.bwither._base.common.BaseEntity;
import com.umc.bwither.animal.entity.enums.ParentType;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AnimalParents extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long animalParentsId;

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
    private LocalDate birthDate;

    @Column(nullable = false, length = 50)
    private String hereditary;

    @Column(nullable = false, columnDefinition = "TEXT", name = "`character`")
    private String character;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String healthCheck;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String imageUrl;

    @OneToMany(mappedBy = "animalParents", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HealthCheckImage> healthCheckImages;

    public void update(String name, String breed, LocalDate birthDate, String hereditary, String character, String healthCheck, String imageUrl) {
        this.name = name;
        this.breed = breed;
        this.birthDate = birthDate;
        this.hereditary = hereditary;
        this.character = character;
        this.healthCheck = healthCheck;
        if (imageUrl != null) {
            this.imageUrl = imageUrl;
        }
    }
}

