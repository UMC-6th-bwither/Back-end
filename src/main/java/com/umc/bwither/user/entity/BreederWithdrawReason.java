package com.umc.bwither.user.entity;

import com.umc.bwither.user.entity.enums.BreederReasonType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BreederWithdrawReason {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BreederReasonType reasonType; // 브리더 탈퇴 이유

    @Column(length = 255)
    private String additionalComment;
}
