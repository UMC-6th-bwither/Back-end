package com.umc.bwither.post.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.bwither.post.dto.MainPageResponseDTO.BreederTipsDTO;
import com.umc.bwither.post.entity.Post;
import com.umc.bwither.post.entity.enums.Category;
import com.umc.bwither.post.repository.PostRepository;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainPageServiceImpl implements MainPageService{

  private final PostRepository postRepository;

  @Override
  public List<BreederTipsDTO> getMainTipPosts(Category category) {
    Pageable pageable = PageRequest.of(0,10, Sort.by(Sort.Direction.DESC, "createdAt"));
    List<Post> posts = postRepository.findByCategory(category,pageable).getContent();

    List<BreederTipsDTO> breederTipsDTOS = posts.stream()
        .map(post -> {
          String postImageUrl = post.getBlocks().stream()
              .map(block -> parseImageUrl(block.getBlock()))
              .filter(Objects::nonNull)
              .findFirst()
              .orElse(null);

          return BreederTipsDTO.builder()
              .postId(post.getPostId())
              .category(post.getCategory())
              .title(post.getTitle())
              .breederName(post.getBreeder().getTradeName())
              .breederImageUrl(post.getBreeder().getUser().getProfileImage())
              .postImageUrl(postImageUrl)
              .createdAt(post.getCreatedAt())
              .updatedAt(post.getUpdatedAt())
              .build();
        })
        .collect(Collectors.toList());

    return breederTipsDTOS;
  }

  private String parseImageUrl(String blockJson) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode blockNode = objectMapper.readTree(blockJson);

      if ("image".equals(blockNode.get("type").asText())) {
        return blockNode.get("data").get("file").get("url").asText();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
