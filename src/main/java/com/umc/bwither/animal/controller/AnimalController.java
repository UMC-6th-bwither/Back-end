package com.umc.bwither.animal.controller;

import com.umc.bwither._base.apiPayLoad.ApiResponse;
import com.umc.bwither._base.apiPayLoad.code.status.ErrorStatus;
import com.umc.bwither._base.apiPayLoad.code.status.SuccessStatus;
import com.umc.bwither._base.apiPayLoad.exception.handler.TestHandler;
import com.umc.bwither.animal.dto.AnimalRequestDTO;
import com.umc.bwither.animal.dto.AnimalResponseDTO;
import com.umc.bwither.animal.entity.enums.FileType;
import com.umc.bwither.animal.entity.enums.ParentType;
import com.umc.bwither.animal.service.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

  @PostMapping(value = "", consumes = "multipart/form-data")
  @Operation(summary = "분양대기동물 작성 API", description = "분양대기동물 작성 API")
  public ApiResponse<Long> animalCreate(
      @RequestParam String memberId,
      @ModelAttribute AnimalRequestDTO.AnimalCreateDTO animalCreateDTO,
      @RequestPart(value = "pedigreeImage", required = false) MultipartFile pedigreeImage,
      @RequestPart(value = "feedingImages", required = false) List<MultipartFile> feedingImages,
      @RequestPart(value = "vaccinationImages", required = false) List<MultipartFile> vaccinationImages,
      @RequestPart(value = "virusCheckImages", required = false) List<MultipartFile> virusCheckImages,
      @RequestPart(value = "parasiticImages", required = false) List<MultipartFile> parasiticImages,
      @RequestPart(value = "healthCheckImages", required = false) List<MultipartFile> healthCheckImages,
      @RequestPart(value = "animalImages", required = false) List<MultipartFile> animalImages,
      @RequestPart(value = "motherImages", required = false) MultipartFile motherImage,
      @RequestPart(value = "fatherImages", required = false) MultipartFile fatherImage,
      @RequestPart(value = "motherHealthCheckImages", required = false) List<MultipartFile> motherHealthCheckImages,
      @RequestPart(value = "fatherHealthCheckImages", required = false) List<MultipartFile> fatherHealthCheckImages) {
    //동물 파일
    Map<FileType, List<MultipartFile>> animalFiles = new HashMap<>();
    animalFiles.put(FileType.PEDIGREE, pedigreeImage != null ? List.of(pedigreeImage) : List.of());
    animalFiles.put(FileType.FEEDING, feedingImages);
    animalFiles.put(FileType.VACCINATION, vaccinationImages);
    animalFiles.put(FileType.VIRUS_CHECK, virusCheckImages);
    animalFiles.put(FileType.PARASITIC, parasiticImages);
    animalFiles.put(FileType.HEALTH_CHECK, healthCheckImages);
    animalFiles.put(FileType.ANIMAL_IMAGE, animalImages);
    //부모 동물 파일
    Map<ParentType, MultipartFile> parentImages = new HashMap<>();
    parentImages.put(ParentType.MOTHER, motherImage);
    parentImages.put(ParentType.FATHER, fatherImage);

    Map<ParentType, List<MultipartFile>> parentHealthCheckImages = new HashMap<>();
    parentHealthCheckImages.put(ParentType.MOTHER, motherHealthCheckImages);
    parentHealthCheckImages.put(ParentType.FATHER, fatherHealthCheckImages);

    Long animalId = animalService.animalCreate(Long.parseLong(memberId), animalCreateDTO, animalFiles, parentImages, parentHealthCheckImages);

    return ApiResponse.onSuccess(animalId);
  }

  @PutMapping(value = "/{animalId}", consumes = "multipart/form-data")
  @Operation(summary = "분양대기동물 수정 API", description = "분양대기동물 수정 API")
  public ApiResponse<Long> animalUpdate(
      @PathVariable Long animalId,
      @RequestParam String memberId,
      @ModelAttribute AnimalRequestDTO.AnimalCreateDTO animalCreateDTO,
      @RequestPart(value = "pedigreeImage", required = false) MultipartFile pedigreeImage,
      @RequestPart(value = "feedingImages", required = false) List<MultipartFile> feedingImages,
      @RequestPart(value = "vaccinationImages", required = false) List<MultipartFile> vaccinationImages,
      @RequestPart(value = "virusCheckImages", required = false) List<MultipartFile> virusCheckImages,
      @RequestPart(value = "parasiticImages", required = false) List<MultipartFile> parasiticImages,
      @RequestPart(value = "healthCheckImages", required = false) List<MultipartFile> healthCheckImages,
      @RequestPart(value = "animalImages", required = false) List<MultipartFile> animalImages,
      @RequestPart(value = "motherImages", required = false) MultipartFile motherImage,
      @RequestPart(value = "fatherImages", required = false) MultipartFile fatherImage,
      @RequestPart(value = "motherHealthCheckImages", required = false) List<MultipartFile> motherHealthCheckImages,
      @RequestPart(value = "fatherHealthCheckImages", required = false) List<MultipartFile> fatherHealthCheckImages) {
    if (!animalService.isAnimalAuthor(animalId, Long.parseLong(memberId))) {
      throw new TestHandler(ErrorStatus.BREEDER_NOT_AUTHORIZED);
    }
    //동물 파일
    Map<FileType, List<MultipartFile>> animalFiles = new HashMap<>();
    animalFiles.put(FileType.PEDIGREE, pedigreeImage != null ? List.of(pedigreeImage) : List.of());
    animalFiles.put(FileType.FEEDING, feedingImages);
    animalFiles.put(FileType.VACCINATION, vaccinationImages);
    animalFiles.put(FileType.VIRUS_CHECK, virusCheckImages);
    animalFiles.put(FileType.PARASITIC, parasiticImages);
    animalFiles.put(FileType.HEALTH_CHECK, healthCheckImages);
    animalFiles.put(FileType.ANIMAL_IMAGE, animalImages);
    //부모 동물 파일
    Map<ParentType, MultipartFile> parentImages = new HashMap<>();
    parentImages.put(ParentType.MOTHER, motherImage);
    parentImages.put(ParentType.FATHER, fatherImage);

    Map<ParentType, List<MultipartFile>> parentHealthCheckImages = new HashMap<>();
    parentHealthCheckImages.put(ParentType.MOTHER, motherHealthCheckImages);
    parentHealthCheckImages.put(ParentType.FATHER, fatherHealthCheckImages);

    animalService.animalUpdate(animalId,Long.parseLong(memberId), animalCreateDTO, animalFiles, parentImages, parentHealthCheckImages);

    return ApiResponse.onSuccess(animalId);
  }

  @PostMapping("/{animalId}/bookmark")
  @Operation(summary = "동물 저장(북마크) API", description = "동물 저장(북마크) API. 동물 아이디(animalId) PathVariable")
  public ApiResponse bookmarkAnimal(
      @PathVariable(name = "animalId") Long animalId,
      @RequestParam String memberId) {
    animalService.bookmarkAnimal(Long.parseLong(memberId), animalId);
    return ApiResponse.onSuccess(SuccessStatus._OK);
  }

  @DeleteMapping("/{animalId}/bookmark")
  @Operation(summary = "동물 저장(북마크) 취소 API", description = "사용자가 저장한 동물을 취소하는 API. 동물 아이디(animalId) PathVariable")
  public ApiResponse unbookmarkAnimal(
      @PathVariable(name = "animalId") Long animalId,
      @RequestParam String memberId) {
    animalService.unbookmarkAnimal(Long.parseLong(memberId), animalId);
    return ApiResponse.onSuccess(SuccessStatus._OK);
  }


}

//  @PostMapping(value = "", consumes = "multipart/form-data" )
//  @Operation(summary = "분양대기동물 작성 API", description = "분양대기동물 작성 API")
//  public ApiResponse<Long> animalCreate(
//      @RequestParam String memberId,
//      @RequestParam AnimalRequestDTO.AnimalCreateDTO animalCreateDTO) {
//    Long animalId = animalService.createAnimal(Long.parseLong(memberId), animalCreateDTO);
//    return ApiResponse.onSuccess(animalId);
//  }

