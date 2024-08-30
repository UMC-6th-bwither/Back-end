package com.umc.bwither.post.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.bwither._base.common.UserAuthorizationUtil;
import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.breeder.repository.BreederRepository;
import com.umc.bwither.post.dto.BlockDTO;
import com.umc.bwither.post.dto.PostRequestDTO;
import com.umc.bwither.post.dto.PostResponseDTO;
import com.umc.bwither.post.entity.Block;
import com.umc.bwither.post.entity.Bookmark;
import com.umc.bwither.post.entity.Post;
import com.umc.bwither.post.entity.enums.Category;
import com.umc.bwither.post.repository.BlockRepository;
import com.umc.bwither.post.repository.BookmarkRepository;
import com.umc.bwither.post.repository.PostRepository;
import com.umc.bwither.user.entity.User;
import com.umc.bwither.user.entity.enums.Role;
import com.umc.bwither.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final BlockRepository blockRepository;
    private final UserRepository userRepository;
    private final BreederRepository breederRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ObjectMapper mapper;
    private final UserAuthorizationUtil userAuthorizationUtil;

    // 블록을 처리하면서 coverImage URL을 추출합니다.
    String coverImageUrl = null;


    @Override
    @Transactional
    public void createTips(PostRequestDTO.GetTipDTO tipDTO) {
        coverImageUrl = null;

        Long userId = userAuthorizationUtil.getCurrentUserId();
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (user.getRole() != Role.BREEDER) {
            // 사용자 역할이 BREEDER가 아닌 경우 예외를 던집니다.
            throw new RuntimeException("브리더 꿀정보는 브리더만 작성할 수 있습니다.");
        }

        Post post = Post.builder()
                .user(user)
                .petType(tipDTO.getPetType())
                .title(tipDTO.getTitle())
                .category(Category.TIPS)
                .build();

        List<Block> blocks = tipDTO.getBlocks().stream()
                .map(blockDTO -> {
                    Block block = new Block();
                    try {
                        // JSON 직렬화
                        String blockContent = mapper.writeValueAsString(blockDTO);
                        block.setBlock(blockContent);
                        block.setPost(post);

                        // JSON 파싱을 통해 이미지 블록의 URL을 추출
                        JsonNode blockNode = mapper.readTree(blockContent);
                        String type = blockNode.get("type").asText();
                        if ("image".equals(type) && coverImageUrl == null) {
                            // 첫 번째 이미지 URL을 coverImage로 설정
                            coverImageUrl = blockNode.get("data")
                                    .get("file")
                                    .get("url").asText();
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("blockDTO 직렬화 및 파싱 오류", e);
                    }
                    return block;
                })
                .collect(Collectors.toList());

        // 추출한 coverImage URL을 post 객체에 설정
        post.setCoverImage(coverImageUrl);

        // Post와 블록들을 저장합니다.
        Post savedPost = postRepository.save(post);
        blockRepository.saveAll(blocks);

        savedPost.setBlocks(blocks);
    }



    @Override
    @Transactional
    public void createReviews(PostRequestDTO.GetReviewDTO reviewDTO) {
        coverImageUrl = null;

        Long userId = userAuthorizationUtil.getCurrentUserId();

        // 브리더 조회
        Breeder breeder = breederRepository.findById(reviewDTO.getBreederId())
                .orElseThrow(() -> new RuntimeException("Breeder not found with id: " + reviewDTO.getBreederId()));

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if(user.getRole() != Role.MEMBER){
            throw new RuntimeException("브리더 후기는 일반 사용자만 작성할 수 있습니다.");
        }

        // 포스트 생성
        Post post = Post.builder()
                .breeder(breeder)
                .user(user)
                .petType(reviewDTO.getPetType())
                .rating(reviewDTO.getRating())
                .category(Category.BREEDER_REVIEWS)
                .build();

        Post savedPost = postRepository.save(post);

        List<Block> blocks = reviewDTO.getBlocks().stream()
                .map(blockDTO -> {
                    Block block = new Block();
                    try {
                        // JSON 직렬화
                        String blockContent = mapper.writeValueAsString(blockDTO);
                        block.setBlock(blockContent);
                        block.setPost(post);

                        // JSON 파싱을 통해 이미지 블록의 URL을 추출
                        JsonNode blockNode = mapper.readTree(blockContent);
                        String type = blockNode.get("type").asText();
                        if ("image".equals(type) && coverImageUrl == null) {
                            // 첫 번째 이미지 URL을 coverImage로 설정
                            coverImageUrl = blockNode.get("data")
                                    .get("file")
                                    .get("url").asText();
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("blockDTO 직렬화 및 파싱 오류", e);
                    }
                    return block;
                })
                .collect(Collectors.toList());

        // 추출한 coverImage URL을 post 객체에 설정
        post.setCoverImage(coverImageUrl);

        // 블록 리스트 저장
        blockRepository.saveAll(blocks);
        savedPost.setBlocks(blocks);

        // 전체 게시글의 평균 별점 계산 및 업데이트
        updateAverageRating(post);
    }



    // 평균 별점 계산 및 업데이트
    @Transactional
    public void updateAverageRating(Post newPost) {
        // Breeder가 null이면 평균 별점 업데이트를 건너뜁니다.
        if (newPost.getBreeder() == null) {
            return;
        }

        List<Post> allPosts = postRepository.findAll();
        double totalRating = 0.0;
        int count = 0;

        for (Post post : allPosts) {
            if (post.getRating() != null) {
                totalRating += post.getRating(); // 각 게시글의 별점 합산
                count++;
            }
        }

        if (count > 0) {
            double averageRating = totalRating / count;
            newPost.getBreeder().setAverageRating(averageRating);
        }

        postRepository.save(newPost);
    }


    @Override
    @Transactional
    public PostResponseDTO getPost(Long postId) {
        Long currentUserId = null;
        try {
            currentUserId = userAuthorizationUtil.getCurrentUserId();
        } catch (Exception e) {
            // currentUserId를 가져오는 데 실패한 경우 예외를 무시하고 null로 설정
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // 조회수 증가
        post.setViewCount(post.getViewCount() + 1);

        postRepository.save(post);

        boolean isSaved = currentUserId != null &&
                bookmarkRepository.findByUserUserIdAndPostPostId(currentUserId, post.getPostId()).isPresent();

        return PostResponseDTO.getPostDTO(post, isSaved);
    }


    @Override
    @Transactional
    public List<PostResponseDTO.PostPreviewDTO> getAllPosts() {
        // DB에서 가져와서 DTO로 변환
        List<Post> postList = postRepository.findAll();
        return postList.stream()
                .map(post -> PostResponseDTO.PostPreviewDTO.getPostPreviewDTO(post))
                .collect(Collectors.toList());
    }

    @Override
    public List<PostResponseDTO.PostPreviewDTO> getPostsByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        List<Post> postList = postRepository.findByUser(user);
        return postList.stream()
                .map(post -> PostResponseDTO.PostPreviewDTO.getPostPreviewDTO(post))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<PostResponseDTO.PostPreviewDTO> getPostsByCategory(Category category) {
        List<Post> postList = postRepository.findByCategory(category);
        return postList.stream()
                .map(post -> PostResponseDTO.PostPreviewDTO.getPostPreviewDTO(post))
                .collect(Collectors.toList());
    }

    @Override
    public List<PostResponseDTO.PostPreviewDTO> getPostsByBreederId(Long breederId) {
        Breeder breeder = breederRepository.findById(breederId).orElseThrow(()-> new RuntimeException("Breeder not found"));
        List<Post> postList = postRepository.findByBreeder(breeder);
        return postList.stream()
                .map(post -> PostResponseDTO.PostPreviewDTO.getPostPreviewDTO(post))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {
        Long userId = userAuthorizationUtil.getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        if(!user.equals(post.getUser())){
            throw new RuntimeException("게시글은 작성자만 삭제할 수 있습니다.");
        }
        postRepository.delete(post);
    }

    @Override
    @Transactional
    public void updateTips(Long postId, PostRequestDTO.GetTipDTO requestDTO) {
        coverImageUrl = null;

        Long userId = userAuthorizationUtil.getCurrentUserId();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if(!userId.equals(post.getUser().getUserId())){
            throw new RuntimeException("게시글은 작성자만 삭제할 수 있습니다.");
        }

        // 제목과 펫 타입 업데이트
        post.setPetType(requestDTO.getPetType());
        post.setTitle(requestDTO.getTitle());

        // 기존 블록 삭제
        blockRepository.deleteAll(post.getBlocks());
        // 포스트의 블록 리스트를 비웁니다
        post.getBlocks().clear();


        List<Block> blocks = requestDTO.getBlocks().stream()
                .map(blockDTO -> {
                    Block block = new Block();
                    try {
                        // JSON 직렬화
                        String blockContent = mapper.writeValueAsString(blockDTO);
                        block.setBlock(blockContent);
                        block.setPost(post);

                        // JSON 파싱을 통해 이미지 블록의 URL을 추출
                        JsonNode blockNode = mapper.readTree(blockContent);
                        String type = blockNode.get("type").asText();
                        if ("image".equals(type) && coverImageUrl == null) {
                            // 첫 번째 이미지 URL을 coverImage로 설정
                            coverImageUrl = blockNode.get("data")
                                    .get("file")
                                    .get("url").asText();
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("blockDTO 직렬화 및 파싱 오류", e);
                    }
                    return block;
                })
                .collect(Collectors.toList());

        // 추출한 coverImage URL을 post 객체에 설정
        post.setCoverImage(coverImageUrl);

        postRepository.save(post);
    }

    @Override
    @Transactional
    public void updateReviews(Long postId, PostRequestDTO.GetReviewDTO requestDTO) {
        coverImageUrl = null;
        Long userId = userAuthorizationUtil.getCurrentUserId();

        // 브리더 조회
        Breeder breeder = breederRepository.findById(requestDTO.getBreederId())
                .orElseThrow(() -> new RuntimeException("Breeder not found with id: " + requestDTO.getBreederId()));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if(!userId.equals(post.getUser().getUserId())){
            throw new RuntimeException("게시글은 작성자만 삭제할 수 있습니다.");
        }


        // 브리더, 펫 타입, 별점 업데이트
        post.setBreeder(breeder);
        post.setPetType(requestDTO.getPetType());
        post.setRating(requestDTO.getRating());

        // 기존 블록 삭제
        blockRepository.deleteAll(post.getBlocks());

        // 포스트의 블록 리스트 비우기
        post.getBlocks().clear();

        List<Block> blocks = requestDTO.getBlocks().stream()
                .map(blockDTO -> {
                    Block block = new Block();
                    try {
                        // JSON 직렬화
                        String blockContent = mapper.writeValueAsString(blockDTO);
                        block.setBlock(blockContent);
                        block.setPost(post);

                        // JSON 파싱을 통해 이미지 블록의 URL을 추출
                        JsonNode blockNode = mapper.readTree(blockContent);
                        String type = blockNode.get("type").asText();
                        if ("image".equals(type) && coverImageUrl == null) {
                            // 첫 번째 이미지 URL을 coverImage로 설정
                            coverImageUrl = blockNode.get("data")
                                    .get("file")
                                    .get("url").asText();
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("blockDTO 직렬화 및 파싱 오류", e);
                    }
                    return block;
                })
                .collect(Collectors.toList());

        post.setCoverImage(coverImageUrl);

        // 블록을 Post에 설정하고 저장
        blockRepository.saveAll(blocks);
        post.setBlocks(blocks);

        postRepository.save(post);

        // 평균 별점 업데이트
        updateAverageRating(post);
    }

    @Override
    @Transactional
    public void bookmarkPost(Long memberId, Long postId) {
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // 이미 북마크한 상태인지 확인
        if (bookmarkRepository.findByUserAndPost(user, post).isPresent()) {
            throw new RuntimeException("Post is already bookmarked");
        }

        // 북마크 추가
        Bookmark bookmark = new Bookmark(user, post);
        post.setBookmarkCount(post.getBookmarkCount() + 1);
        postRepository.save(post);
        bookmarkRepository.save(bookmark);
    }

    @Override
    @Transactional
    public void unbookmarkPost(Long memberId, Long postId) {
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // 북마크가 있는지 확인
        Bookmark bookmark = bookmarkRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new RuntimeException("Bookmark not found"));

        // 북마크 삭제
        post.setBookmarkCount(post.getBookmarkCount() - 1);
        postRepository.save(post);
        bookmarkRepository.delete(bookmark);
    }

    @Override
    @Transactional
    public List<PostResponseDTO.PostPreviewDTO> getBookmarkedPosts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        List<Bookmark> bookmarks = bookmarkRepository.findByUser(user);

        List<Post> posts = bookmarks.stream()
                .map(Bookmark::getPost)
                .collect(Collectors.toList());

        return posts.stream()
                .map(post -> PostResponseDTO.PostPreviewDTO.getPostPreviewDTO(post))
                .collect(Collectors.toList());
    }
}
