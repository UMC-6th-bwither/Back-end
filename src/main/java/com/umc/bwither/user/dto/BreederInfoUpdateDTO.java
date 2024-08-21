package com.umc.bwither.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.umc.bwither.breeder.entity.enums.AnimalType;
import com.umc.bwither.breeder.entity.enums.EmploymentStatus;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BreederInfoUpdateDTO {
    private String tradeName; // 켄넬/캐터리 이름
    private String description; // 간략한 소개
    private String reviewEvent; // 리뷰 이벤트

    // 브리더 정보
    private String tradePhone; // 전화번호
    private String contactableTime; // 연락 가능 시간
    private String snsAddress; // 홈페이지/SNS 주소
    private String descriptionDetail; // 브리더 상세 설명

    private List<String> species; // 종

    // 경력
//    private List<BreedingDTO> breeding;

    // 학력
    private String schoolName; // 학교명
    private String departmentName; // 학과명
    private LocalDate enrollmentDate; // 입학연월
    private LocalDate graduationDate; // 졸업연월

    // 켄넬 정보
    private String kennelAddress; // 켄넬 주소
    private String businessTime; // 영업시간
    private String animalCount; // 개체 수

    // 자주 물어보는 질문
    private String questionGuarantee;
    private String questionPedigree;
    private String questionBaby;
    private String questionPeriod;
    private String questionSupport;

}
