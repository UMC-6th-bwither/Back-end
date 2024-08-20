package com.umc.bwither.post.controller;

import com.umc.bwither._base.apiPayLoad.ApiResponse;
import com.umc.bwither._base.apiPayLoad.code.status.SuccessStatus;
import com.umc.bwither.post.dto.S3RequestDTO;
import com.umc.bwither.post.dto.S3ResponseDTO;
import com.umc.bwither._base.common.UserAuthorizationUtil;
import com.umc.bwither.animal.service.S3Uploader;
import com.umc.bwither.post.dto.PostRequestDTO;
import com.umc.bwither.post.dto.PostResponseDTO;
import com.umc.bwither.post.entity.enums.Category;
import com.umc.bwither.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserAuthorizationUtil userAuthorizationUtil;
    private final S3Uploader s3Uploader;

    @PostMapping(value = "/uploads", consumes = "multipart/form-data")
    @Operation(summary = "파일 업로드 API", description = "파일 업로드 API")
    public ResponseEntity<?> uploadFile(@RequestPart(value = "uploadFile", required = false) MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(S3ResponseDTO.builder()
                            .success(0)
                            .file(S3ResponseDTO.FileDTO.builder().url("업로드할 파일이 없습니다.").build())
                            .build());
        }

        try {
            String uploadedFileUrl = s3Uploader.uploadFile("post", file);

            return ResponseEntity.ok(
                    S3ResponseDTO.builder()
                            .success(1)
                            .file(S3ResponseDTO.FileDTO.builder().url(uploadedFileUrl).build())
                            .build()
            );

        } catch (Exception e) {
            log.error("파일 업로드 오류: {}", file.getOriginalFilename(), e);
            S3ResponseDTO result = S3ResponseDTO.builder()
                    .success(0)
                    .file(S3ResponseDTO.FileDTO.builder().url("파일 업로드 실패").build())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(result);
        }
    }

    @Operation(summary = "브리더의 꿀정보 작성 API", description = "브리더의 꿀정보 작성 API")
    @PostMapping("/create/tip")
    public ResponseEntity<?> createTip(@RequestBody PostRequestDTO.GetTipDTO requestDTO) {
        postService.createTips(requestDTO);
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus.SUCCESS_CREATE_TIP));
    }

    @Operation(summary = "브리더 후기 작성 API", description = "브리더 후기 작성 API")
    @PostMapping("/create/review")
    public ResponseEntity<?> createReview(@RequestBody PostRequestDTO.GetReviewDTO requestDTO) {
        postService.createReviews(requestDTO);
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus.SUCCESS_CREATE_REVIEW));
    }


    @Operation(summary = "브리더의 꿀정보 수정 API", description = "브리더의 꿀정보 수정 API. 작성자만 수정할 수 있습니다.")
    @PutMapping("/tips/{postId}")
    public ResponseEntity<String> updateTips(@PathVariable Long postId, @RequestBody PostRequestDTO.GetTipDTO requestDTO) {
        try {
            postService.updateTips(postId, requestDTO);
            return ResponseEntity.ok(SuccessStatus.SUCCESS_UPDATE_TIP.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "브리더 후기 수정 API", description = "브리더 후기 수정 API. 작성자만 수정할 수 있습니다.")
    @PutMapping("/reviews/{postId}")
    public ResponseEntity<?> updateReviews(@PathVariable Long postId, @RequestBody PostRequestDTO.GetReviewDTO requestDTO) {
        try {
            postService.updateReviews(postId, requestDTO);
            return ResponseEntity.ok(SuccessStatus.SUCCESS_UPDATE_REVIEW.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @Operation(summary = "게시글 상세 조회 API", description = "해당 ID의 게시글 상세 조회")
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(@PathVariable Long postId) {
        PostResponseDTO postResponse = postService.getPost(postId);
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus.SUCCESS_GET_POST, postResponse));
    }

    @Operation(summary = "게시글 목록 조회 API", description = "category를 입력하면 해당 카테고리의 전체 글이 조회됩니다. userId를 입력하면 특정 사용자가 작성한 모든 글이 조회됩니다.")
    @GetMapping
    public ResponseEntity<?> getAllPosts(
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long breederId
    ) {
        Long currentUser = userAuthorizationUtil.getCurrentUserId();
        List<PostResponseDTO> posts;

        try {
            if (userId != null) {
                // 특정 사용자의 글 조회
                posts = postService.getPostsByUser(userId);
                return ResponseEntity.ok(ApiResponse.of(SuccessStatus.SUCCESS_GET_ALL_POSTS, posts));
            } else if (category != null) {
                // 카테고리가 주어진 경우, 해당 카테고리의 게시글 조회
                posts = postService.getPostsByCategory(category);

                if (category.equals(Category.TIPS)) {
                    return ResponseEntity.ok(ApiResponse.of(SuccessStatus.SUCCESS_GET_ALL_TIP_POSTS, posts));
                } else if (category.equals(Category.BREEDER_REVIEWS)) {
                    return ResponseEntity.ok(ApiResponse.of(SuccessStatus.SUCCESS_GET_ALL_REVIEW_POSTS, posts));
                } else {
                    return ResponseEntity.badRequest().body("유효하지 않은 카테고리입니다.");
                }
            } else if(breederId != null){
                posts = postService.getPostsByBreederId(breederId);
                return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK, posts));
            }else {
                // category와 userId가 둘 다 없는 경우, 전체 게시글 조회
                posts = postService.getAllPosts(currentUser);
                return ResponseEntity.ok(ApiResponse.of(SuccessStatus.SUCCESS_GET_ALL_POSTS, posts));
            }
        } catch (Exception e) {
            // 예외 발생 시 에러 메시지와 상태 코드 반환
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "게시글 삭제 API", description = "해당 ID의 게시글 삭제")
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @Operation(summary = "브리더의 꿀정보 저장 API", description = "브리더의 꿀정보 저장 API")
    @PostMapping("/{postId}/bookmark")
    public ResponseEntity<?> bookmarkPost(@PathVariable(name = "postId") Long postId) {
        Long memberId = userAuthorizationUtil.getCurrentUserId();
        postService.bookmarkPost(memberId, postId);
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus.SUCCESS_BOOKMARK_POST));
    }

    @Operation(summary = "브리더의 꿀정보 저장 해제 API", description = "브리더의 꿀정보 저장 해제 API")
    @DeleteMapping("/{postId}/bookmark")
    public ResponseEntity<?> unbookmarkPost(@PathVariable(name = "postId") Long postId) {
        Long memberId = userAuthorizationUtil.getCurrentUserId();
        postService.unbookmarkPost(memberId, postId);
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus.SUCCESS_UNBOOKMARK_POST));
    }

    @Operation(summary = "저장한 글 조회 API", description = "현재 로그인한 사용자가 북마크한 모든 브리더 꿀정보를 조회")
    @GetMapping("/bookmarks")
    public ResponseEntity<ApiResponse> getBookmarkedPosts() {
        Long userId = userAuthorizationUtil.getCurrentUserId();
        List<PostResponseDTO> posts = postService.getBookmarkedPosts(userId);
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus.SUCCESS_GET_BOOKMARKED_POSTS, posts));
    }

    // Todo : 목록 DTO 수정
    // Todo : 대표 이미지(입력받은 블록 중 첫번째 이미지)

}