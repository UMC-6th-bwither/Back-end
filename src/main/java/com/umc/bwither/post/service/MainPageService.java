package com.umc.bwither.post.service;

import com.umc.bwither.breeder.entity.enums.AnimalType;
import com.umc.bwither.post.dto.MainPageResponseDTO.AnimalReviewDTO;
import com.umc.bwither.post.dto.MainPageResponseDTO.BreederTipsDTO;
import com.umc.bwither.post.dto.MainPageResponseDTO.PopularBreedersDTO;
import com.umc.bwither.post.entity.enums.Category;
import java.util.List;

public interface MainPageService {

  List<BreederTipsDTO> getMainTipPosts(Category category);

  List<PopularBreedersDTO> getMainBreeders(AnimalType animalType);

  List<AnimalReviewDTO> getMainReviewPosts(Category category);

  Integer getMainTitle();
}
