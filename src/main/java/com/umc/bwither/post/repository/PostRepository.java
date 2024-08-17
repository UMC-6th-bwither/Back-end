package com.umc.bwither.post.repository;

import com.umc.bwither.post.entity.Post;
import com.umc.bwither.post.entity.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCategory(Category category);
}
