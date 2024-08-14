package com.umc.bwither.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {
    private UserDTO userDTO;
    private BreederDTO breederDTO;
    private MemberDTO memberDTO;
}
