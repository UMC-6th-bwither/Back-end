package com.umc.bwither.post.controller;

import com.umc.bwither._base.apiPayLoad.ApiResponse;
import com.umc.bwither._base.apiPayLoad.code.status.SuccessStatus;
import com.umc.bwither.post.dto.PostRequestDTO;
import com.umc.bwither.post.dto.PostResponseDTO;
import com.umc.bwither.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestBody PostRequestDTO requestDTO) {
        postService.createPost(requestDTO);
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse> getPost(@PathVariable Long postId) {
        PostResponseDTO postResponse = postService.getPost(postId);
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK, postResponse));
    }
}