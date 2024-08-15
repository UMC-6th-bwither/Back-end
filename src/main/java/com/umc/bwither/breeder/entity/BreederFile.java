package com.umc.bwither.breeder.entity;

import com.umc.bwither._base.common.BaseEntity;
import com.umc.bwither.breeder.entity.enums.FileType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BreederFile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long breederFileId;

    @ManyToOne
    @JoinColumn(name = "breeder_id", nullable = false)
    private Breeder breeder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileType type; // Enum 정의 필요

    @Column(nullable = false, columnDefinition = "TEXT")
    private String breederFilePath;
}
