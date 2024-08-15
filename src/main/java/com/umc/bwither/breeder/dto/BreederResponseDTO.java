package com.umc.bwither.breeder.dto;

import com.umc.bwither.animal.dto.AnimalResponseDTO;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class BreederResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BreederDetailDTO {
        Long breederId;
        String profileUrl;
        String tradeName;
        List<String> species;
        String address;
        String description;
        Integer totalAnimals;
        Double breederRating;
        Integer reviewCount;
        Integer careerYear;
        Integer trustLevel;
        String tradePhone;
        String snsAddress;
        String detailDescription;
        String schoolName;
        String departmentName;
        LocalDate enrollmentDate;
        LocalDate graduationDate;
        String questionGuarantee;
        String questionPedigree;
        String questionBaby;
        String questionPeriod;
        String questionSupport;
        List<BreederFileDTO> files;
        List<BreedingCareerDTO> breedingCareers;
        List<BreedingAnimalDTO> breedingAnimals;
        List<ReviewDTO> reviews;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BreedingAnimalDTO {
        Long animalId;
        Integer age;
        String gender;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewDTO {
        Long reviewId;
        Integer age;
        String gender;
    }

    @Data
    @Builder
    public static class TrustLevelResponseDTO {
        private Integer trustLevel;
    }
}
