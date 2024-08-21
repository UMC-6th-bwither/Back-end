package com.umc.bwither.user.service;

import com.umc.bwither.animal.repository.AnimalRepository;
import com.umc.bwither.animal.service.S3Uploader;
import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.breeder.entity.BreederFile;
import com.umc.bwither.breeder.entity.Breeding;
import com.umc.bwither.breeder.entity.enums.FileType;
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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {
    private final UserRepository userRepository;
    private final BreederRepository breederRepository;
    private final MemberRepository memberRepository;
    private final BreedingRepository breedingRepository;
    private final BreederFileRepository breederFileRepository;
    private final AnimalRepository animalRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final S3Uploader s3Uploader;

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
                                    // .breedingId(breeding.getBreedingId())
                                    // .tradeName(breeding.getTradeName())
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
                    .cohabitantCount(member.getCohabitantCount())
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
    @Transactional
    public UserInfoDTO updateBreederProfile(Long userId, BreederProfileUpdateDTO breederProfileUpdateDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        if (breederProfileUpdateDTO.getProfileImage() != null) {
            user.setProfileImage(breederProfileUpdateDTO.getProfileImage());
        }
        if (breederProfileUpdateDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(breederProfileUpdateDTO.getPassword()));
        }
        userRepository.save(user);

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

        return UserInfoDTO.builder()
                .userDTO(userDTO)
                .build();
    }

    // @Transactional
    // public UserInfoDTO updateBreederInfo(Long userId, BreederInfoUpdateDTO
    // breederInfoUpdateDTO) {
    // // 사용자 및 브리더 정보 조회
    // User user = userRepository.findById(userId)
    // .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
    //
    // Breeder breeder = breederRepository.findByUser_UserId(userId)
    // .orElseThrow(() -> new IllegalArgumentException("해당 브리더가 존재하지 않습니다."));
    //
    // // Breeder 정보 업데이트
    // if (breederInfoUpdateDTO.getSpecies() != null) {
    // breeder.setSpecies(breederInfoUpdateDTO.getSpecies());
    // }
    // if (breederInfoUpdateDTO.getTradeName() != null) {
    // breeder.setTradeName(breederInfoUpdateDTO.getTradeName());
    // }
    // if (breederInfoUpdateDTO.getTradePhone() != null) {
    // breeder.setTradePhone(breederInfoUpdateDTO.getTradePhone());
    // }
    // if (breederInfoUpdateDTO.getContactableTime() != null) {
    // breeder.setContactableTime(breederInfoUpdateDTO.getContactableTime());
    // }
    // if (breederInfoUpdateDTO.getSnsAddress() != null) {
    // breeder.setSnsAddress(breederInfoUpdateDTO.getSnsAddress());
    // }
    // if (breederInfoUpdateDTO.getDescription() != null) {
    // breeder.setDescription(breederInfoUpdateDTO.getDescription());
    // }
    // if (breederInfoUpdateDTO.getDescriptionDetail() != null) {
    // breeder.setDescriptionDetail(breederInfoUpdateDTO.getDescriptionDetail());
    // }
    // if (breederInfoUpdateDTO.getSchoolName() != null) {
    // breeder.setSchoolName(breederInfoUpdateDTO.getSchoolName());
    // }
    // if (breederInfoUpdateDTO.getDepartmentName() != null) {
    // breeder.setDepartmentName(breederInfoUpdateDTO.getDepartmentName());
    // }
    // if (breederInfoUpdateDTO.getEnrollmentDate() != null) {
    // breeder.setEnrollmentDate(breederInfoUpdateDTO.getEnrollmentDate());
    // }
    // if (breederInfoUpdateDTO.getGraduationDate() != null) {
    // breeder.setGraduationDate(breederInfoUpdateDTO.getGraduationDate());
    // }
    // if (breederInfoUpdateDTO.getBusinessTime() != null) {
    // breeder.setBusinessTime(breederInfoUpdateDTO.getBusinessTime());
    // }
    // if (breederInfoUpdateDTO.getQuestionGuarantee() != null) {
    // breeder.setQuestionGuarantee(breederInfoUpdateDTO.getQuestionGuarantee());
    // }
    // if (breederInfoUpdateDTO.getQuestionPedigree() != null) {
    // breeder.setQuestionPedigree(breederInfoUpdateDTO.getQuestionPedigree());
    // }
    // if (breederInfoUpdateDTO.getQuestionBaby() != null) {
    // breeder.setQuestionBaby(breederInfoUpdateDTO.getQuestionBaby());
    // }
    // if (breederInfoUpdateDTO.getQuestionPeriod() != null) {
    // breeder.setQuestionPeriod(breederInfoUpdateDTO.getQuestionPeriod());
    // }
    // if (breederInfoUpdateDTO.getQuestionSupport() != null) {
    // breeder.setQuestionSupport(breederInfoUpdateDTO.getQuestionSupport());
    // }
    // breederRepository.save(breeder);
    //
    //
    // // Breeding 엔티티 업데이트
    //// if (breederInfoUpdateDTO.getBreeding() != null) {
    //// for (BreedingDTO breedingDTO : breederInfoUpdateDTO.getBreeding()) {
    //// Breeding breeding =
    // breedingRepository.findById(breedingDTO.getBreedingId())
    //// .orElseThrow(() -> new IllegalArgumentException("Breeding not found"));
    ////
    //// breeding.setJoinDate(breedingDTO.getJoinDate());
    //// breeding.setLeaveDate(breedingDTO.getLeaveDate());
    //// breeding.setCurrentlyEmployed(breedingDTO.getCurrentlyEmployed());
    //// breeding.setDescription(breedingDTO.getBreedingDescription());
    ////
    //// breedingRepository.save(breeding);
    //// }
    //// }
    //
    // // Breeding 엔티티 업데이트
    // System.out.println("Received DTO: " + breederInfoUpdateDTO.toString());
    // System.out.println("브리딩: "+breederInfoUpdateDTO.getBreeding());
    // if (breederInfoUpdateDTO.getBreeding() != null) {
    // // 기존 Breeding 데이터 삭제
    // breedingRepository.deleteByBreeder(breeder);
    //
    // // 새 Breeding 데이터 추가
    // List<Breeding> newBreedingList = breederInfoUpdateDTO.getBreeding().stream()
    // .map(breedingDTO -> Breeding.builder()
    // .breeder(breeder)
    // .joinDate(breedingDTO.getJoinDate())
    // .leaveDate(breedingDTO.getLeaveDate())
    // .currentlyEmployed(breedingDTO.getCurrentlyEmployed())
    // .description(breedingDTO.getDescription())
    // .build())
    // .collect(Collectors.toList());
    //
    // breedingRepository.saveAll(newBreedingList);
    // }
    //
    // breederRepository.save(breeder);
    //
    // // UserDTO와 BreederDTO 생성
    // UserDTO userDTO = UserDTO.builder()
    // .name(user.getName())
    // .phone(user.getPhone())
    // .email(user.getEmail())
    // .username(user.getUsername())
    // .address(user.getAddress())
    // .addressDetail(user.getAddressDetail())
    // .profileImage(user.getProfileImage())
    // .role(user.getRole())
    // .status(user.getStatus())
    // .build();
    //
    // BreederDTO breederDTO = BreederDTO.builder()
    // .animal(breeder.getAnimal())
    // .species(breeder.getSpecies())
    // .backgroundImage(breeder.getBackgroundImage())
    // .tradeName(breeder.getTradeName())
    // .tradePhone(breeder.getTradePhone())
    // .contactableTime(breeder.getContactableTime())
    // .tradeEmail(breeder.getTradeEmail())
    // .representative(breeder.getRepresentative())
    // .registrationNumber(breeder.getRegistrationNumber())
    // .licenseNumber(breeder.getLicenseNumber())
    // .snsAddress(breeder.getSnsAddress())
    // .animalHospital(breeder.getAnimalHospital())
    // .trustLevel(breeder.getTrustLevel())
    // .description(breeder.getDescription())
    // .descriptionDetail(breeder.getDescriptionDetail())
    // .schoolName(breeder.getSchoolName())
    // .departmentName(breeder.getDepartmentName())
    // .enrollmentDate(breeder.getEnrollmentDate())
    // .graduationDate(breeder.getGraduationDate())
    // .businessTime(breeder.getBusinessTime())
    // .questionGuarantee(breeder.getQuestionGuarantee())
    // .questionPedigree(breeder.getQuestionPedigree())
    // .questionBaby(breeder.getQuestionBaby())
    // .questionPeriod(breeder.getQuestionPeriod())
    // .questionSupport(breeder.getQuestionSupport())
    // .breeding(breeder.getBreedingCareer().stream()
    // .map(b -> BreedingDTO.builder()
    // .joinDate(b.getJoinDate())
    // .leaveDate(b.getLeaveDate())
    // .currentlyEmployed(b.getCurrentlyEmployed())
    // .description(b.getDescription())
    // .build())
    // .collect(Collectors.toList()))
    // .breederFiles(breeder.getBreederFiles().stream()
    // .map(f -> BreederFileDTO.builder()
    // .breederFileId(f.getBreederFileId())
    // .type(f.getType())
    // .breederFilePath(f.getBreederFilePath())
    // .build())
    // .collect(Collectors.toList()))
    // .build();
    //
    // return UserInfoDTO.builder()
    // .userDTO(userDTO)
    // .breederDTO(breederDTO)
    // .build();
    // }

    @Override
    @Transactional
    public void updateBreederInfo(Long userId, BreederInfoUpdateDTO breederInfoUpdateDTO,
            Map<FileType, List<MultipartFile>> breederFiles, BreedingRequestDTO breedings) {
        // 브리더 엔티티 조회
        Breeder breeder = breederRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 브리더가 존재하지 않습니다."));

        // DTO의 값을 사용하여 브리더 엔티티 업데이트
        updateBreederFromDTO(breeder, breederInfoUpdateDTO);

        // 저장된 브리더 엔티티 저장
        breederRepository.save(breeder);

        // 파일 처리
        if (breederFiles != null) {
            for (Map.Entry<FileType, List<MultipartFile>> entry : breederFiles.entrySet()) {
                FileType fileType = entry.getKey();
                List<MultipartFile> files = entry.getValue();
                if (files != null) {
                    // 기존 파일 삭제
                    List<BreederFile> oldFiles = breederFileRepository.findByBreederAndType(breeder, fileType);
                    for (BreederFile oldFile : oldFiles) {
                        s3Uploader.deleteFile(oldFile.getBreederFilePath());
                    }
                    breederFileRepository.deleteByBreederAndType(breeder, fileType);

                    // 새 파일 업로드 및 저장
                    for (MultipartFile file : files) {
                        if (file != null && !file.isEmpty()) {
                            String filePath = s3Uploader.uploadFile("breeder-files", file);
                            BreederFile breederFile = BreederFile.builder()
                                    .breeder(breeder)
                                    .type(fileType)
                                    .breederFilePath(filePath)
                                    .build();
                            breederFileRepository.save(breederFile);
                        }
                    }
                }
            }
        }

        // 경력 정보 업데이트
        updateBreedingCareer(breeder, breedings);
    }

    private void updateBreederFromDTO(Breeder breeder, BreederInfoUpdateDTO dto) {
        // 기본 정보 업데이트
        breeder.setTradeName(dto.getTradeName());
        breeder.setDescription(dto.getDescription());
        breeder.setReviewEvent(dto.getReviewEvent());
        breeder.setTradePhone(dto.getTradePhone());
        breeder.setContactableTime(dto.getContactableTime());
        breeder.setSnsAddress(dto.getSnsAddress());
        breeder.setDescriptionDetail(dto.getDescriptionDetail());
        breeder.setSpecies(dto.getSpecies());
        breeder.setBusinessTime(dto.getBusinessTime());
        breeder.setAnimalCount(dto.getAnimalCount());
        breeder.setQuestionGuarantee(dto.getQuestionGuarantee());
        breeder.setQuestionPedigree(dto.getQuestionPedigree());
        breeder.setQuestionBaby(dto.getQuestionBaby());
        breeder.setQuestionPeriod(dto.getQuestionPeriod());
        breeder.setQuestionSupport(dto.getQuestionSupport());

        // 학교 및 졸업 정보 업데이트
        breeder.setSchoolName(dto.getSchoolName());
        breeder.setDepartmentName(dto.getDepartmentName());
        breeder.setEnrollmentDate(dto.getEnrollmentDate());
        breeder.setGraduationDate(dto.getGraduationDate());

        // 기타 정보 업데이트
        breeder.setKennelAddress(dto.getKennelAddress());
    }

    @Transactional
    public void updateBreedingCareer(Breeder breeder, BreedingRequestDTO breedings) {
        if (breedings == null || breedings.getBreedingDTOs() == null) {
            System.out.println("브리딩 데이터가 제공되지 않았습니다.");
            return; // 또는 적절한 예외를 던질 수 있습니다.
        }

        // DTO 목록을 기반으로 새로운 Breeding 엔티티 생성
        List<Breeding> updatedBreedingCareers = breedings.getBreedingDTOs().stream()
                .map(dto -> Breeding.builder()
                        .tradeName(dto.getTradeName())
                        .joinDate(dto.getJoinDate())
                        .leaveDate(dto.getLeaveDate())
                        .currentlyEmployed(dto.getCurrentlyEmployed())
                        .description(dto.getDescription())
                        .breeder(breeder)
                        .build())
                .collect(Collectors.toList());

        // 현재 Breeder에 연결된 Breeding 목록을 가져옴
        List<Breeding> existingBreedingCareers = breedingRepository.findByBreeder(breeder);

        // Breeding 엔티티의 ID 목록 생성
        List<Long> updatedBreedingIds = updatedBreedingCareers.stream()
                .filter(breeding -> breeding.getBreedingId() != null) // ID가 null이 아닌 것만 필터링
                .map(Breeding::getBreedingId)
                .collect(Collectors.toList());

        // 현재 저장된 Breeding 엔티티의 ID와 비교하여 삭제할 엔티티를 찾음
        List<Breeding> toDelete = existingBreedingCareers.stream()
                .filter(breeding -> !updatedBreedingIds.contains(breeding.getBreedingId()))
                .collect(Collectors.toList());

        // 삭제할 엔티티를 삭제
        breedingRepository.deleteAll(toDelete);

        // 새로운 Breeding 엔티티를 저장
        for (Breeding breeding : updatedBreedingCareers) {
            if (breeding.getBreedingId() == null) {
                // 새 엔티티 추가
                breedingRepository.save(breeding);
            } else {
                // 기존 엔티티 업데이트
                breedingRepository.save(breeding);
            }
        }
    }

    private Breeding convertToBreedingEntity(BreedingDTO dto) {
        return Breeding.builder()
                .tradeName(dto.getTradeName())
                .joinDate(dto.getJoinDate())
                .leaveDate(dto.getLeaveDate())
                .currentlyEmployed(dto.getCurrentlyEmployed())
                .description(dto.getDescription())
                .build();
    }

    @Transactional
    public void updateBreedingData(Breeder breeder, List<BreedingDTO> newBreedingData) {
        // 기존 Breeding 데이터 삭제
        breedingRepository.deleteByBreeder(breeder);

        // 새로운 Breeding 데이터 추가
        List<Breeding> newBreedingList = newBreedingData.stream()
                .map(dto -> Breeding.builder()
                        .breeder(breeder)
                        .tradeName(dto.getTradeName())
                        .joinDate(dto.getJoinDate())
                        .leaveDate(dto.getLeaveDate())
                        .currentlyEmployed(dto.getCurrentlyEmployed())
                        .description(dto.getDescription())
                        .build())
                .collect(Collectors.toList());

        // Breeding 데이터를 저장
        breedingRepository.saveAll(newBreedingList);
    }

    @Override
    @Transactional
    public void updateBreederBackgroundImage(Long userId, String backgroundImageUrl) {
        Breeder breeder = breederRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 브리더가 존재하지 않습니다."));
        breeder.setBackgroundImage(backgroundImageUrl);
        breederRepository.save(breeder);
    }

    @Override
    @Transactional
    public void updateBreederFiles(Long userId, List<BreederFileDTO> breederFileDTOs) {
        Breeder breeder = breederRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 브리더가 존재하지 않습니다."));

        // for (FileType fileType : FileType.values()) {
        // breederFileRepository.deleteByBreederAndType(breeder, fileType);
        // }
        //
        // // 새로운 파일 추가
        // List<BreederFile> breederFiles = breederFileDTOs.stream()
        // .map(fileDTO -> new BreederFile(breeder, fileDTO.getType(),
        // fileDTO.getBreederFilePath()))
        // .collect(Collectors.toList());
        //
        // breederFileRepository.saveAll(breederFiles);
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
        // if (memberUpdateDTO.getProfileImage() != null) {
        // user.setProfileImage(memberUpdateDTO.getProfileImage());
        // }
        if (memberUpdateDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(memberUpdateDTO.getPassword()));
        }
        if (memberUpdateDTO.getPhone() != null) {
            user.setPhone(memberUpdateDTO.getPhone());
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
        if (memberUpdateDTO.getCohabitantCount() != null) {
            member.setCohabitantCount(memberUpdateDTO.getCohabitantCount());
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
                .cohabitantCount(member.getCohabitantCount())
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
    public void updateMemberProfileImage(Long userId, String profileImage) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        if (profileImage != null) {
            user.setProfileImage(profileImage);
        }

        userRepository.save(user);
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

        // 최대 30개만 저장
        if (viewsList.size() > 30) {
            viewsList = viewsList.subList(0, 30);
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