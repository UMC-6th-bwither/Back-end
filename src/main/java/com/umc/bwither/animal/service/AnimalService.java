package com.umc.bwither.animal.service;

import com.umc.bwither.animal.dto.AnimalResponseDTO.AnimalDetailDTO;

public interface AnimalService {

  AnimalDetailDTO getAnimalDetail(Long animalId);
}
