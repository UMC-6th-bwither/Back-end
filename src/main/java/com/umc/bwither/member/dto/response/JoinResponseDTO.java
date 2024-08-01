package com.umc.bwither.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JoinResponseDTO {

    private Long userId;
    private String name;
    private LocalDateTime createAt;
}
