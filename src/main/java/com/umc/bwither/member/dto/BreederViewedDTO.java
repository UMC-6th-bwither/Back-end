package com.umc.bwither.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BreederViewedDTO {
    private Long breederId;
    private String breederName;
    private LocalDateTime viewedAt;

}
