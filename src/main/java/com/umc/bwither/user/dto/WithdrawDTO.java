package com.umc.bwither.user.dto;

import lombok.*;

import java.util.List;

public class WithdrawDTO {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberWithdrawDTO {
        private Long userId;
        private List<String> reasons;
        private String additionalComment;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BreederWithdrawDTO {
        private Long userId;
        private List<String> reasons;
        private String additionalComment;
    }
}
