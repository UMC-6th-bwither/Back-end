package com.umc.bwither.post.dto;

import com.umc.bwither.post.entity.enums.Category;
import java.time.LocalDateTime;
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
}
