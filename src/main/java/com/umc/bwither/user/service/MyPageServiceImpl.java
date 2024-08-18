package com.umc.bwither.user.service;

import com.umc.bwither.animal.repository.AnimalRepository;
import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.breeder.entity.BreederFile;
import com.umc.bwither.breeder.entity.Breeding;
import com.umc.bwither.breeder.repository.BreederFileRepository;
import com.umc.bwither.breeder.repository.BreederRepository;
import com.umc.bwither.breeder.repository.BreedingRepository;
import com.umc.bwither.member.entity.Member;
import com.umc.bwither.member.repository.MemberRepository;
import com.umc.bwither.user.dto.*;
import com.umc.bwither.user.entity.User;
import com.umc.bwither.user.entity.enums.Role;
import com.umc.bwither.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService{
    private final UserRepository userRepository;
    private final BreederRepository breederRepository;
    private final MemberRepository memberRepository;
    private final BreedingRepository breedingRepository;
    private final BreederFileRepository breederFileRepository;
    private final AnimalRepository animalRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserInfoDTO getUserInfo(Long userId) {
        // 사용자 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 사용자가 존재하지 않습니다."));

        // UserDTO 설정
        UserDTO userDTO = UserDTO.builder()
                .name(user.getName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .username(user.getUsername())
                .zipcode(user.getZipcode())
                .address(user.getAddress())
                .addressDetail(user.getAddressDetail())
                .profileImage(user.getProfileImage())
                .role(user.getRole())
                .status(user.getStatus())
                .build();

        // UserInfoDTO 초기화
        UserInfoDTO userInfoDTO = UserInfoDTO.builder()
                .userDTO(userDTO)
                .build();

        // 사용자 역할에 따라 BreederDTO 또는 MemberDTO 설정
        if (user.getRole() == Role.BREEDER) {
            // Breeder 정보 조회
            Breeder breeder = breederRepository.findByUser_UserId(userId)
                    .orElseThrow(() -> new RuntimeException("해당 브리더가 존재하지 않습니다."));

            // BreederDTO 설정
            BreederDTO breederDTO = BreederDTO.builder()
                    .animal(breeder.getAnimal())
                    .species(breeder.getSpecies()) // 이미 리스트이므로 toString() 호출 불필요
                    .backgroundImage(breeder.getBackgroundImage())
                    .tradeName(breeder.getTradeName())
                    .tradePhone(breeder.getTradePhone())
                    .contactableTime(breeder.getContactableTime())
                    .tradeEmail(breeder.getTradeEmail())
                    .representative(breeder.getRepresentative())
                    .registrationNumber(breeder.getRegistrationNumber())
                    .licenseNumber(breeder.getLicenseNumber())
                    .snsAddress(breeder.getSnsAddress())
                    .animalHospital(breeder.getAnimalHospital())
                    .employmentStatus(breeder.getEmploymentStatus())
                    .trustLevel(breeder.getTrustLevel())
                    .description(breeder.getDescription())
                    .descriptionDetail(breeder.getDescriptionDetail())
                    .schoolName(breeder.getSchoolName())
                    .departmentName(breeder.getDepartmentName())
                    .enrollmentDate(breeder.getEnrollmentDate())
                    .businessTime(breeder.getBusinessTime())
                    .graduationDate(breeder.getGraduationDate())
                    .questionGuarantee(breeder.getQuestionGuarantee())
                    .questionPedigree(breeder.getQuestionPedigree())
                    .questionBaby(breeder.getQuestionBaby())
                    .questionPeriod(breeder.getQuestionPeriod())
                    .questionSupport(breeder.getQuestionSupport())
                    .breeding(breeder.getBreedingCareer() != null ? breeder.getBreedingCareer().stream()
                            .map(breeding -> BreedingDTO.builder()
                                    .breedingId(breeding.getBreedingId())
                                    .tradeName(breeding.getTradeName())
                                    .joinDate(breeding.getJoinDate())
                                    .leaveDate(breeding.getLeaveDate())
                                    .currentlyEmployed(breeding.getCurrentlyEmployed())
                                    .description(breeding.getDescription())
                                    .build())
                            .collect(Collectors.toList()) : null)
                    .breederFiles(breeder.getBreederFiles() != null ? breeder.getBreederFiles().stream()
                            .map(file -> BreederFileDTO.builder()
                                    .breederFileId(file.getBreederFileId())
                                    .type(file.getType())
                                    .breederFilePath(file.getBreederFilePath())
                                    .build())
                            .collect(Collectors.toList()) : null)
                    .build();

            // BreederDTO를 UserInfoDTO에 설정
            userInfoDTO.setBreederDTO(breederDTO);

        } else if (user.getRole() == Role.MEMBER) {
            // Member 정보 조회
            Member member = memberRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("해당 일반 유저가 존재하지 않습니다."));

            // MemberDTO 설정
            MemberDTO memberDTO = MemberDTO.builder()
                    .petAllowed(member.getPetAllowed())
                    .cohabitant(member.getCohabitant())
                    .familyAgreement(member.getFamilyAgreement())
                    .employmentStatus(member.getEmploymentStatus())
                    .commuteTime(member.getCommuteTime())
                    .petExperience(member.getPetExperience())
                    .currentPet(member.getCurrentPet())
                    .futurePlan(member.getFuturePlan())
                    .build();

            // MemberDTO를 UserInfoDTO에 설정
            userInfoDTO.setMemberDTO(memberDTO);
        }

        return userInfoDTO;
    }

    @Override
    public UserInfoDTO updateBreeder(Long userId, BreederUpdateDTO breederUpdateDTO) {
        // 사용자 및 브리더 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        Breeder breeder = breederRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 브리더가 존재하지 않습니다."));

        // User 정보 업데이트
        if (breederUpdateDTO.getProfileImage() != null) {
            user.setProfileImage(breederUpdateDTO.getProfileImage());
        }
        if (breederUpdateDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(breederUpdateDTO.getPassword()));
        }
        userRepository.save(user);

        // Breeder 정보 업데이트
        if (breederUpdateDTO.getAnimal() != null) {
            breeder.setAnimal(breederUpdateDTO.getAnimal());
        }
        if (breederUpdateDTO.getSpecies() != null) {
            breeder.setSpecies(breederUpdateDTO.getSpecies());
        }
        if (breederUpdateDTO.getBackgroundImage() != null) {
            breeder.setBackgroundImage(breederUpdateDTO.getBackgroundImage());
        }
        if (breederUpdateDTO.getTradeName() != null) {
            breeder.setTradeName(breederUpdateDTO.getTradeName());
        }
        if (breederUpdateDTO.getTradePhone() != null) {
            breeder.setTradePhone(breederUpdateDTO.getTradePhone());
        }
        if (breederUpdateDTO.getContactableTime() != null) {
            breeder.setContactableTime(breederUpdateDTO.getContactableTime());
        }
        if (breederUpdateDTO.getTradeEmail() != null) {
            breeder.setTradeEmail(breederUpdateDTO.getTradeEmail());
        }
        if (breederUpdateDTO.getRepresentative() != null) {
            breeder.setRepresentative(breederUpdateDTO.getRepresentative());
        }
        if (breederUpdateDTO.getRegistrationNumber() != null) {
            breeder.setRegistrationNumber(breederUpdateDTO.getRegistrationNumber());
        }
        if (breederUpdateDTO.getLicenseNumber() != null) {
            breeder.setLicenseNumber(breederUpdateDTO.getLicenseNumber());
        }
        if (breederUpdateDTO.getSnsAddress() != null) {
            breeder.setSnsAddress(breederUpdateDTO.getSnsAddress());
        }
        if (breederUpdateDTO.getAnimalHospital() != null) {
            breeder.setAnimalHospital(breederUpdateDTO.getAnimalHospital());
        }
        if (breederUpdateDTO.getEmploymentStatus() != null) {
            breeder.setEmploymentStatus(breederUpdateDTO.getEmploymentStatus());
        }
        if (breederUpdateDTO.getTrustLevel() != null) {
            breeder.setTrustLevel(breederUpdateDTO.getTrustLevel());
        }
        if (breederUpdateDTO.getDescription() != null) {
            breeder.setDescription(breederUpdateDTO.getDescription());
        }
        if (breederUpdateDTO.getDescriptionDetail() != null) {
            breeder.setDescriptionDetail(breederUpdateDTO.getDescriptionDetail());
        }
        if (breederUpdateDTO.getSchoolName() != null) {
            breeder.setSchoolName(breederUpdateDTO.getSchoolName());
        }
        if (breederUpdateDTO.getDepartmentName() != null) {
            breeder.setDepartmentName(breederUpdateDTO.getDepartmentName());
        }
        if (breederUpdateDTO.getEnrollmentDate() != null) {
            breeder.setEnrollmentDate(breederUpdateDTO.getEnrollmentDate());
        }
        if (breederUpdateDTO.getGraduationDate() != null) {
            breeder.setGraduationDate(breederUpdateDTO.getGraduationDate());
        }
        if (breederUpdateDTO.getBusinessTime() != null) {
            breeder.setBusinessTime(breederUpdateDTO.getBusinessTime());
        }
        if (breederUpdateDTO.getQuestionGuarantee() != null) {
            breeder.setQuestionGuarantee(breederUpdateDTO.getQuestionGuarantee());
        }
        if (breederUpdateDTO.getQuestionPedigree() != null) {
            breeder.setQuestionPedigree(breederUpdateDTO.getQuestionPedigree());
        }
        if (breederUpdateDTO.getQuestionBaby() != null) {
            breeder.setQuestionBaby(breederUpdateDTO.getQuestionBaby());
        }
        if (breederUpdateDTO.getQuestionPeriod() != null) {
            breeder.setQuestionPeriod(breederUpdateDTO.getQuestionPeriod());
        }
        if (breederUpdateDTO.getQuestionSupport() != null) {
            breeder.setQuestionSupport(breederUpdateDTO.getQuestionSupport());
        }
        breederRepository.save(breeder);

        // BreederFile 엔티티 업데이트
        if (breederUpdateDTO.getBreederFiles() != null) {
            for (BreederFileDTO fileDTO : breederUpdateDTO.getBreederFiles()) {
                BreederFile file = breederFileRepository.findById(fileDTO.getBreederFileId())
                        .orElseThrow(() -> new IllegalArgumentException("BreederFile not found"));

                file.setType(fileDTO.getType());
                file.setBreederFilePath(fileDTO.getBreederFilePath());

                breederFileRepository.save(file);
            }
        }

        // Breeding 엔티티 업데이트
        if (breederUpdateDTO.getBreeding() != null) {
            for (BreedingDTO breedingDTO : breederUpdateDTO.getBreeding()) {
                Breeding breeding = breedingRepository.findById(breedingDTO.getBreedingId())
                        .orElseThrow(() -> new IllegalArgumentException("Breeding not found"));

                breeding.setTradeName(breedingDTO.getTradeName());
                breeding.setJoinDate(breedingDTO.getJoinDate());
                breeding.setLeaveDate(breedingDTO.getLeaveDate());
                breeding.setCurrentlyEmployed(breedingDTO.getCurrentlyEmployed());
                breeding.setDescription(breedingDTO.getDescription());

                breedingRepository.save(breeding);
            }
        }

        // UserDTO와 BreederDTO 생성
        UserDTO userDTO = UserDTO.builder()
                .name(user.getName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .username(user.getUsername())
                .address(user.getAddress())
                .addressDetail(user.getAddressDetail())
                .profileImage(user.getProfileImage())
                .role(user.getRole())
                .status(user.getStatus())
                .build();

        BreederDTO breederDTO = BreederDTO.builder()
                .animal(breeder.getAnimal())
                .species(breeder.getSpecies())
                .backgroundImage(breeder.getBackgroundImage())
                .tradeName(breeder.getTradeName())
                .tradePhone(breeder.getTradePhone())
                .contactableTime(breeder.getContactableTime())
                .tradeEmail(breeder.getTradeEmail())
                .representative(breeder.getRepresentative())
                .registrationNumber(breeder.getRegistrationNumber())
                .licenseNumber(breeder.getLicenseNumber())
                .snsAddress(breeder.getSnsAddress())
                .animalHospital(breeder.getAnimalHospital())
                .employmentStatus(breeder.getEmploymentStatus())
                .trustLevel(breeder.getTrustLevel())
                .description(breeder.getDescription())
                .descriptionDetail(breeder.getDescriptionDetail())
                .schoolName(breeder.getSchoolName())
                .departmentName(breeder.getDepartmentName())
                .enrollmentDate(breeder.getEnrollmentDate())
                .graduationDate(breeder.getGraduationDate())
                .businessTime(breeder.getBusinessTime())
                .questionGuarantee(breeder.getQuestionGuarantee())
                .questionPedigree(breeder.getQuestionPedigree())
                .questionBaby(breeder.getQuestionBaby())
                .questionPeriod(breeder.getQuestionPeriod())
                .questionSupport(breeder.getQuestionSupport())
                .breeding(breeder.getBreedingCareer().stream()
                        .map(b -> BreedingDTO.builder()
                                .breedingId(b.getBreedingId())
                                .tradeName(b.getTradeName())
                                .joinDate(b.getJoinDate())
                                .leaveDate(b.getLeaveDate())
                                .currentlyEmployed(b.getCurrentlyEmployed())
                                .description(b.getDescription())
                                .build())
                        .collect(Collectors.toList()))
                .breederFiles(breeder.getBreederFiles().stream()
                        .map(f -> BreederFileDTO.builder()
                                .breederFileId(f.getBreederFileId())
                                .type(f.getType())
                                .breederFilePath(f.getBreederFilePath())
                                .build())
                        .collect(Collectors.toList()))
                .build();

        return UserInfoDTO.builder()
                .userDTO(userDTO)
                .breederDTO(breederDTO)
                .build();
    }


    @Override
    public UserInfoDTO updateMember(Long userId, MemberUpdateDTO memberUpdateDTO) {
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        // 멤버 정보 조회
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일반 유저가 존재하지 않습니다."));

        // User 정보 업데이트
        if (memberUpdateDTO.getProfileImage() != null) {
            user.setProfileImage(memberUpdateDTO.getProfileImage());
        }
        if (memberUpdateDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(memberUpdateDTO.getPassword()));
        }
        if (memberUpdateDTO.getZipcode() != null) {
            user.setZipcode(memberUpdateDTO.getZipcode());
        }
        if (memberUpdateDTO.getAddress() != null) {
            user.setAddress(memberUpdateDTO.getAddress());
        }
        if (memberUpdateDTO.getAddressDetail() != null) {
            user.setAddressDetail(memberUpdateDTO.getAddressDetail());
        }

        // Member 정보 업데이트
        if (memberUpdateDTO.getPetAllowed() != null) {
            member.setPetAllowed(memberUpdateDTO.getPetAllowed());
        }
        if (memberUpdateDTO.getCohabitant() != null) {
            member.setCohabitant(memberUpdateDTO.getCohabitant());
        }
        if (memberUpdateDTO.getFamilyAgreement() != null) {
            member.setFamilyAgreement(memberUpdateDTO.getFamilyAgreement());
        }
        if (memberUpdateDTO.getEmploymentStatus() != null) {
            member.setEmploymentStatus(memberUpdateDTO.getEmploymentStatus());
        }
        if (memberUpdateDTO.getCommuteTime() != null) {
            member.setCommuteTime(memberUpdateDTO.getCommuteTime());
        }
        if (memberUpdateDTO.getPetExperience() != null) {
            member.setPetExperience(memberUpdateDTO.getPetExperience());
        }
        if (memberUpdateDTO.getCurrentPet() != null) {
            member.setCurrentPet(memberUpdateDTO.getCurrentPet());
        }
        if (memberUpdateDTO.getFuturePlan() != null) {
            member.setFuturePlan(memberUpdateDTO.getFuturePlan());
        }

        // 변경사항 저장
        userRepository.save(user);
        memberRepository.save(member);

        // DTO 변환
        UserDTO userDTO = UserDTO.builder()
                .name(user.getName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .username(user.getUsername())
                .zipcode(user.getZipcode())
                .address(user.getAddress())
                .addressDetail(user.getAddressDetail())
                .profileImage(user.getProfileImage())
                .role(user.getRole())
                .status(user.getStatus())
                .build();

        MemberDTO memberDTO = MemberDTO.builder()
                .petAllowed(member.getPetAllowed())
                .cohabitant(member.getCohabitant())
                .familyAgreement(member.getFamilyAgreement())
                .employmentStatus(member.getEmploymentStatus())
                .commuteTime(member.getCommuteTime())
                .petExperience(member.getPetExperience())
                .currentPet(member.getCurrentPet())
                .futurePlan(member.getFuturePlan())
                .build();

        UserInfoDTO userInfoDTO = UserInfoDTO.builder()
                .userDTO(userDTO)
                .memberDTO(memberDTO)
                .build();

        return userInfoDTO;
    }

    @Override
    public Object getUserReservation(Long userId) {
        return null;
    }

    @Override
    public void saveAnimalView(Long userId, Long animalId, HttpServletRequest request, HttpServletResponse response) {
        String cookieName = "recentViews_" + userId;

        // 동물 ID 유효성 검사
        if (!animalRepository.existsByAnimalId(animalId)) {
            // 동물 ID가 유효하지 않으면 쿠키를 업데이트하지 않음
            throw new RuntimeException("동물 id를 찾을 수 없습니다.");
        }

        // 기존 쿠키 조회
        Cookie[] cookies = request.getCookies();
        String recentViews = "";

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    // 디코딩하여 쿠키 값을 읽음
                    recentViews = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                    break;
                }
            }
        }

        // 새로운 동물 ID 추가
        recentViews = addAnimalIdToRecentViews(recentViews, animalId);

        // 인코딩하여 쿠키 값을 설정
        String encodedRecentViews = URLEncoder.encode(recentViews, StandardCharsets.UTF_8);
        Cookie recentViewsCookie = new Cookie(cookieName, encodedRecentViews);
        recentViewsCookie.setPath("/");
        recentViewsCookie.setMaxAge(7 * 24 * 60 * 60); // 7일간 유지
        response.addCookie(recentViewsCookie);
    }
    private String addAnimalIdToRecentViews(String recentViews, Long animalId) {
        String[] viewsArray = recentViews.isEmpty() ? new String[0] : recentViews.split(",");
        List<String> viewsList = new ArrayList<>(Arrays.asList(viewsArray));

        // 동물 ID가 이미 목록에 있으면 제거하고 맨 앞으로 추가
        viewsList.remove(animalId.toString());
        viewsList.add(0, animalId.toString());

        // 최대 5개만 저장
        if (viewsList.size() > 5) {
            viewsList = viewsList.subList(0, 5);
        }

        return String.join(",", viewsList);
    }

    @Override
    public List<Long> getRecentViews(Long userId, HttpServletRequest request) {
        String cookieName = "recentViews_" + userId;

        // 쿠키에서 최근 본 동물 목록 조회
        Cookie[] cookies = request.getCookies();
        String recentViews = "";

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    // 디코딩하여 쿠키 값을 읽음
                    recentViews = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                    break;
                }
            }
        }

        // String을 List<Long>으로 변환
        return Arrays.stream(recentViews.split(","))
                .filter(s -> !s.isEmpty())
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }
}
