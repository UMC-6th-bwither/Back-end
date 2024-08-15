package com.umc.bwither.user.controller;

import com.umc.bwither._base.apiPayLoad.ApiResponse;
import com.umc.bwither._base.apiPayLoad.code.status.SuccessStatus;
import com.umc.bwither._base.common.UserAuthorizationUtil;
import com.umc.bwither.animal.repository.WaitListRepository;
import com.umc.bwither.breeder.service.BreederService;
import com.umc.bwither.member.service.MemberService;
import com.umc.bwither.user.dto.BreederUpdateDTO;
import com.umc.bwither.user.dto.MemberUpdateDTO;
import com.umc.bwither.user.dto.UserInfoDTO;
import com.umc.bwither.user.service.MyPageService;
import com.umc.bwither.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class MyPageController {
    private final UserService userService;
    private final MyPageService myPageService;
    private final UserAuthorizationUtil userAuthorizationUtil;

    @GetMapping("")
    public ApiResponse<?> getUserInfo() {
        Long userId = userAuthorizationUtil.getCurrentUserId();;
        System.out.println("id: "+userId);
        UserInfoDTO userInfo = myPageService.getUserInfo(userId);
        System.out.println(userInfo);
        return ApiResponse.of(SuccessStatus.SUCCESS_GET_USERINFO, userInfo);
    }

    @GetMapping("/reservations")
    public ApiResponse<?> getUserReservation() {
        Long userId = userAuthorizationUtil.getCurrentUserId();
        Object userReservation = myPageService.getUserReservation(userId);
        return ApiResponse.of(SuccessStatus.SUCCESS_GET_USERRESERVATION, userReservation);
    }

    @PatchMapping("/breeder")
    public ApiResponse<?> updateBreederInfo(@RequestBody BreederUpdateDTO breederUpdateDTO) {
        Long userId = userAuthorizationUtil.getCurrentUserId();
        UserInfoDTO updatedUserInfo = myPageService.updateBreeder(userId, breederUpdateDTO);

        return ApiResponse.of(SuccessStatus.SUCCESS_UPDATE_BREEDER, updatedUserInfo);
    }

    @PatchMapping("/member")
    public ApiResponse<?> updateMemberInfo(@RequestBody MemberUpdateDTO memberUpdateDTO) {
        Long userId = userAuthorizationUtil.getCurrentUserId();
        UserInfoDTO updatedUserInfo = myPageService.updateMember(userId, memberUpdateDTO);

        return ApiResponse.of(SuccessStatus.SUCCESS_UPDATE_MEMBER, updatedUserInfo);
    }
}
