package com.umc.bwither.user.controller;

import com.umc.bwither._base.apiPayLoad.ApiResponse;
import com.umc.bwither._base.apiPayLoad.code.status.SuccessStatus;
import com.umc.bwither.animal.entity.AnimalFile;
import com.umc.bwither.animal.service.S3Uploader;
import com.umc.bwither.breeder.dto.BreedingCareerDTO;
import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.breeder.entity.BreederFile;
import com.umc.bwither.breeder.entity.Breeding;
import com.umc.bwither.breeder.entity.enums.FileType;
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
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final BreederService breederService;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final TokenProvider tokenProvider;
    private final S3Uploader s3Uploader;

    @PostMapping(value = "/breeder/join", consumes = "multipart/form-data")
    @Operation(summary = "브리더 회원가입 API", description = "브리더 회원가입 API")
    public ResponseEntity<?> registerUser(
            @ModelAttribute BreederJoinDTO breederJoinDTO,
            @RequestPart(value = "registrationImage", required = false) MultipartFile registrationImage,
            @RequestPart(value = "certificateImages", required = false) List<MultipartFile> certificateImages,
            @RequestPart(value = "kennelImages", required = false) List<MultipartFile> kennelImages,
            @RequestParam Map<String, String> breedingCareerDTOs
    ) {
        try {
            if (breederJoinDTO == null || breederJoinDTO.getPassword() == null) {
                throw new RuntimeException("비밀번호가 입력되지 않았습니다.");
            }

            // 브리더 파일
            Map<FileType, List<MultipartFile>> breederFiles = new HashMap<>();
            breederFiles.put(FileType.REGISTRATION, registrationImage != null ? List.of(registrationImage) : List.of());
            breederFiles.put(FileType.CERTIFICATE, certificateImages);
            breederFiles.put(FileType.KENNEL, kennelImages);

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

            // Breeding 객체 생성 및 초기화
            for (int i = 0; breedingCareerDTOs.containsKey("breedingCareerDTOs[" + i + "].breedingTradeName"); i++) {
                String tradeName = breedingCareerDTOs.get("breedingCareerDTOs[" + i + "].breedingTradeName");
                String joinDateStr = breedingCareerDTOs.get("breedingCareerDTOs[" + i + "].breedingJoinDate");
                String leaveDateStr = breedingCareerDTOs.get("breedingCareerDTOs[" + i + "].breedingLeaveDate");
                Boolean currentlyEmployed = Boolean.parseBoolean(breedingCareerDTOs.get("breedingCareerDTOs[" + i + "].breedingCurrentlyEmployed"));

                LocalDate joinDate = (joinDateStr != null && !joinDateStr.isEmpty()) ? LocalDate.parse(joinDateStr + "-01") : null;
                LocalDate leaveDate = (leaveDateStr != null && !leaveDateStr.isEmpty()) ? LocalDate.parse(leaveDateStr + "-01") : null;

                Breeding breeding = Breeding.builder()
                        .tradeName(tradeName)
                        .joinDate(joinDate)
                        .leaveDate(leaveDate)
                        .currentlyEmployed(currentlyEmployed)
                        .breeder(breeder)
                        .build();

                breederService.saveBreeding(breeding);
            }

            // BreederFile 객체 생성 및 초기화
            if (breederFiles != null) {
                for (Map.Entry<com.umc.bwither.breeder.entity.enums.FileType, List<MultipartFile>> entry : breederFiles.entrySet()) {
                    com.umc.bwither.breeder.entity.enums.FileType fileType = entry.getKey();
                    List<MultipartFile> files = entry.getValue();
                    if (files != null && !files.isEmpty()) {
                        for (MultipartFile file : files) {
                            String filePath = s3Uploader.uploadFile("breeder-files", file);
                            BreederFile breederFile = BreederFile.builder()
                                    .type(fileType)
                                    .breederFilePath(filePath)
                                    .breeder(breeder)
                                    .build();
                            breederService.saveBreederFile(breederFile);
                        }
                    }
                }
            }

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
                        .userId(user.getUserId())
                        .username(user.getUsername())
                        .token(token)
                        .role(user.getRole())
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