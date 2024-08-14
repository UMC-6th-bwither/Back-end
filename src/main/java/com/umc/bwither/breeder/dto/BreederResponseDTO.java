package com.umc.bwither.breeder.dto;

import lombok.Builder;
import lombok.Data;

public class BreederResponseDTO {
    @Data
    @Builder
    public static class TrustLevelResponseDTO {
        private Integer trustLevel;
    }
}
