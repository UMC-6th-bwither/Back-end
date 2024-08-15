package com.umc.bwither.post.dto;

import com.umc.bwither.post.entity.enums.Category;
import com.umc.bwither.post.entity.enums.PetType;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDTO {
    private Long id;
    private String title;
    private PetType petType;
    private Integer rating;
    private double averageRating;
    private Category category;
    private String kennelName;
    private String author; // 작성자 필드 추가
    private List<BlockDTO> blocks;
    private LocalDateTime createdAt;


}
