package com.umc.bwither.user.dto;

import com.umc.bwither.breeder.dto.BreedingCareerDTO;
import com.umc.bwither.breeder.entity.enums.AnimalType;
import com.umc.bwither.breeder.entity.enums.EmploymentStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BreederUpdateDTO {
    // User
    private String profileImage;
    private String password;

    // Breeder
    private AnimalType animal; // 브리딩하는 동물 종류
    private List<String> species; // 종
    private String tradeName; // 상호명
    private String tradePhone; // 전화번호
    private String tradeEmail; // 이메일
    private String representative; // 대표자명
    private String registrationNumber; // 사업자 등록 번호
    private String licenseNumber; // 동물생산업 허가 번호
    private String snsAddress; // 홈페이지/SNS 주소
    private String animalHospital; // 이용 중인 동물병원
    private EmploymentStatus employmentStatus; // 재직 상태
    private Integer trustLevel; // 신뢰 등급

    private String description; // 간략한 소개
    private String descriptionDetail; // 브리더 상세 설명

    // 학력
    private String schoolName; // 학교명
    private String departmentName; // 학과명
    private LocalDate enrollmentDate; // 입학연월
    private LocalDate graduationDate; // 졸업연월

    // 자주 물어보는 질문
    private String questionGuarantee;
    private String questionPedigree;
    private String questionBaby;
    private String questionPeriod;
    private String questionSupport;

    // Breeding
    private List<BreedingDTO> breeding;

    // Breeder Files
    private List<BreederFileDTO> breederFiles;
}
