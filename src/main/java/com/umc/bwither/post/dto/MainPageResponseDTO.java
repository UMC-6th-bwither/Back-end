package com.umc.bwither.post.dto;

import com.umc.bwither.breeder.entity.enums.AnimalType;
import com.umc.bwither.post.entity.enums.Category;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MainPageResponseDTO {

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class BreederTipsDTO {
    Long postId;
    Category category;
    String title;
    String breederName;
    String breederImageUrl;
    String postImageUrl;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PopularBreedersDTO {
    Integer totalBreeders;
    List<BreederProfileDTO> breederProfiles;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class BreederProfileDTO {
    Long breederId;
    String profileUrl;
    String tradeName;
    Double breederRating;
    Integer careerYear;
    AnimalType animalType;
  }

}
