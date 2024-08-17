package com.umc.bwither.post.dto;

import com.umc.bwither.post.entity.Block;
import com.umc.bwither.post.entity.Post;
import com.umc.bwither.post.entity.enums.Category;
import com.umc.bwither.post.entity.enums.DataType;
import com.umc.bwither.post.entity.enums.PetType;
import com.umc.bwither.post.repository.BookmarkRepository;
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

        return PostResponseDTO.builder()
                .id(post.getPostId())
                .title(post.getTitle())
                .petType(post.getPetType())
                .rating(post.getRating())
                .averageRating(post.getBreeder().getAverageRating())
                .category(post.getCategory())
                .kennelName(post.getBreeder().getTradeName())
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

        /*private DataType type;
        private BlockDTO.DataDTO data;

        public static GetBlockDTO getBlockDTO(Block block) {
            BlockDTO.DataDTO dataDTO = null;

            if (block.getDataType() == DataType.TEXT) {
                dataDTO = new BlockDTO.DataDTO(block.getText(), null);
            } else if (block.getDataType() == DataType.IMAGE) {
                BlockDTO.ImageUrlDTO imageUrlDTO = new BlockDTO.ImageUrlDTO(block.getImageUrl());
                dataDTO = new BlockDTO.DataDTO(null, imageUrlDTO);
            } else {
                throw new IllegalArgumentException("지원되지 않는 데이터 유형 : " + block.getDataType());
            }

            return GetBlockDTO.builder()
                    .type(block.getDataType())
                    .data(dataDTO)
                    .build();
        }*/
    }


}
