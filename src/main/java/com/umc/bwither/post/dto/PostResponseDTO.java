package com.umc.bwither.post.dto;

import com.umc.bwither.post.entity.Block;
import com.umc.bwither.post.entity.Post;
import com.umc.bwither.post.entity.enums.Category;
import com.umc.bwither.post.entity.enums.PetType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDTO {
    private Long id;
    private String title;
    private PetType petType;
    private Integer rating;
    private Double averageRating;
    private Category category;
    private String kennelName;
    private String author;
    private LocalDateTime createdAt;
    private Boolean isSaved;
    private List<GetBlockDTO> blocks;
    private Integer viewCount;
    private Integer bookmarkCount;

    public static PostResponseDTO getPostDTO(Post post, Boolean isSaved) {
        List<GetBlockDTO> blockDTOS = post.getBlocks().stream()
                .map(GetBlockDTO::getBlockDTO).toList();

        // Breeder가 null일 경우 처리
        Double averageRating = (post.getBreeder() != null) ? post.getBreeder().getAverageRating() : null;
        String kennelName = (post.getBreeder() != null) ? post.getBreeder().getTradeName() : null;

        return PostResponseDTO.builder()
                .id(post.getPostId())
                .title(post.getTitle())
                .petType(post.getPetType())
                .rating(post.getRating())
                .averageRating(averageRating)
                .category(post.getCategory())
                .kennelName(kennelName)
                .author(post.getUser().getName())
                .createdAt(post.getCreatedAt())
                .isSaved(isSaved)
                .blocks(blockDTOS)
                .viewCount(post.getViewCount())
                .bookmarkCount(post.getBookmarkCount())
                .build();

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetBlockDTO{
        private String block;
        public static GetBlockDTO getBlockDTO(Block block) {
            return GetBlockDTO.builder()
                    .block(block.getBlock())
                    .build();
        }
    }
}
