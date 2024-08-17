package com.umc.bwither.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockDTO {
    /*private DataType type;
    private DataDTO data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataDTO {
        private String text;
        private ImageUrlDTO file;

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
        private String url;
    }*/
    private String id;
    private String type;
    private BlockDataDTO data;

    @Data
    public static class BlockDataDTO {
        private String text;
        private String caption;
        private Boolean withBorder;
        private Boolean withBackground;
        private Boolean stretched;
        private FileDTO file;
        private String style;
        private List<String> items;

        @Data
        public static class FileDTO {
            private String url;
        }
    }
}
