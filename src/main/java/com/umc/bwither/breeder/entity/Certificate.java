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
public class Certificate extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long certificateId;

    @Column(length = 100)
    private String certificateName;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Breeder breeder;
}

