package com.umc.bwither.post.service;

import com.umc.bwither.post.dto.PostRequestDTO;
import com.umc.bwither.post.dto.PostResponseDTO;
import com.umc.bwither.post.entity.enums.Category;

import java.util.Calendar;
import java.util.List;

public interface PostService {
    void createTips(PostRequestDTO.GetTipDTO tipDTO);
    void createReviews(PostRequestDTO.GetReviewDTO reviewDTO);

    PostResponseDTO getPost(Long postId);

    List<PostResponseDTO.PostPreviewDTO> getAllPosts(Long userId);

    List<PostResponseDTO.PostPreviewDTO> getPostsByUser(Long userId);

    List<PostResponseDTO.PostPreviewDTO> getPostsByCategory(Category category);

    void deletePost(Long postId);

    void updateTips(Long postId, PostRequestDTO.GetTipDTO requestDTO);

    void updateReviews(Long postId, PostRequestDTO.GetReviewDTO requestDTO);

    void bookmarkPost(Long memberId, Long postId);

    void unbookmarkPost(Long memberId, Long postId);

    List<PostResponseDTO.PostPreviewDTO> getBookmarkedPosts(Long userId);

    List<PostResponseDTO.PostPreviewDTO> getPostsByBreederId(Long breederId);
}
