/*
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

    */
/*@PutMapping("/tip/{postId}")
    public ResponseEntity<ApiResponse> updateTip(@PathVariable Long postId, @RequestBody PostRequestDTO.GetTipDTO requestDTO) {
        postService.updateTips(postId, requestDTO);
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK,null));
    }

    @PutMapping("/review/{postId}")
    public ResponseEntity<ApiResponse> updateReview(@PathVariable Long postId, @RequestBody PostRequestDTO.GetReviewDTO requestDTO) {
        postService.updateReviews(postId, requestDTO);
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK,null));
    }*//*


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
}*/
package com.umc.bwither.post.controller;

import com.umc.bwither._base.apiPayLoad.ApiResponse;
import com.umc.bwither._base.apiPayLoad.code.status.SuccessStatus;
import com.umc.bwither._base.common.UserAuthorizationUtil;
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
    private final UserAuthorizationUtil userAuthorizationUtil;

    // Todo : 브리더만 작성할 수 있도록 수정
    @Operation(summary = "브리더의 꿀정보 작성 API", description = "브리더의 꿀정보 작성 API")
    @PostMapping("/create/tip")
    public ResponseEntity<?> createTip(@RequestBody PostRequestDTO.GetTipDTO requestDTO) {
        postService.createTips(requestDTO);
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK));
    }

    // Todo : 일반 멤버만 작성할 수 있도록 수정
    @Operation(summary = "브리더 후기 작성 API", description = "브리더 후기 작성 API")
    @PostMapping("/create/review")
    public ResponseEntity<?> createReview(@RequestBody PostRequestDTO.GetReviewDTO requestDTO) {
        postService.createReviews(requestDTO);
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK));
    }

    // Todo : 작성자만 삭제할 수 있도록 수정
    @Operation(summary = "게시글 삭제 API", description = "해당 ID의 게시글 삭제")
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // Todo : 조회수 count
    @Operation(summary = "게시글 상세 조회 API", description = "해당 ID의 게시글 상세 조회")
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse> getPost(@PathVariable Long postId) {
        PostResponseDTO postResponse = postService.getPost(postId);
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK, postResponse));
    }

    //Todo : 목록 DTO 만들기, Pagenation, sort 설정(최신순, 조회순, 스크랩순), 조회수, 북마크 수
    @Operation(summary = "브리더의 꿀정보 목록 조회 API", description = "브리더의 꿀정보 전체 조회")
    @GetMapping("/tips")
    public ResponseEntity<ApiResponse> getAllTipPosts() {
        List<PostResponseDTO> posts = postService.getPostsByCategory(Category.TIPS);
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK, posts));
    }

    //Todo : 목록 DTO 만들기, sort 설정(최신순, 별점 높은순, 별점 낮은순), 최근 연락한 브리더 기능 구현, 브리더 받은 후기 구현, 사진 리뷰만
    @Operation(summary = "브리더 후기 목록 조회 API", description = "브위더 후기 전체 조회")
    @GetMapping("/reviews")
    public ResponseEntity<ApiResponse> getAllReviewPosts() {
        List<PostResponseDTO> posts = postService.getPostsByCategory(Category.BREEDER_REVIEWS);
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK, posts));
    }

    @Operation(summary = "브리더의 꿀정보 저장 API", description = "브리더의 꿀정보 저장 API")
    @PostMapping("/{postId}/bookmark")
    public ResponseEntity<?> bookmarkPost(@PathVariable(name = "postId") Long postId, @RequestParam Long memberId) {
        postService.bookmarkPost(memberId, postId);
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK));
    }

    @Operation(summary = "브리더의 꿀정보 저장 해제 API", description = "브리더의 꿀정보 저장 해제 API")
    @DeleteMapping("/{postId}/bookmark")
    public ResponseEntity<?> unbookmarkPost(@PathVariable(name = "postId") Long postId, @RequestParam Long memberId) {
        postService.unbookmarkPost(memberId, postId);
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK));
    }

    @Operation(summary = "저장한 글 조회 API", description = "특정 사용자가 북마크한 모든 브리더 꿀정보를 조회")
    @GetMapping("/bookmarks")
    public ResponseEntity<ApiResponse> getBookmarkedPosts() {
        Long userId = userAuthorizationUtil.getCurrentUserId();;
        System.out.println("id: "+userId);
        List<PostResponseDTO> posts = postService.getBookmarkedPosts(userId);
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK, posts));
    }
    // Todo : 브리더의 꿀정보 수정
    // Todo : 브리더 후기 수정
    // Todo : 내가 쓴 글 // 브리더가 작성한 브리더 꿀정보
    // Todo : 나의 후기 // 일반 회원이 작성한 브리더 후기
    // Todo : 브리더 받은 후기
}