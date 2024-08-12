package com.umc.bwither.user.controller;

import com.umc.bwither._base.apiPayLoad.ApiResponse;
import com.umc.bwither._base.apiPayLoad.code.status.SuccessStatus;
import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.breeder.service.BreederService;
import com.umc.bwither.user.dto.BreederJoinDTO;
import com.umc.bwither.user.dto.LoginRequestDTO;
import com.umc.bwither.user.dto.LoginResponseDTO;
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

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final BreederService breederService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final TokenProvider tokenProvider;

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
                    .build();


            breederService.saveBreeder(breeder);

            return ResponseEntity.ok(ApiResponse.of(SuccessStatus.SUCCESS_JOIN_BREEDER, null));
        } catch (Exception e) {
            // 오류 발생 시 응답 생성
            return ResponseEntity.badRequest().body(ApiResponse.of(SuccessStatus.ERROR_JOIN_BREEDER, e.getMessage()));
        }
    }


    @PostMapping("/breeder/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequestDTO loginRequestDTO) {
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

                return ResponseEntity.ok(ApiResponse.of(SuccessStatus.SUCCESS_LOGIN_BREEDER, responseDTO));
            } else {
                return ResponseEntity
                        .badRequest()
                        .body(ApiResponse.of(SuccessStatus.ERROR_LOGIN_BREEDER, "잘못된 사용자 정보입니다"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.of(SuccessStatus.ERROR_LOGIN_BREEDER, e.getMessage()));
        }
    }
}
