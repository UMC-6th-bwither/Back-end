package com.umc.bwither.breeder.entity;

import com.umc.bwither._base.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

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

    @Column
    private Date joinDate;

    @Column
    private Date leaveDate;

    @Column(nullable = false)
    private Boolean currentlyEmployed;  // 재직 중 여부

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Breeder breeder;
}
