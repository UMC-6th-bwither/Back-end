package com.umc.bwither.post.service;

import com.umc.bwither.post.dto.MainPageResponseDTO.BreederTipsDTO;
import com.umc.bwither.post.entity.enums.Category;
import java.util.List;

public interface MainPageService {

  List<BreederTipsDTO> getMainTipPosts(Category category);
}
