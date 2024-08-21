package com.umc.bwither.user.dto;

import com.umc.bwither.user.entity.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDTO {
    private Long userId;
    private Long breederId;
    private String username;
    private String token;
    private Role role;
}