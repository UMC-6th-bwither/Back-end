package com.umc.bwither.breeder.controller;

import com.umc.bwither._base.apiPayLoad.ApiResponse;
import com.umc.bwither._base.apiPayLoad.code.status.SuccessStatus;
import com.umc.bwither.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
=======
import com.umc.bwither.animal.dto.AnimalResponseDTO;
import com.umc.bwither.animal.entity.enums.Gender;
import com.umc.bwither.animal.service.AnimalService;
import com.umc.bwither.breeder.dto.BreederResponseDTO;
import com.umc.bwither.breeder.entity.enums.Animal;
import com.umc.bwither.breeder.service.BreederService;
import com.umc.bwither.user.dto.BreederJoinDTO;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/breeder")
public class BreederController {
    private final BreederService breederService;
    private final AnimalService animalService;
    private final MemberService memberService;

    @Operation(summary = "브리더 상세페이지 조회 API", description = "브리더 상세페이지 조회 API")
    @GetMapping("/{breederId}")
    public ApiResponse<BreederResponseDTO.BreederDetailDTO> getBreederDetail(@PathVariable("breederId") Long breederId) {
        BreederResponseDTO.BreederDetailDTO result = breederService.getBreederDetail(breederId);
        return ApiResponse.of(SuccessStatus.SUCCESS_FETCH_BREEDER,result);
    }

    @GetMapping("/{breederId}/trust-level")
    @Operation(summary = "신뢰등급 조회 API", description = "신뢰등급 조회 API")
    public ApiResponse<BreederResponseDTO.TrustLevelResponseDTO> getTrustLevel(
            @PathVariable Long breederId) {
        BreederResponseDTO.TrustLevelResponseDTO result = breederService.getTrustLevel(breederId);
        return ApiResponse.onSuccess(result);
    }

    @GetMapping("/{breederId}/missing-files")
    @Operation(summary = "업로드 누락된 동물 파일 조회 API", description = "업로드 누락된 동물 파일 조회 API")
    public ApiResponse<Map<String, Object>> getMissingFiles(@PathVariable Long breederId) {
        List<AnimalResponseDTO.MissingFilesDTO> missingFilesList = animalService.getAnimalsWithMissingFiles(breederId);

        Map<String, Object> result = new HashMap<>();
        result.put("breederId", breederId);
        result.put("notUpload", missingFilesList);

        return ApiResponse.of(SuccessStatus.SUCCESS_MISSING_PHOTO, result);
    }
  
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
