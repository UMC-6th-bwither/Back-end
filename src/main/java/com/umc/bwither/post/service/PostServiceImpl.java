package com.umc.bwither.post.service;

import com.umc.bwither.post.dto.BlockDTO;
import com.umc.bwither.post.dto.PostRequestDTO;
import com.umc.bwither.post.dto.PostResponseDTO;
import com.umc.bwither.post.entity.Block;
import com.umc.bwither.post.entity.Post;
import com.umc.bwither.post.entity.enums.DataType; // DataType Enum을 import
import com.umc.bwither.post.repository.PostRepository;
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

    @Override
    @Transactional
    public void createPost(PostRequestDTO requestDTO) {

        List<Block> blocks = new ArrayList<>();
        requestDTO.getBlocks().forEach(blockDTO -> {
            Block block = new Block();
            block.setDataType(blockDTO.getType());

            // 타입에 따른 텍스트 또는 이미지 URL 처리 로직
            if (blockDTO.getType() == DataType.IMAGE && blockDTO.getData().getFile() != null) {
                block.setImageUrl(blockDTO.getData().getFile().getUrl());
            } else if (blockDTO.getType() == DataType.TEXT) {
                block.setText(blockDTO.getData().getText());
            }

            blocks.add(block);
        });

        Post post = Post.create(
//                requestDTO.getUser(),
                requestDTO.getPetType(),
                requestDTO.getTitle(),
                requestDTO.getCategory(),
                blocks
        );

        blocks.forEach(block -> block.setPost(post)); // Block 객체에도 Post 참조 설정

        postRepository.save(post);
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

        List<BlockDTO> blockDTOs = post.getBlocks().stream()
                .map(block -> {
                    BlockDTO.DataDTO dataDTO = block.getImageUrl() != null
                            ? new BlockDTO.DataDTO(null, new BlockDTO.ImageUrlDTO(block.getImageUrl()))
                            : new BlockDTO.DataDTO(block.getText(), null);

                    return BlockDTO.builder()
                            .type(block.getDataType())
                            .data(dataDTO)
                            .build();
                })
                .collect(Collectors.toList());

        return new PostResponseDTO(post.getPostId(), post.getTitle(), post.getPetType(), post.getCategory(), post.getUser().getName(), blockDTOs);
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        postRepository.delete(post);
    }
}
