package com.umc.bwither.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BreedingJoinDTO {

    private String tradeName;      // 상호명

    private LocalDate joinDate;       // 입사 연월 (예: YYYY-MM-DD)

    private LocalDate leaveDate;     // 퇴사 연월 (예: YYYY-MM-DD)

    private Boolean currentlyEmployed; // 재직 중 여부

}
