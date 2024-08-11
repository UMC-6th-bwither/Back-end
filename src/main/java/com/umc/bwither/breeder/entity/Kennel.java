package com.umc.bwither.breeder.entity;

import com.umc.bwither._base.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Kennel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kennelId;

    @Column(nullable = false)
    private String kennelFile;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Breeder breeder;
}