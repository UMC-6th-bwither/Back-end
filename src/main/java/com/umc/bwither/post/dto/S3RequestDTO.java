package com.umc.bwither.post.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
public class S3RequestDTO {
    private MultipartFile uploadFile;
}
