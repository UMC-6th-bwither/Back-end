package com.umc.bwither.post.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.bwither.post.entity.Block;
import com.umc.bwither.post.entity.Post;
import com.umc.bwither.post.entity.enums.Category;
import com.umc.bwither.post.entity.enums.PetType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private String authorImage;
    private LocalDateTime createdAt;
    private Boolean isSaved;
    private List<GetBlockDTO> blocks;
    private Integer viewCount;
    private Integer bookmarkCount;

    public static PostResponseDTO getPostDTO(Post post, Boolean isSaved) {
        ObjectMapper mapper = new ObjectMapper();
        List<GetBlockDTO> blockDTOS = post.getBlocks().stream()
                .map(block -> {
                    try {
                        // JSON 문자열을 Map으로 변환 후 GetBlockDTO로 변환
                        Map<String, Object> blockMap = mapper.readValue(block.getBlock(), Map.class);
                        return GetBlockDTO.fromMap(blockMap);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("Error deserializing block data from JSON", e);
                    }
                })
                .collect(Collectors.toList());

        return PostResponseDTO.builder()
                .id(post.getPostId())
                .title(post.getTitle())
                .petType(post.getPetType())
                .rating(post.getRating())
                .averageRating(post.getBreeder() != null ? post.getBreeder().getAverageRating() : null)
                .category(post.getCategory())
                .kennelName(post.getBreeder() != null ? post.getBreeder().getTradeName() : null)
                .author(post.getUser().getName())
                .authorImage(post.getUser().getProfileImage())
                .createdAt(post.getCreatedAt())
                .isSaved(isSaved)
                .blocks(blockDTOS)
                .viewCount(post.getViewCount())
                .bookmarkCount(post.getBookmarkCount())
                .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetBlockDTO {
        private String id;
        private String type;
        private Map<String, Object> data;

        public static GetBlockDTO fromMap(Map<String, Object> map) {
            return GetBlockDTO.builder()
                    .id((String) map.get("id"))
                    .type((String) map.get("type"))
                    .data((Map<String, Object>) map.get("data"))
                    .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostPreviewDTO {
        private Long id;
        private String coverImage;
        private Boolean hasImage; // 사진이 있는지 여부
        private String title;
        private PetType petType;
        private Integer rating;
        private Category category;
        private String author;
        private String authorImage;
        private String kennelName;
        private List<GetBlockDTO> blocks;
        private LocalDateTime createdAt;
        private Integer viewCount;
        private Integer bookmarkCount;

        public static PostPreviewDTO getPostPreviewDTO(Post post) {
            boolean imageExists = post.getCoverImage() != null && !post.getCoverImage().trim().isEmpty();
            ObjectMapper mapper = new ObjectMapper();
            List<GetBlockDTO> blocks = post.getBlocks().stream()
                    .limit(3) // 첫 번째 요소를 Optional로 가져옵니다.
                    .map(block -> {
                        try {
                            // JSON 문자열을 Map으로 변환 후 GetBlockDTO로 변환
                            Map<String, Object> blockMap = mapper.readValue(block.getBlock(), Map.class);
                            return GetBlockDTO.fromMap(blockMap);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException("Error deserializing block data from JSON", e);
                        }
                    }).collect(Collectors.toList());

            return PostPreviewDTO.builder()
                    .id(post.getPostId())
                    .coverImage(post.getCoverImage())
                    .hasImage(imageExists)
                    .title(post.getTitle())
                    .petType(post.getPetType())
                    .rating(post.getRating())
                    .category(post.getCategory())
                    .author(post.getUser().getName())
                    .authorImage(post.getUser().getProfileImage())
                    .kennelName(post.getBreeder() != null ? post.getBreeder().getTradeName() : null)
                    .blocks(blocks)
                    .createdAt(post.getCreatedAt())
                    .viewCount(post.getViewCount())
                    .bookmarkCount(post.getBookmarkCount())
                    .build();
        }
    }
}
