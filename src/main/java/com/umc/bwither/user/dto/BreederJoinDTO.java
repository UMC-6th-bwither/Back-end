package com.umc.bwither.user.dto;

import com.umc.bwither.breeder.dto.BreederFileDTO;
import com.umc.bwither.breeder.dto.BreedingCareerDTO;
import com.umc.bwither.breeder.entity.enums.AnimalType;
import com.umc.bwither.breeder.entity.enums.EmploymentStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BreederJoinDTO {
    // User 관련 필드
    private String name;           // 이름
    private String phone;          // 전화번호
    private String email;          // 이메일
    private String username;       // 아이디
    private String password;       // 비밀번호

    private String zipcode;        // 우편번호
    private String address;        // 주소
    private String addressDetail;  // 상세 주소

    // Breeder 관련 필드
    private AnimalType animal;         // 브리딩하는 동물 종류
    private List<String> species;        // 종
    private String tradeName;      // 상호명
    private String tradePhone;     // 전화번호
    private String tradeEmail;     // 이메일
    private String representative; // 대표자명
    private String registrationNumber; // 사업자 등록 번호
    private String licenseNumber;  // 동물생산업 허가 번호
    private String snsAddress;     // 홈페이지/SNS 주소 (선택 사항)
    private String animalHospital; // 이용 중인 동물병원 (선택 사항)
    private List<String> certificateNames; // 자격증 명
    private List<BreedingJoinDTO> breedingCareers;

}