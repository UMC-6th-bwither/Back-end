package com.umc.bwither.breeder.dto;

import com.umc.bwither.animal.dto.AnimalResponseDTO;
import com.umc.bwither.animal.entity.enums.Gender;
import com.umc.bwither.animal.entity.enums.Status;
import com.umc.bwither.breeder.entity.enums.AnimalType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class BreederResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BreederDetailDTO {
        Long breederId;
        String profileUrl;
        String backgroundUrl;
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
        String contactableTime;
        String snsAddress;
        String detailDescription;
        String schoolName;
        String departmentName;
        LocalDate enrollmentDate;
        LocalDate graduationDate;
        String kennelAddress;
        String businessTime;
        List<String> animalCount;
        String questionGuarantee;
        String questionPedigree;
        String questionBaby;
        String questionPeriod;
        String questionSupport;
        List<BreederFileDTO> files;
        List<BreedingCareerDTO> breedingCareers;
        List<BreedingAnimalDTO> breedingAnimals;
        List<ReviewDTO> reviews;
        List<BreederTipsDTO> breederTips;
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
        Long postId;
        Long userId;
        String species;
        List<String> fileUrl;
        Integer rating;
        String content;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BreederTipsDTO {
        Long postId;
        Long breederId;
        String fileUrl;
        String title;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BreederPreViewListDTO {
        List<BreederPreviewDTO> breederList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BreederPreviewDTO {
        Long breederId;
        String profileUrl;
        String address;
        String breederName;
        AnimalType animalType;
        List<String> species;
        Integer careerYear;
        Integer certificateCount;
        Integer waitAnimal;
        Integer waitList;
        Double breederRating;
        Integer reviewCount;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookmarkBreederDTO {
        Long breederId;
        String profileUrl;
        String breederName;
        String address;
        AnimalType animalType;
        List<String> species;
        Integer careerYear;
        Integer certificateCount;
        Integer waitAnimal;
        Integer waitList;
        Double breederRating;
        Integer reviewCount;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookmarkBreederPreViewListDTO {
        List<BookmarkBreederDTO> breederList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Data
    @Builder
    public static class TrustLevelResponseDTO {
        private Integer trustLevel;
    }
}
