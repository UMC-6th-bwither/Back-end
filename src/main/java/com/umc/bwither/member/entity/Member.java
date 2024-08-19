package com.umc.bwither.member.entity;

import com.umc.bwither.member.entity.enums.EmploymentStatus;
import com.umc.bwither.member.entity.enums.FamilyAgreement;
import com.umc.bwither.member.entity.enums.FuturePlan;
import com.umc.bwither.member.entity.enums.PetAllowed;
import com.umc.bwither.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetAllowed petAllowed;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String cohabitant;

    @Column(nullable = false)
    private Long cohabitantCount;

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

    public Member(Long memberId) {
        this.memberId = memberId;
    }

}

