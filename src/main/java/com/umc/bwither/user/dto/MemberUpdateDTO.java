package com.umc.bwither.user.dto;

import com.umc.bwither.member.entity.enums.EmploymentStatus;
import com.umc.bwither.member.entity.enums.FamilyAgreement;
import com.umc.bwither.member.entity.enums.FuturePlan;
import com.umc.bwither.member.entity.enums.PetAllowed;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateDTO {
    // User
    private String profileImage;
    private String password;
    private String zipcode;
    private String address;
    private String addressDetail;

    // Member
    private PetAllowed petAllowed;
    private String cohabitant;
    private FamilyAgreement familyAgreement;
    private EmploymentStatus employmentStatus;
    private String commuteTime;
    private Boolean petExperience;
    private String currentPet;
    private FuturePlan futurePlan;
}
