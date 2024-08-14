package com.umc.bwither.breeder.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BreedingCareerDTO {
    private String breedingTradeName;      // 상호명

    private String breedingJoinDate;       // 입사 연월 (예: YYYY-MM)

    private String breedingLeaveDate;     // 퇴사 연월 (예: YYYY-MM)

    private Boolean breedingCurrentlyEmployed; // 재직 중 여부
}
