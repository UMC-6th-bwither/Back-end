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
import com.umc.bwither._base.apiPayLoad.code.status.SuccessStatus;
import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.breeder.service.BreederService;
import com.umc.bwither.member.entity.Member;
import com.umc.bwither.member.entity.enums.EmploymentStatus;
import com.umc.bwither.member.entity.enums.FamilyAgreement;
import com.umc.bwither.member.entity.enums.FuturePlan;
import com.umc.bwither.member.entity.enums.PetAllowed;
import com.umc.bwither.member.service.MemberService;
import com.umc.bwither.user.dto.BreederJoinDTO;
import com.umc.bwither.user.dto.LoginRequestDTO;
import com.umc.bwither.user.dto.LoginResponseDTO;
import com.umc.bwither.user.dto.MemberJoinDTO;
import com.umc.bwither.user.entity.User;
import com.umc.bwither.user.entity.enums.Role;
import com.umc.bwither.user.entity.enums.Status;
import com.umc.bwither.user.security.TokenProvider;
import com.umc.bwither.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.umc.bwither._base.common.UserAuthorizationUtil.getCurrentUserId;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final BreederService breederService;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final TokenProvider tokenProvider;

    @GetMapping("/{userId}")
    public ApiResponse<?> getUserInfo() {
        Long userId = getCurrentUserId();
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
    public ApiResponse<?> updateUserInfo(@RequestBody UserInfoDTO requestDTO) {
        Long userId = getCurrentUserId();
        System.out.println("컨토를러1" + requestDTO);
        UserInfoDTO updatedUserInfo = userService.updateUserInfo(
                userId,
                requestDTO.getUserDTO(),
                requestDTO.getBreederDTO(),
                requestDTO.getMemberDTO());
        System.out.println("컨토를러2" + updatedUserInfo);

        return ApiResponse.onSuccess(updatedUserInfo);
    }


    @PostMapping("/breeder/join")
    public ResponseEntity<?> registerUser(@RequestBody BreederJoinDTO breederJoinDTO) {
        try {
            if (breederJoinDTO == null || breederJoinDTO.getPassword() == null) {
                throw new RuntimeException("비밀번호가 입력되지 않았습니다.");
            }
            User user = User.builder()
                    .name(breederJoinDTO.getName())
                    .phone(breederJoinDTO.getPhone())
                    .email(breederJoinDTO.getEmail())
                    .username(breederJoinDTO.getUsername())
                    .password(passwordEncoder.encode(breederJoinDTO.getPassword())) // 비밀번호 암호화
                    .zipcode(breederJoinDTO.getZipcode())
                    .address(breederJoinDTO.getAddress())
                    .addressDetail(breederJoinDTO.getAddressDetail())
                    .role(Role.BREEDER) // 브리더로 설정
                    .status(Status.ACTIVE) // 상태를 ACTIVE로 설정
                    .build();

            User savedUser = userService.create(user);

            // Breeder 객체 생성 및 초기화
            Breeder breeder = Breeder.builder()
                    .user(savedUser)
                    .animal(breederJoinDTO.getAnimal())
                    .species(breederJoinDTO.getSpecies())
                    .tradeName(breederJoinDTO.getTradeName())
                    .tradePhone(breederJoinDTO.getTradePhone())
                    .tradeEmail(breederJoinDTO.getTradeEmail())
                    .representative(breederJoinDTO.getRepresentative())
                    .registrationNumber(breederJoinDTO.getRegistrationNumber())
                    .licenseNumber(breederJoinDTO.getLicenseNumber())
                    .snsAddress(breederJoinDTO.getSnsAddress())
                    .animalHospital(breederJoinDTO.getAnimalHospital())
                    .employmentStatus(breederJoinDTO.getEmploymentStatus())
                    .trustLevel(5)
                    .build();


            breederService.saveBreeder(breeder);

            return ResponseEntity.ok(ApiResponse.of(SuccessStatus.SUCCESS_JOIN_BREEDER, null));
        } catch (Exception e) {
            // 오류 발생 시 응답 생성
            return ResponseEntity.badRequest().body(ApiResponse.of(SuccessStatus.ERROR_JOIN_BREEDER, e.getMessage()));
        }
    }

    @PostMapping("/user/join")
    public ResponseEntity<?> JoinMember(@RequestBody MemberJoinDTO memberJoinDTO) {
        try {
            if (memberJoinDTO == null || memberJoinDTO.getPassword() == null) {
                throw new RuntimeException("비밀번호가 입력되지 않았습니다.");
            }
            User user = User.builder()
                    .name(memberJoinDTO.getName())
                    .phone(memberJoinDTO.getPhone())
                    .email(memberJoinDTO.getEmail())
                    .username(memberJoinDTO.getUsername())
                    .password(passwordEncoder.encode(memberJoinDTO.getPassword())) // 비밀번호 암호화
                    .zipcode(memberJoinDTO.getZipcode())
                    .address(memberJoinDTO.getAddress())
                    .addressDetail(memberJoinDTO.getAddressDetail())
                    .role(Role.MEMBER) // 브리더로 설정
                    .status(Status.ACTIVE) // 상태를 ACTIVE로 설정
                    .build();

            User savedUser = userService.create(user);


            // Member 객체 생성 및 초기화
            Member member = Member.builder()
                    .user(savedUser)
                    .petAllowed(memberJoinDTO.getPetAllowed())
                    .cohabitant(memberJoinDTO.getCohabitant())
                    .familyAgreement(memberJoinDTO.getFamilyAgreement())
                    .employmentStatus(memberJoinDTO.getEmploymentStatus())
                    .commuteTime(memberJoinDTO.getCommuteTime())
                    .petExperience(memberJoinDTO.getPetExperience())
                    .currentPet(memberJoinDTO.getCurrentPet())
                    .futurePlan(memberJoinDTO.getFuturePlan())
                    .build();


            memberService.saveMember(member);

            return ResponseEntity.ok(ApiResponse.of(SuccessStatus.SUCCESS_JOIN_MEMBER, null));
        } catch (Exception e) {
            // 오류 발생 시 응답 생성
            return ResponseEntity.badRequest().body(ApiResponse.of(SuccessStatus.ERROR_JOIN_MEMBER, e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> LoginUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            User user = userService.getByCredentials(
                    loginRequestDTO.getUsername(),
                    loginRequestDTO.getPassword(),
                    passwordEncoder);

            if (user != null) {
                // 토큰 생성
                final String token = tokenProvider.create(user);
                final LoginResponseDTO responseDTO = LoginResponseDTO.builder()
                        .username(user.getUsername())
                        .token(token)
                        .build();

                return ResponseEntity.ok(ApiResponse.of(SuccessStatus.SUCCESS_LOGIN_USER, responseDTO));
            } else {
                return ResponseEntity
                        .badRequest()
                        .body(ApiResponse.of(SuccessStatus.ERROR_LOGIN_USER, "잘못된 사용자 정보입니다"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.of(SuccessStatus.ERROR_LOGIN_USER, e.getMessage()));
        }
    }
}
