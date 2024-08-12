package com.umc.bwither.post.dto;

import com.umc.bwither.post.entity.enums.DataType;
import lombok.Data;

import java.util.Map;
@Data
public class BlockDTO {
    private DataType type;
    private DataDTO data;

    private class DataDTO{
        private String text;
        private ImageUrlDTO file;
    }

    private class ImageUrlDTO{
        private String imageUrl;
    }
}
