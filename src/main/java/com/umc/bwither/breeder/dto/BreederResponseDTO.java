package com.umc.bwither.breeder.dto;

import com.umc.bwither.animal.dto.AnimalResponseDTO;
import com.umc.bwither.animal.entity.enums.Gender;
import com.umc.bwither.animal.entity.enums.Status;
import com.umc.bwither.breeder.entity.enums.AnimalType;
import com.umc.bwither.post.dto.BlockDTO;
import com.umc.bwither.post.entity.Block;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        String animalImage;
        String animalName;
        Integer age;
        String gender;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewDTO {
        Long postId;
        String userName;
        String species;
        Integer rating;
        List<BlockDTO> content;
        LocalDateTime created_at;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BreederTipsDTO {
        Long postId;
        String userName;
        String title;
        String userProfile;
        String thumbnail;
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

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MissingBreederFilesDTO {
        private Long breederId;
        private String fileType;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    public static class BreederFileStatusDTO {
        private Long breederId;
        private String fileType;
        private boolean isUploaded;

        public BreederFileStatusDTO(Long breederId, String fileType, boolean isUploaded) {
            this.breederId = breederId;
            this.fileType = fileType;
            this.isUploaded = isUploaded;
        }
    }
}
