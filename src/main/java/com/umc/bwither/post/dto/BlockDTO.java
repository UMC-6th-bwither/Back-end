package com.umc.bwither.post.dto;

import com.umc.bwither.post.entity.enums.DataType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockDTO {
    private DataType type;
    private DataDTO data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataDTO {
        private String text;
        private ImageUrlDTO file;  // file 필드는 ImageUrlDTO 타입

        // Factory method to create appropriate DataDTO instance
        public static DataDTO of(DataType type, String text, ImageUrlDTO file) {
            if (type == DataType.TEXT) {
                return new DataDTO(text, null);
            } else if (type == DataType.IMAGE) {
                return new DataDTO(null, file);
            } else {
                throw new IllegalArgumentException("Unsupported data type: " + type);
            }
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageUrlDTO {
        private String url;  // JSON의 "url" 필드와 일치
    }
}
