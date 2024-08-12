package com.umc.bwither.user.dto;

import com.umc.bwither.user.entity.enums.Role;
import com.umc.bwither.user.entity.enums.Status;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String name;
    private String phone;
    private String email;
    private String username;
    private String zipcode;
    private String address;
    private String addressDetail;
    private String profileImage;
    private Role role;
    private Status status;
}

