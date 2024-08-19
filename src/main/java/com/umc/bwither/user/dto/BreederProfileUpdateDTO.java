package com.umc.bwither.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BreederProfileUpdateDTO {
    private String profileImage;
    private String password;
}
