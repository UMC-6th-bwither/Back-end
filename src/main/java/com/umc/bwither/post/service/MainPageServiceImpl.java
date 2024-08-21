package com.umc.bwither.post.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.bwither.animal.repository.AnimalRepository;
import com.umc.bwither.breeder.entity.enums.AnimalType;
import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.breeder.repository.BreederRepository;
import com.umc.bwither.breeder.repository.InquiryRepository;
import com.umc.bwither.post.dto.MainPageResponseDTO.AnimalReviewDTO;
import com.umc.bwither.post.dto.MainPageResponseDTO.BreederProfileDTO;
import com.umc.bwither.post.dto.MainPageResponseDTO.BreederTipsDTO;
import com.umc.bwither.post.dto.MainPageResponseDTO.PopularBreedersDTO;
import com.umc.bwither.post.entity.Post;
import com.umc.bwither.post.entity.enums.Category;
import com.umc.bwither.post.repository.PostRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainPageServiceImpl implements MainPageService {

  private final PostRepository postRepository;
  private final AnimalRepository animalRepository;
  private final BreederRepository breederRepository;


  @Override
  public List<BreederTipsDTO> getMainTipPosts(Category category) {
    Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

    Page<Post> postPage = postRepository.findByCategory(category, pageable);
    List<Post> posts = postPage.getContent();

    List<BreederTipsDTO> breederTipsDTOS = posts.stream()
        .map(post -> {

          String breederName =
              (post.getBreeder() != null) ? post.getBreeder().getTradeName() : "Unknown Breeder";
          String breederImageUrl =
              (post.getBreeder() != null && post.getBreeder().getUser() != null)
                  ? post.getBreeder().getUser().getProfileImage()
                  : "default_image_url";

          String postImageUrl = post.getBlocks().stream()
              .map(block -> parseImageUrl(block.getBlock()))
              .filter(Objects::nonNull)
              .findFirst()
              .orElse(null);

          return BreederTipsDTO.builder()
              .postId(post.getPostId())
              .category(post.getCategory())
              .title(post.getTitle())
              .breederName(breederName)
              .breederImageUrl(breederImageUrl)
              .postImageUrl(postImageUrl)
              .createdAt(post.getCreatedAt())
              .updatedAt(post.getUpdatedAt())
              .build();
        })
        .collect(Collectors.toList());

    return breederTipsDTOS;
  }

  @Override
  public List<PopularBreedersDTO> getMainBreeders(AnimalType animalType) {
    LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
    List<Breeder> breeders = breederRepository.findAll();
    System.out.println("Total Breeders: " + breeders.size());

    breeders.forEach(breeder -> {
      Hibernate.initialize(breeder.getInquiries());
      System.out.println(
          "Breeder ID: " + breeder.getBreederId() + ", Inquiries Size: " + breeder.getInquiries()
              .size());
    });

    List<Breeder> filteredBreeders = breeders.stream()
        .filter(breeder -> animalType == null || breeder.getAnimal() == animalType)
        .peek(breeder -> {
          breeder.getInquiries().forEach(inquiry -> {
            System.out.println("Breeder ID: " + breeder.getBreederId() +
                ", Inquiry ID: " + inquiry.getInquiryId() +
                ", Created At: " + inquiry.getCreatedAt());
          });
        })
        .sorted((breeder1, breeder2) -> {
          long inquiries1 = breeder1.getInquiries().stream()
              .filter(inquiry -> inquiry.getCreatedAt().isAfter(sevenDaysAgo))
              .count();
          long inquiries2 = breeder2.getInquiries().stream()
              .filter(inquiry -> inquiry.getCreatedAt().isAfter(sevenDaysAgo))
              .count();
          return Long.compare(inquiries2, inquiries1);
        })
        .collect(Collectors.toList());

    System.out.println("Filtered Breeders Count: " + filteredBreeders.size());

    List<BreederProfileDTO> breederProfiles = filteredBreeders.stream()
        .map(breeder -> BreederProfileDTO.builder()
            .breederId(breeder.getBreederId())
            .profileUrl(breeder.getUser().getProfileImage())
            .tradeName(breeder.getTradeName())
            .breederRating(breeder.getAverageRating())
            .careerYear(breeder.getExperienceYears())
            .animalType(breeder.getAnimal())
            .build())
        .collect(Collectors.toList());

    PopularBreedersDTO popularBreedersDTO = PopularBreedersDTO.builder()
        .totalBreeders(breeders.size())
        .breederProfiles(breederProfiles)
        .build();

    return List.of(popularBreedersDTO);
  }

  @Override
  public List<AnimalReviewDTO> getMainReviewPosts(Category category) {
    Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

    Page<Post> postPage = postRepository.findByCategory(category, pageable);
    List<Post> posts = postPage.getContent();

    List<AnimalReviewDTO> AnimalReviewDTOS = posts.stream()
        .map(post -> {
          String postImageUrl = post.getBlocks().stream()
              .map(block -> parseImageUrl(block.getBlock()))
              .filter(Objects::nonNull)
              .findFirst()
              .orElse(null);

          return AnimalReviewDTO.builder()
              .postId(post.getPostId())
              .title(post.getTitle())
              .postImageUrl(postImageUrl)
              .build();
        })
        .collect(Collectors.toList());

    return AnimalReviewDTOS;
  }

  @Override
  public Integer getMainTitle() {
    return Math.toIntExact(animalRepository.count());
  }

  private String parseImageUrl(String blockJson) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      JsonNode rootNode = objectMapper.readTree(blockJson);
      String type = rootNode.path("type").asText();

      if ("IMAGE".equalsIgnoreCase(type)) {
        JsonNode dataNode = rootNode.path("data");
        JsonNode fileNode = dataNode.path("file");
        return fileNode.path("url").asText(null);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}