package com.umc.bwither.breeder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BreedingCareerDTO {
    private String tradeName;      // 상호명

    private String joinDate;       // 입사 연월 (예: YYYY-MM)

    private String leaveDate;     // 퇴사 연월 (예: YYYY-MM)

    private Boolean currentlyEmployed; // 재직 중 여부

    private String description; // 설명

}
