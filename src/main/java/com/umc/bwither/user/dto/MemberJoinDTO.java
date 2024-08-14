package com.umc.bwither.user.dto;

import com.umc.bwither.breeder.dto.BreedingHistoryDTO;
import com.umc.bwither.breeder.dto.CertificateDTO;
import com.umc.bwither.breeder.entity.enums.Animal;
import com.umc.bwither.member.entity.enums.EmploymentStatus;
import com.umc.bwither.member.entity.enums.FamilyAgreement;
import com.umc.bwither.member.entity.enums.FuturePlan;
import com.umc.bwither.member.entity.enums.PetAllowed;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MemberJoinDTO {
    private String token;
    // User
    private String userId;
    private String name;           // 이름
    private String phone;          // 전화번호
    private String email;          // 이메일
    private String username;       // 아이디
    private String password;       // 비밀번호

    private String zipcode;        // 우편번호
    private String address;        // 주소
    private String addressDetail;  // 상세 주소

    //Member
    private PetAllowed petAllowed;
    private String cohabitant;
    private FamilyAgreement familyAgreement;
    private EmploymentStatus employmentStatus;
    private String commuteTime;
    private Boolean petExperience;
    private String currentPet;
    private FuturePlan futurePlan;

}
