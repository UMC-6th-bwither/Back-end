package com.umc.bwither.member.dto.request;

import com.umc.bwither.member.entity.enums.EmploymentStatus;
import com.umc.bwither.member.entity.enums.FamilyAgreement;
import com.umc.bwither.member.entity.enums.FuturePlan;
import com.umc.bwither.member.entity.enums.PetAllowed;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRequestDTO {

    private String phone;
    private String password;
    private String zipcode;
    private String address;
    private String addressDetail;
    private String profileImage;

    private PetAllowed petAllowed;
    private String cohabitant;
    private FamilyAgreement familyAgreement;
    private EmploymentStatus employmentStatus;
    private String commuteTime;
    private Boolean petExperience;
    private String currentPet;
    private FuturePlan futurePlan;
}