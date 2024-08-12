package com.umc.bwither.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequestDTO {
    private String username;
    private String password;
}
