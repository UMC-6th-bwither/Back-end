package com.umc.bwither.post.dto;

import com.umc.bwither.post.entity.enums.Category;
import com.umc.bwither.post.entity.enums.PetType;
import com.umc.bwither.user.entity.User;
import lombok.Data;

import java.util.List;
@Data
public class PostRequestDTO {
    private Long userId;
    private PetType petType;
    private String title;
    private Category category;
    private List<BlockDTO> blocks;
}
