package com.umc.bwither.post.service;

import com.umc.bwither.post.dto.PostRequestDTO;
import com.umc.bwither.post.dto.PostResponseDTO;
import com.umc.bwither.post.entity.enums.Category;

import java.util.Calendar;
import java.util.List;

public interface PostService {
//    void createPost(PostRequestDTO requestDTO);
    void createTips(PostRequestDTO.GetTipDTO tipDTO);
    void createReviews(PostRequestDTO.GetReviewDTO reviewDTO);
    void increaseViewCount(Long postId);
    //    void scrapPost(Long postId, String userName);
    int getViewCount(Long postId);

    PostResponseDTO getPost(Long postId);

    List<PostResponseDTO> getAllPosts(Long userId);

    List<PostResponseDTO> getPostsByCategory(Category category);

    void deletePost(Long postId);

    void updateTips(Long postId, PostRequestDTO.GetTipDTO requestDTO);

    void updateReviews(Long postId, PostRequestDTO.GetReviewDTO requestDTO);

    void bookmarkPost(Long memberId, Long postId);

    void unbookmarkPost(Long memberId, Long postId);

    List<PostResponseDTO> getBookmarkedPosts(Long userId);
}
