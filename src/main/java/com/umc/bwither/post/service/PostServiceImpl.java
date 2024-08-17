package com.umc.bwither.post.service;

import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.breeder.repository.BreederRepository;
import com.umc.bwither.post.dto.BlockDTO;
import com.umc.bwither.post.dto.PostRequestDTO;
import com.umc.bwither.post.dto.PostResponseDTO;
import com.umc.bwither.post.entity.Block;
import com.umc.bwither.post.entity.Bookmark;
import com.umc.bwither.post.entity.Post;
import com.umc.bwither.post.entity.enums.DataType; // DataType Enum을 import
import com.umc.bwither.post.repository.BookmarkRepository;
import com.umc.bwither.post.repository.PostRepository;
import com.umc.bwither.user.entity.User;
import com.umc.bwither.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final BreederRepository breederRepository;
    private final BookmarkRepository bookmarkRepository;


    @Override
    @Transactional
    public void createTips(PostRequestDTO.GetTipDTO tipDTO) {

        // 사용자 조회
        User user = userRepository.findById(tipDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + tipDTO.getUserId()));

        // 블록 리스트 생성
        List<Block> blocks = new ArrayList<>();
        tipDTO.getBlocks().forEach(blockDTO -> {
            Block block = new Block();
            block.setDataType(blockDTO.getType());

            // 타입에 따른 텍스트 또는 이미지 URL 처리 로직
            if (blockDTO.getType() == DataType.IMAGE && blockDTO.getData().getFile() != null) {
                block.setImageUrl(blockDTO.getData().getFile().getUrl());
            } else if (blockDTO.getType() == DataType.TEXT) {
                block.setText(blockDTO.getData().getText());
            } else {
                throw new IllegalArgumentException("Unsupported data type: " + blockDTO.getType());
            }

            blocks.add(block);
        });

        Post post = Post.builder()
                .user(user)
                .petType(tipDTO.getPetType())
                .title(tipDTO.getTitle())
                .category(tipDTO.getCategory())
                .blocks(blocks)
                .build();

        blocks.forEach(block -> block.setPost(post)); // Block 객체에도 Post 참조 설정

        postRepository.save(post);
    }

    @Override
    @Transactional
    public void createReviews(PostRequestDTO.GetReviewDTO reviewDTO) {
        // 브리더 조회
        Breeder breeder = breederRepository.findById(reviewDTO.getBreederId())
                .orElseThrow(() -> new RuntimeException("Breeder not found with id: " + reviewDTO.getUserId()));

        // 사용자 조회
        User user = userRepository.findById(reviewDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + reviewDTO.getUserId()));

        // 블록 리스트 생성
        List<Block> blocks = new ArrayList<>();
        reviewDTO.getBlocks().forEach(blockDTO -> {
            Block block = new Block();
            block.setDataType(blockDTO.getType());

            // 타입에 따른 텍스트 또는 이미지 URL 처리 로직
            if (blockDTO.getType() == DataType.IMAGE && blockDTO.getData().getFile() != null) {
                block.setImageUrl(blockDTO.getData().getFile().getUrl());
            } else if (blockDTO.getType() == DataType.TEXT) {
                block.setText(blockDTO.getData().getText());
            } else {
                throw new IllegalArgumentException("Unsupported data type: " + blockDTO.getType());
            }

            blocks.add(block);
        });

        Post post = Post.builder()
                .breeder(breeder)
                .user(user)
                .petType(reviewDTO.getPetType())
                .rating(reviewDTO.getRating())
                .category(reviewDTO.getCategory())
                .blocks(blocks)
                .build();

        blocks.forEach(block -> block.setPost(post)); // Block 객체에도 Post 참조 설정

        postRepository.save(post);

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
    public void increaseViewCount(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);
    }

    @Override
    public int getViewCount(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return post.getViewCount();
    }

    @Override
    @Transactional
    public PostResponseDTO getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return PostResponseDTO.getPostDTO(post, bookmarkRepository);
    }

    @Override
    @Transactional
    public List<PostResponseDTO> getAllPosts() {
        // DB에서 가져와서 DTO로 변환
        List<Post> postList = postRepository.findAll();
        return postList.stream()
                .map(template3 -> PostResponseDTO.getPostDTO(template3,bookmarkRepository))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        postRepository.delete(post);
    }

    @Override
    public void updateTips(Long postId, PostRequestDTO.GetTipDTO requestDTO) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setPetType(requestDTO.getPetType());
        post.setTitle(requestDTO.getTitle());

        // 기존 블록 삭제
        post.getBlocks().clear();

        // 새로운 블록 추가
        List<Block> blocks = requestDTO.getBlocks().stream()
                .map(blockDTO -> {
                    Block block = new Block();
                    block.setDataType(blockDTO.getType());

                    if (blockDTO.getType() == DataType.IMAGE && blockDTO.getData().getFile() != null) {
                        block.setImageUrl(blockDTO.getData().getFile().getUrl());
                    } else if (blockDTO.getType() == DataType.TEXT) {
                        block.setText(blockDTO.getData().getText());
                    }

                    return block;
                })
                .collect(Collectors.toList());

        blocks.forEach(block -> block.setPost(post));
        post.getBlocks().addAll(blocks);

        postRepository.save(post);
    }

    @Override
    public void updateReviews(Long postId, PostRequestDTO.GetReviewDTO requestDTO) {
        // 브리더 조회
        Breeder breeder = breederRepository.findById(requestDTO.getBreederId())
                .orElseThrow(() -> new RuntimeException("Breeder not found with id: " + requestDTO.getUserId()));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setBreeder(breeder);
        post.setPetType(requestDTO.getPetType());
        post.setRating(requestDTO.getRating());

        // 기존 블록 삭제
        post.getBlocks().clear();

        // 새로운 블록 추가
        List<Block> blocks = requestDTO.getBlocks().stream()
                .map(blockDTO -> {
                    Block block = new Block();
                    block.setDataType(blockDTO.getType());

                    if (blockDTO.getType() == DataType.IMAGE && blockDTO.getData().getFile() != null) {
                        block.setImageUrl(blockDTO.getData().getFile().getUrl());
                    } else if (blockDTO.getType() == DataType.TEXT) {
                        block.setText(blockDTO.getData().getText());
                    }

                    return block;
                })
                .collect(Collectors.toList());

        blocks.forEach(block -> block.setPost(post));
        post.getBlocks().addAll(blocks);

        postRepository.save(post);
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
        bookmarkRepository.delete(bookmark);
    }
}
