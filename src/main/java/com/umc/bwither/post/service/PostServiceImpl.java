package com.umc.bwither.post.service;

import com.umc.bwither.post.dto.PostRequestDTO;
import com.umc.bwither.post.entity.Block;
import com.umc.bwither.post.entity.Post;
import com.umc.bwither.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
            block.setData(blockDTO.getData());
            blocks.add(block);
        });

        Post post = Post.create(
                requestDTO.getUser(),
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
}
