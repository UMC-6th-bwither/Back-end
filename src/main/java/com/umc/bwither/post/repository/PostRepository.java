package com.umc.bwither.post.repository;

import com.umc.bwither.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCategory(String category);

    @Query("SELECT COUNT(p) FROM Post p WHERE p.breeder.breederId = :breederId AND p.category = com.umc.bwither.post.entity.enums.Category.BREEDER_REVIEWS")
    int countByBreederAndBreederReviewsCategory(Long breederId);
}
