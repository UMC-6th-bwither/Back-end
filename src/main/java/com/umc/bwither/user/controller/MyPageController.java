package com.umc.bwither.user.controller;

import com.umc.bwither._base.apiPayLoad.ApiResponse;
import com.umc.bwither._base.apiPayLoad.code.status.SuccessStatus;
import com.umc.bwither._base.common.UserAuthorizationUtil;
import com.umc.bwither.animal.service.S3Uploader;
import com.umc.bwither.user.dto.BreederInfoUpdateDTO;
import com.umc.bwither.user.dto.BreederProfileUpdateDTO;
import com.umc.bwither.user.dto.MemberUpdateDTO;
import com.umc.bwither.user.dto.UserInfoDTO;
import com.umc.bwither.user.service.MyPageService;
import com.umc.bwither.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class MyPageController {
    private final UserService userService;
    private final MyPageService myPageService;
    private final UserAuthorizationUtil userAuthorizationUtil;
    private final S3Uploader s3Uploader;

    @GetMapping("/user")
    public ApiResponse<?> getUserInfo() {
        Long userId = userAuthorizationUtil.getCurrentUserId();
        System.out.println("id: "+userId);
        UserInfoDTO userInfo = myPageService.getUserInfo(userId);
        System.out.println(userInfo);
        return ApiResponse.of(SuccessStatus.SUCCESS_GET_USERINFO, userInfo);
    }

    @GetMapping("/user/reservations")
    public ApiResponse<?> getUserReservation() {
        Long userId = userAuthorizationUtil.getCurrentUserId();
        Object userReservation = myPageService.getUserReservation(userId);
        return ApiResponse.of(SuccessStatus.SUCCESS_GET_USERRESERVATION, userReservation);
    }

    @PatchMapping(value = "/breeder/profile", consumes = "multipart/form-data")
    public ApiResponse<?> updateProfile(@RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
                                        @RequestPart(value = "password") String password) {
        Long userId = userAuthorizationUtil.getCurrentUserId();

        // 프로필 이미지 파일을 S3에 업로드
        String profileImageUrl = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            profileImageUrl = s3Uploader.uploadFile("breeder-profile-images", profileImage);
        }

        // BreederProfileUpdateDTO 객체 생성 및 값 설정
        BreederProfileUpdateDTO breederProfileUpdateDTO = BreederProfileUpdateDTO.builder()
                .profileImage(profileImageUrl)
                .password(password)
                .build();

        UserInfoDTO updatedUserInfo = myPageService.updateBreederProfile(userId, breederProfileUpdateDTO);

        return ApiResponse.of(SuccessStatus.SUCCESS_UPDATE_BREEDERPROFILE, updatedUserInfo);
    }

    @PatchMapping("/breeder/info")
    public ApiResponse<?> updateBreederInfo(@RequestBody BreederInfoUpdateDTO breederInfoUpdateDTO) {
        Long userId = userAuthorizationUtil.getCurrentUserId();
        UserInfoDTO updatedUserInfo = myPageService.updateBreederInfo(userId, breederInfoUpdateDTO);

        return ApiResponse.of(SuccessStatus.SUCCESS_UPDATE_BREEDERINFO, updatedUserInfo);
    }

    @PatchMapping("/user/member")
    public ApiResponse<?> updateMemberInfo(@RequestBody MemberUpdateDTO memberUpdateDTO) {
        Long userId = userAuthorizationUtil.getCurrentUserId();
        UserInfoDTO updatedUserInfo = myPageService.updateMember(userId, memberUpdateDTO);

        return ApiResponse.of(SuccessStatus.SUCCESS_UPDATE_MEMBER, updatedUserInfo);
    }

    @PostMapping("/user/recent-animals")
    public ApiResponse<?> viewAnimal(@RequestParam Long animalId,
                                             HttpServletRequest request,
                                             HttpServletResponse response) {
        Long userId = userAuthorizationUtil.getCurrentUserId();

        // 동물 조회 기록을 저장
        myPageService.saveAnimalView(userId, animalId, request, response);

        return ApiResponse.of(SuccessStatus.SUCCESS_SAVE_RECENTANIMAL, animalId);
    }

    @GetMapping("/user/recent-animals")
    public ApiResponse<List<Long>> getRecentViews(HttpServletRequest request) {
        Long userId = userAuthorizationUtil.getCurrentUserId();

        // 최근 본 동물 ID 목록 반환
        List<Long> recentAnimalIds = myPageService.getRecentViews(userId, request);

        return ApiResponse.of(SuccessStatus.SUCCESS_GET_RECENTANIMAL, recentAnimalIds);
    }
}
