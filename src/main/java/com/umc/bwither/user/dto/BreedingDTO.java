package com.umc.bwither.user.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BreedingDTO {
    private String tradeName; // 기관명
    private LocalDate joinDate; // 입사연월
    private LocalDate leaveDate; // 퇴사연월

    private Boolean currentlyEmployed;
    private String description; // 경력 설명
}
