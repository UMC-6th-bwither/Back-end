package com.umc.bwither.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.breeder.repository.BreederRepository;
import com.umc.bwither.member.entity.Member;
import com.umc.bwither.member.repository.MemberRepository;
import com.umc.bwither.user.dto.BreederDTO;
import com.umc.bwither.user.dto.MemberDTO;
import com.umc.bwither.user.dto.UserDTO;
import com.umc.bwither.user.dto.UserInfoDTO;
import com.umc.bwither.user.entity.User;
import com.umc.bwither.user.entity.enums.Role;
import com.umc.bwither.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BreederRepository breederRepository;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;
    @Override
    public UserInfoDTO getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        UserDTO userDto = UserDTO.builder()
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

        BreederDTO breederDto = null;
        MemberDTO memberDto = null;

        if (user.getRole() == Role.BREEDER) {
            breederDto = breederRepository.findById(userId)
                    .map(breeder -> BreederDTO.builder()
                            .animal(breeder.getAnimal())
                            .species(breeder.getSpecies())
                            .tradeName(breeder.getTradeName())
                            .representative(breeder.getRepresentative())
                            .registrationNumber(breeder.getRegistrationNumber())
                            .licenseNumber(breeder.getLicenseNumber())
                            .snsAddress(breeder.getSnsAddress())
                            .animalHospital(breeder.getAnimalHospital())
                            .employmentStatus(breeder.getEmploymentStatus())
                            .trustLevel(breeder.getTrustLevel())
                            .description(breeder.getDescription())
                            .build())
                    .orElseThrow(() -> new IllegalArgumentException("Breeder details not found for user with id: " + userId));
        } else if (user.getRole() == Role.MEMBER) {
            memberDto = memberRepository.findById(userId)
                    .map(member -> MemberDTO.builder()
                            .petAllowed(member.getPetAllowed())
                            .cohabitant(member.getCohabitant())
                            .familyAgreement(member.getFamilyAgreement())
                            .employmentStatus(member.getEmploymentStatus())
                            .commuteTime(member.getCommuteTime())
                            .petExperience(member.getPetExperience())
                            .currentPet(member.getCurrentPet())
                            .futurePlan(member.getFuturePlan())
                            .build())
                    .orElseThrow(() -> new IllegalArgumentException("Member details not found for user with id: " + userId));
        }

        return UserInfoDTO.builder()
                .userDTO(userDto)
                .breederDTO(breederDto)
                .memberDTO(memberDto)
                .build();
    }
    @Override
    public UserInfoDTO updateUserInfo(Long userId, UserDTO userDto, BreederDTO breederDto, MemberDTO memberDto) {
        System.out.println("서비스들어옴"+userId);
        // 1. 사용자 정보 가져오기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // 2. UserDTO 업데이트
        if (userDto != null) {
            System.out.println("유저"+userDto);
            if (userDto.getName() != null) user.setName(userDto.getName());
            if (userDto.getPhone() != null) user.setPhone(userDto.getPhone());
            if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
            if (userDto.getUsername() != null) user.setUsername(userDto.getUsername());
            if (userDto.getZipcode() != null) user.setZipcode(userDto.getZipcode());
            if (userDto.getAddress() != null) user.setAddress(userDto.getAddress());
            if (userDto.getAddressDetail() != null) user.setAddressDetail(userDto.getAddressDetail());
            if (userDto.getProfileImage() != null) user.setProfileImage(userDto.getProfileImage());
        }

        // 3. BreederDTO 업데이트 (사용자가 Breeder일 경우)
        if (breederDto != null && user.getRole() == Role.BREEDER) {
            System.out.println("브리더"+breederDto);
            Breeder breeder = breederRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Breeder details not found for user with id: " + userId));
            if (breederDto.getAnimal() != null) breeder.setAnimal(breederDto.getAnimal());
            if (breederDto.getSpecies() != null) breeder.setSpecies(breederDto.getSpecies());
            if (breederDto.getTradeName() != null) breeder.setTradeName(breederDto.getTradeName());
            if (breederDto.getRepresentative() != null) breeder.setRepresentative(breederDto.getRepresentative());
            if (breederDto.getRegistrationNumber() != null) breeder.setRegistrationNumber(breederDto.getRegistrationNumber());
            if (breederDto.getLicenseNumber() != null) breeder.setLicenseNumber(breederDto.getLicenseNumber());
            if (breederDto.getSnsAddress() != null) breeder.setSnsAddress(breederDto.getSnsAddress());
            if (breederDto.getAnimalHospital() != null) breeder.setAnimalHospital(breederDto.getAnimalHospital());
            if (breederDto.getEmploymentStatus() != null) breeder.setEmploymentStatus(breederDto.getEmploymentStatus());
            if (breederDto.getTrustLevel() != null) breeder.setTrustLevel(breederDto.getTrustLevel());
            if (breederDto.getDescription() != null) breeder.setDescription(breederDto.getDescription());
            breederRepository.save(breeder);
        }

        // 4. MemberDTO 업데이트 (사용자가 Member일 경우)
        if (memberDto != null && user.getRole() == Role.MEMBER) {
            System.out.println("멤버"+memberDto);
            Member member = memberRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Member details not found for user with id: " + userId));
            if (memberDto.getPetAllowed() != null) member.setPetAllowed(memberDto.getPetAllowed());
            if (memberDto.getCohabitant() != null) member.setCohabitant(memberDto.getCohabitant());
            if (memberDto.getFamilyAgreement() != null) member.setFamilyAgreement(memberDto.getFamilyAgreement());
            if (memberDto.getEmploymentStatus() != null) member.setEmploymentStatus(memberDto.getEmploymentStatus());
            if (memberDto.getCommuteTime() != null) member.setCommuteTime(memberDto.getCommuteTime());
            if (memberDto.getPetExperience() != null) member.setPetExperience(memberDto.getPetExperience());
            if (memberDto.getCurrentPet() != null) member.setCurrentPet(memberDto.getCurrentPet());
            if (memberDto.getFuturePlan() != null) member.setFuturePlan(memberDto.getFuturePlan());
            memberRepository.save(member);
        }

        // 5. 최종 사용자 정보 저장
        userRepository.save(user);

        // 6. 업데이트된 정보 반환
        return UserInfoDTO.builder()
                .userDTO(UserDTO.builder()
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
                        .build())
                .breederDTO(breederDto)
                .memberDTO(memberDto)
                .build();
    }
    public User create(User user) {
        if (user == null || user.getUsername() == null) {
            throw new RuntimeException("유효하지 않은 사용자 정보입니다.");
        }
        final String username = user.getUsername();
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }
        return userRepository.save(user);
    }

    public User getByCredentials(final String username, final String password,
                                 final PasswordEncoder encoder){
        final User originalUser = userRepository.findByUsername(username);

        if (originalUser == null) {
            log.error("No user found with username: {}", username);
            return null;
        }

        if(encoder.matches(password, originalUser.getPassword())) {
            return originalUser;
        } else {
            log.error("Password mismatch for user: {}", username);
        }

        return null;
    }
}
