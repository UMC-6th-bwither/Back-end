package com.umc.bwither.user.service;

import com.umc.bwither.user.dto.BreederDTO;
import com.umc.bwither.user.dto.MemberDTO;
import com.umc.bwither.user.dto.UserDTO;
import com.umc.bwither.user.dto.UserInfoDTO;

import java.util.Map;

public interface UserService {
    Object getUserInfo(Long userId);
    UserInfoDTO updateUserInfo(Long userId, UserDTO userDto, BreederDTO breederDto, MemberDTO memberDto);
}
