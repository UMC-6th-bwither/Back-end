package com.umc.bwither.user.controller;

import com.umc.bwither._base.apiPayLoad.ApiResponse;
import com.umc.bwither.user.dto.BreederDTO;
import com.umc.bwither.user.dto.MemberDTO;
import com.umc.bwither.user.dto.UserDTO;
import com.umc.bwither.user.dto.UserInfoDTO;
import com.umc.bwither.user.entity.User;
import com.umc.bwither.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public ApiResponse<?> getUserInfo(@PathVariable Long userId) {
        Object userInfo = userService.getUserInfo(userId);
        return ApiResponse.onSuccess(userInfo);
    }

//    @PatchMapping("/{userId}")
//    public ApiResponse<?> updateUserInfo(@PathVariable Long userId,
//                                         @RequestBody @Validated UserDTO userDTO,
//                                         @RequestBody(required = false) BreederDTO breederDTO,
//                                         @RequestBody(required = false) MemberDTO memberDTO) {
//        userService.updateUserInfo(userId, userDTO, breederDTO, memberDTO);
//        return ApiResponse.onSuccess(userId);
//    }
    @PatchMapping("/{userId}")
    public ApiResponse<?> updateUserInfo(@PathVariable Long userId,
                                         @RequestBody UserInfoDTO requestDTO) {
        System.out.println("컨토를러1"+requestDTO);
        UserInfoDTO updatedUserInfo = userService.updateUserInfo(
                userId,
                requestDTO.getUserDTO(),
                requestDTO.getBreederDTO(),
                requestDTO.getMemberDTO());
        System.out.println("컨토를러2"+updatedUserInfo);

        return ApiResponse.onSuccess(updatedUserInfo);
    }
}
