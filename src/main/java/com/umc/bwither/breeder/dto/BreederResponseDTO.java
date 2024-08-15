package com.umc.bwither.breeder.dto;

import com.umc.bwither.animal.dto.AnimalResponseDTO;
import com.umc.bwither.animal.entity.enums.Gender;
import com.umc.bwither.animal.entity.enums.Status;
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

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BreedersDTO {
        Long breederId;
        String profileUrl;
        String tradeName;
        String address;
        // Todo   Integer careerYear;
        // Todo   Integer certificateCount;
        // Todo   Integer waitAnimal;
        // Todo   Integer waitList;
        //TODO    Double breederRating;
        //TODO    Integer reviewCount;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BreederPreViewListDTO {
        List<BreedersDTO> animalList;
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
    public static class BookmarkBreederDTO {
        Long breederId;
        String profileUrl;
        String tradeName;
        String address;
        // Todo   Integer careerYear;
        // Todo   Integer certificateCount;
        // Todo   Integer waitAnimal;
        // Todo   Integer waitList;
        //TODO    Double breederRating;
        //TODO    Integer reviewCount;
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
