package com.umc.bwither.member.controller;

import com.umc.bwither._base.apiPayLoad.ApiResponse;
import com.umc.bwither._base.apiPayLoad.code.status.SuccessStatus;
import com.umc.bwither.member.dto.BreederViewedDTO;
import com.umc.bwither.member.entity.BreederViewed;
import com.umc.bwither.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    @GetMapping("/user/{memberId}/view-breeders")
    public ResponseEntity<?> getRecentViews(@PathVariable Long memberId) {
        try {
            List<BreederViewedDTO> views = memberService.getRecentViews(memberId);
            return ResponseEntity.ok(ApiResponse.of(SuccessStatus.SUCCESS_VIEW_BREEDERS, views ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.of(SuccessStatus.ERROR_VIEW_BREEDERS, e.getMessage()));
        }
    }
}
