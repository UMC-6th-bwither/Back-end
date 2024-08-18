package com.umc.bwither.post.repository;

import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.post.entity.Post;
import com.umc.bwither.post.entity.enums.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCategory(Category category);

    @Query("SELECT COUNT(p) FROM Post p WHERE p.breeder.breederId = :breederId AND p.category = com.umc.bwither.post.entity.enums.Category.BREEDER_REVIEWS")
    int countByBreederAndBreederReviewsCategory(Long breederId);
    List<Post> findByBreederAndCategory(Breeder breeder, Category category);

    List<Post> findByBreederAndCategory(Breeder breeder, Category category, Sort sort);

    @Query("SELECT COUNT(p) FROM Post p WHERE p.breeder.breederId = :breederId AND p.category = com.umc.bwither.post.entity.enums.Category.BREEDER_REVIEWS")
    int countReviewsByBreederId(Long breederId);
}
