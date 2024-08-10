package com.umc.bwither.animal.service;

import com.umc.bwither.animal.dto.AnimalRequestDTO.AnimalCreateDTO;
import com.umc.bwither.animal.dto.AnimalResponseDTO.AnimalDetailDTO;
import com.umc.bwither.animal.entity.enums.FileType;
import com.umc.bwither.animal.entity.enums.ParentType;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public interface AnimalService {

  AnimalDetailDTO getAnimalDetail(Long animalId);

  Long animalCreate(long memberId, AnimalCreateDTO animalCreateDTO, Map<FileType, List<MultipartFile>> animalFiles, Map<ParentType, MultipartFile> parentImages, Map<ParentType, List<MultipartFile>> parentHealthCheckImages);

  void animalUpdate(Long animalId, long memberId, AnimalCreateDTO animalCreateDTO, Map<FileType, List<MultipartFile>> animalFiles, Map<ParentType, MultipartFile> parentImages, Map<ParentType, List<MultipartFile>> parentHealthCheckImages);

  boolean isAnimalAuthor(Long animalId, long memberId);

  void bookmarkAnimal(long memberId, Long animalId);
}
