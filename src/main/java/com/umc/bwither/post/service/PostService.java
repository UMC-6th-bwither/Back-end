package com.umc.bwither.post.service;

import com.umc.bwither.post.dto.PostRequestDTO;
import com.umc.bwither.post.dto.PostResponseDTO;

import java.util.List;

public interface PostService {
    void createPost(PostRequestDTO requestDTO);
    void increaseViewCount(Long postId);
    //    void scrapPost(Long postId, String userName);
    int getViewCount(Long postId);

    PostResponseDTO getPost(Long postId);

    List<PostResponseDTO> getAllPosts();

    void deletePost(Long postId);

    void updatePost(Long postId, PostRequestDTO requestDTO);

}
