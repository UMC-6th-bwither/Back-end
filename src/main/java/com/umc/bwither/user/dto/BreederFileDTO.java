package com.umc.bwither.user.dto;

import com.umc.bwither.breeder.entity.enums.FileType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BreederFileDTO {
    private Long breederFileId;
    private FileType type; // 파일 타입
    private String breederFilePath; // 파일 경로
}
