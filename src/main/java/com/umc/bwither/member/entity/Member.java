package com.umc.bwither.member.entity;

import com.umc.bwither.member.entity.enums.EmploymentStatus;
import com.umc.bwither.member.entity.enums.FamilyAgreement;
import com.umc.bwither.member.entity.enums.FuturePlan;
import com.umc.bwither.member.entity.enums.PetAllowed;
import com.umc.bwither.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
//@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {

    @Id
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetAllowed petAllowed;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String cohabitant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FamilyAgreement familyAgreement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmploymentStatus employmentStatus;

    @Column(length = 50)
    private String commuteTime;

    @Column
    private Boolean petExperience;

    @Column(columnDefinition = "TEXT")
    private String currentPet;

    @Enumerated(EnumType.STRING)
    private FuturePlan futurePlan;
}

