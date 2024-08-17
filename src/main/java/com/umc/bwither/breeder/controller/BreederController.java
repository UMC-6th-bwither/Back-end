package com.umc.bwither.breeder.controller;

import com.umc.bwither._base.apiPayLoad.ApiResponse;
import com.umc.bwither._base.apiPayLoad.code.status.SuccessStatus;
import com.umc.bwither.breeder.entity.enums.AnimalType;
import com.umc.bwither.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.umc.bwither.animal.dto.AnimalResponseDTO;
import com.umc.bwither.animal.service.AnimalService;
import com.umc.bwither.breeder.dto.BreederResponseDTO;
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
    @Parameter(name = "sort", description = "정렬 필드 (createdAt, rating_asc, rating_desc)")
    public ApiResponse<BreederResponseDTO.BreederDetailDTO> getBreederDetail(
            @PathVariable("breederId") Long breederId,
            @RequestParam(name = "sort", defaultValue = "createdAt") String sortField) {
        BreederResponseDTO.BreederDetailDTO result = breederService.getBreederDetail(breederId, sortField);
        return ApiResponse.of(SuccessStatus.SUCCESS_FETCH_BREEDER,result);
    }

    @Operation(summary = "브리더 목록 조회 API", description = "브리더 목록 조회 API")
    @GetMapping("")
    @Parameters({
            @Parameter(name = "page", description = "페이지 번호, 0번이 1 페이지입니다."),
            @Parameter(name = "region", description = "지역 (서울, 세종, 강원, 인천, 경기, 충청북도, 충청남도, 경상북도, 대전, 대구, 전라북도, 경상남도, 울산, 광주, 부산, 전라남도, 제주)"),
            @Parameter(name = "animalType", description = "동물 타입 (DOG, CAT)"),
            @Parameter(name = "species", description = "종"),
            @Parameter(name = "sort", description = "정렬 필드 (createdAt, breederMemberCount, distance, animalCount)")
    })
    public ApiResponse<BreederResponseDTO.BreederPreViewListDTO> getBreederList(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "region", required = false) String region,
            @RequestParam(name = "animalType", required = false) AnimalType animalType,
            @RequestParam(name = "species", required = false) String species,
            @RequestParam(name = "sort", defaultValue = "createdAt") String sortField) {
            BreederResponseDTO.BreederPreViewListDTO result = breederService.getBreederList(region, animalType, species, sortField, page);
            return ApiResponse.of(SuccessStatus.SUCCESS_FETCH_BREEDER,result);
        }

    @PostMapping("/{breederId}/bookmark")
    @Operation(summary = "브리더 저장 API", description = "브리더 저장 API")
    public ApiResponse bookmarkBreeder(
            @PathVariable(name = "breederId") Long breederId,
            @RequestParam String memberId) {
        breederService.bookmarkBreeder(Long.parseLong(memberId), breederId);
        return ApiResponse.onSuccess(SuccessStatus.SUCCESS_BOOKMARK_BREEDER);
    }

    @DeleteMapping("/{breederId}/bookmark")
    @Operation(summary = "브리더 저장 취소 API", description = "사용자가 저장한 동물을 취소하는 API")
    public ApiResponse unbookmarkBreeder(
            @PathVariable(name = "breederId") Long breederId,
            @RequestParam String memberId) {
        breederService.unbookmarkBreeder(Long.parseLong(memberId), breederId);
        return ApiResponse.onSuccess(SuccessStatus.SUCCESS_REMOVE_BOOKMARK_BREEDER);
    }

    @GetMapping("/bookmark")
    @Operation(summary = "내가 저장한 브리더 조회 API", description = "내가 저장한 브리더 조회 API.")
    @Parameters({
            @Parameter(name = "page", description = "페이지 번호, 0번이 1 페이지입니다."),
            @Parameter(name = "animalType", description = "동물 타입 (DOG, CAT)"),
            @Parameter(name = "species", description = "종")
    })
    public ApiResponse<BreederResponseDTO.BookmarkBreederPreViewListDTO> getBookmarkAnimal(
            @RequestParam String memberId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "animalType", required = false) AnimalType type,
            @RequestParam(name = "species", required = false) String species) {
        BreederResponseDTO.BookmarkBreederPreViewListDTO result = breederService.getBookmarkedBreeders(
                Long.parseLong(memberId), type, species, page);
        return ApiResponse.of(SuccessStatus.SUCCESS_FETCH_BOOKMARK_BREEDERS_LIST, result);
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
