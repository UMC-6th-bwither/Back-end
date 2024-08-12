package com.umc.bwither.post.service;

import com.umc.bwither.post.dto.PostRequestDTO;
import com.umc.bwither.post.dto.PostResponseDTO;

public interface PostService {
    void createPost(PostRequestDTO requestDTO);
    void increaseViewCount(Long postId);
    //    void scrapPost(Long postId, String userName);
    int getViewCount(Long postId);

    PostResponseDTO getPost(Long postId);

    void deletePost(Long postId);

}
