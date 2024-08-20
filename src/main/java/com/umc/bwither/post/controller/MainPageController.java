package com.umc.bwither.post.controller;

import com.umc.bwither._base.apiPayLoad.ApiResponse;
import com.umc.bwither._base.apiPayLoad.code.status.SuccessStatus;
import com.umc.bwither.breeder.entity.enums.AnimalType;
import com.umc.bwither.post.dto.MainPageResponseDTO.AnimalReviewDTO;
import com.umc.bwither.post.dto.MainPageResponseDTO.BreederTipsDTO;
import com.umc.bwither.post.dto.MainPageResponseDTO.PopularBreedersDTO;
import com.umc.bwither.post.entity.enums.Category;
import com.umc.bwither.post.service.MainPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class MainPageController {

  private final MainPageService mainPageService;

  @GetMapping("/tips")
  @Operation(summary = "메인페이지 브리더가 말해주는 반려동물 꿀정보 조회 API", description = "메인페이지 브리더가 말해주는 반려동물 꿀정보 조회 API")
  public ResponseEntity<ApiResponse> getMainTipPosts() {
    List<BreederTipsDTO> posts = mainPageService.getMainTipPosts(Category.TIPS);
    return ResponseEntity.ok(ApiResponse.of(SuccessStatus.SUCCESS_FETCH_PET_TIPS, posts));
  }

  @GetMapping("/popular")
  @Operation(summary = "메인페이지 지금 인기 있는 브리더 조회 API", description = "메인페이지 지금 인기 있는 브리더 조회 API")
  @Parameters({
      @Parameter(name = "animalType", description = "동물 타입 (DOG, CAT)"),
  })
  public ResponseEntity<ApiResponse> getMainBreeders(
      @RequestParam(name = "animalType", required = false) AnimalType animalType) {
    List<PopularBreedersDTO> popularBreedersDTOS = mainPageService.getMainBreeders(animalType);
    return ResponseEntity.ok(ApiResponse.of(SuccessStatus.SUCCESS_FETCH_POPULAR_BREEDERS, popularBreedersDTOS));
  }

  @GetMapping("/reviews")
  @Operation(summary = "메인페이지 반려동물 분양 후기 조회 API", description = "메인페이지 반려동물 분양 후기 조회 API")
  public ResponseEntity<ApiResponse> getMainReviewPosts() {
    List<AnimalReviewDTO> posts = mainPageService.getMainReviewPosts(Category.BREEDER_REVIEWS);
    return ResponseEntity.ok(ApiResponse.of(SuccessStatus.SUCCESS_FETCH_PET_REVIEWS, posts));
  }

  @GetMapping("title")
  @Operation(summary = "메인페이지 총 분양대기동물 수 조회 API", description = "메인페이지 총 분양대기동물 수 조회 API")
  public ResponseEntity<ApiResponse> getMainTitle() {
    Integer animalCount = mainPageService.getMainTitle();
    return ResponseEntity.ok(ApiResponse.of(SuccessStatus.SUCCESS_FETCH_PET_COUNTS, animalCount));
  }
}
