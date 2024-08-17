package com.umc.bwither._base.aws.s3;

import com.umc.bwither._base.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity @Builder @Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Uuid extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String uuid;
}