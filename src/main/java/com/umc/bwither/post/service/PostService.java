package com.umc.bwither.post.service;

import com.umc.bwither.post.dto.PostRequestDTO;

public interface PostService {
    void createPost(PostRequestDTO requestDTO);
    void increaseViewCount(Long postId);
    //    void scrapPost(Long postId, String userName);
    int getViewCount(Long postId);
}
