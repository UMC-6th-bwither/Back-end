package com.umc.bwither.breeder.controller;

import com.umc.bwither._base.apiPayLoad.ApiResponse;
import com.umc.bwither._base.apiPayLoad.code.status.SuccessStatus;
import com.umc.bwither._base.common.UserAuthorizationUtil;
import com.umc.bwither.breeder.dto.BreederResponseDTO;
import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.breeder.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inquiries")
@RequiredArgsConstructor
public class InquiryController {
    private final InquiryService inquiryService;
    private final UserAuthorizationUtil userAuthorizationUtil;

    @PostMapping
    public ApiResponse<?> createInquiry(@RequestParam Long userId) {
        Long breederUserId = userAuthorizationUtil.getCurrentUserId();
        inquiryService.createInquiry(userId, breederUserId);
        return ApiResponse.of(SuccessStatus.SUCCESS_CREATE_INQUIRY, userId);

    }

    @GetMapping("/breeders")
    public ApiResponse<?> getBreedersByUserId() {
        Long userId = userAuthorizationUtil.getCurrentUserId();
        List<BreederResponseDTO.BreederPreviewDTO> breeders = inquiryService.getBreedersByUserId(userId);
        System.out.println("브리더:"+breeders);
        return ApiResponse.of(SuccessStatus.SUCCESS_GET_INQUIRYBREEDER, breeders);
    }
}
