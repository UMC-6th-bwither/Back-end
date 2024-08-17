package com.umc.bwither.post.controller;

import com.umc.bwither._base.apiPayLoad.ApiResponse;
import com.umc.bwither._base.apiPayLoad.code.status.SuccessStatus;
import com.umc.bwither.post.dto.PostRequestDTO;
import com.umc.bwither.post.dto.PostResponseDTO;
import com.umc.bwither.post.entity.enums.Category;
import com.umc.bwither.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/create/tip")
    public ResponseEntity<?> createTip(@RequestBody PostRequestDTO.GetTipDTO requestDTO) {
        postService.createTips(requestDTO);
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK));
    }

    @PostMapping("/create/review")
    public ResponseEntity<?> createReview(@RequestBody PostRequestDTO.GetReviewDTO requestDTO) {
        postService.createReviews(requestDTO);
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

    @GetMapping
    public ResponseEntity<ApiResponse> getAllPosts(@RequestParam Long userId) {
        List<PostResponseDTO> posts = postService.getAllPosts(userId);
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK, posts));
    }

    @GetMapping("/tips")
    public ResponseEntity<ApiResponse> getAllTipPosts() {
        List<PostResponseDTO> posts = postService.getPostsByCategory(Category.TIPS);
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK, posts));
    }

    @GetMapping("/reviews")
    public ResponseEntity<ApiResponse> getAllReviewPosts() {
        List<PostResponseDTO> posts = postService.getPostsByCategory(Category.BREEDER_REVIEWS);
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK, posts));
    }

    @PutMapping("/tip/{postId}")
    public ResponseEntity<ApiResponse> updateTip(@PathVariable Long postId, @RequestBody PostRequestDTO.GetTipDTO requestDTO) {
        postService.updateTips(postId, requestDTO);
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK,null));
    }

    @PutMapping("/review/{postId}")
    public ResponseEntity<ApiResponse> updateReview(@PathVariable Long postId, @RequestBody PostRequestDTO.GetReviewDTO requestDTO) {
        postService.updateReviews(postId, requestDTO);
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK,null));
    }

    @PostMapping("/{postId}/bookmark")
    public ResponseEntity<?> bookmarkPost(
            @PathVariable(name = "postId") Long postId,
            @RequestParam Long memberId) {
        postService.bookmarkPost(memberId, postId);
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK));
    }

    @DeleteMapping("/{postId}/bookmark")
    public ResponseEntity<?> unbookmarkPost(
            @PathVariable(name = "postId") Long postId,
            @RequestParam Long memberId) {
        postService.unbookmarkPost(memberId, postId);
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK));
    }

    @GetMapping("/{userId}/bookmarks")
    public ResponseEntity<ApiResponse> getBookmarkedPosts(@PathVariable Long userId) {
        List<PostResponseDTO> posts = postService.getBookmarkedPosts(userId);
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK, posts));
    }
}