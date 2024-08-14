package com.umc.bwither.breeder.controller;

import com.umc.bwither._base.apiPayLoad.ApiResponse;
import com.umc.bwither._base.apiPayLoad.code.status.SuccessStatus;
import com.umc.bwither.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/breeder")
public class BreederController {

    private final MemberService memberService;

    @PostMapping("/{breederId}/member/{memberId}")
    public ResponseEntity<?> addView(@PathVariable Long breederId, @PathVariable Long memberId) {
        try {
            memberService.addView(memberId, breederId);
            return ResponseEntity.ok(ApiResponse.of(SuccessStatus.SUCCESS_VIEW_BREEDER, null ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.of(SuccessStatus.ERROR_VIEW_BREEDER, e.getMessage()));
        }
    }
}
