package com.umc.bwither.post.dto;

import com.umc.bwither.post.entity.enums.Category;
import com.umc.bwither.post.entity.enums.PetType;
import com.umc.bwither.user.entity.User;
import lombok.*;

import java.util.List;
@Data
public class PostRequestDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetTipDTO {

        private PetType petType;
        private String title;
        private Category category;
        private List<BlockDTO> blocks;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetReviewDTO {

        private Long breederId;
        private PetType petType;
        private Integer rating;
        private Category category;
        private List<BlockDTO> blocks;


    }
}
