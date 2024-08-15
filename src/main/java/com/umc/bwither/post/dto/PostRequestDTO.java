package com.umc.bwither.post.dto;

import com.umc.bwither.post.entity.enums.Category;
import com.umc.bwither.post.entity.enums.PetType;
import com.umc.bwither.user.entity.User;
import lombok.*;

import java.util.List;
@Data
public class PostRequestDTO {
    private Long breederId;
    private Long userId;
    private PetType petType;
    private String title;
    private Integer rating;
    private double totalRating;
    private Category category;
    private List<BlockDTO> blocks;

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetTipDTO {

        private Long userId;
        private PetType petType;
        private String title;
        private Category category;
        private List<BlockDTO> blocks;

        public static PostRequestDTO.GetTipDTO getTipDTO(PostRequestDTO.GetTipDTO tipsDTO) {
            return GetTipDTO.builder()
                    .userId(tipsDTO.userId)
                    .petType(tipsDTO.petType)
                    .title(tipsDTO.title)
                    .category(Category.TIPS)
                    .blocks(tipsDTO.blocks)
                    .build();
        }

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetReviewDTO {

        private Long breederId;
        private Long userId;
        private PetType petType;
        private Integer rating;
        private Category category;
        private List<BlockDTO> blocks;

        public static PostRequestDTO.GetReviewDTO getReviewDTO(PostRequestDTO.GetReviewDTO reviewDTO) {
            return GetReviewDTO.builder()
                    .breederId(reviewDTO.breederId)
                    .userId(reviewDTO.userId)
                    .petType(reviewDTO.petType)
                    .category(Category.BREEDER_REVIEWS)
                    .rating(reviewDTO.rating)
                    .blocks(reviewDTO.blocks)
                    .build();
        }

    }
}
