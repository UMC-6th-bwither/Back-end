package com.umc.bwither.animal.controller;

import com.umc.bwither._base.apiPayLoad.ApiResponse;
import com.umc.bwither.animal.dto.AnimalResponseDTO;
import com.umc.bwither.animal.service.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/animals")
public class AnimalController {

  private final AnimalService animalService;

  @Operation(summary = "분양대기동물 상세페이지 조회 API", description = "분양대기동물 상세페이지 조회 API / 분양대기동물 아이디(animalId) PathVariable")
  @GetMapping("/{animalId}")
  public ApiResponse<AnimalResponseDTO.AnimalDetailDTO> getAnimalDetail(@PathVariable("animalId") Long animalId) {
    AnimalResponseDTO.AnimalDetailDTO result = animalService.getAnimalDetail(animalId);
    return ApiResponse.onSuccess(result);
  }

}
