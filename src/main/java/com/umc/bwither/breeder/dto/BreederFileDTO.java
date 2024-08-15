package com.umc.bwither.breeder.dto;

import com.umc.bwither.breeder.entity.enums.FileType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BreederFileDTO {
    Long breederFileId;
    FileType type;
    String breederFilePath;
}
