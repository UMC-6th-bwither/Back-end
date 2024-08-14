package com.umc.bwither.user.service;

import com.umc.bwither.user.dto.BreederDTO;
import com.umc.bwither.user.dto.MemberDTO;
import com.umc.bwither.user.dto.UserDTO;
import com.umc.bwither.user.dto.UserInfoDTO;
import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

public interface UserService {
    Object getUserInfo(Long userId);
    UserInfoDTO updateUserInfo(Long userId, UserDTO userDto, BreederDTO breederDto, MemberDTO memberDto);
    User create(User user);
    User getByCredentials(String username, String password, PasswordEncoder encoder);

}
