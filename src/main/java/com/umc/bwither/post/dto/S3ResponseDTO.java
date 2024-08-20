package com.umc.bwither.post.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class S3ResponseDTO {
    private int success;
    private FileDTO file;

    @Data
    @Builder
    public static class FileDTO {
        private String url;
    }
}
