package com.umc.bwither.post.repository;

import com.umc.bwither.post.entity.Bookmark;
import com.umc.bwither.post.entity.Post;
import com.umc.bwither.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByUserAndPost(User user, Post post);
    Optional<Bookmark> findByUserUserIdAndPostPostId(Long userId, Long postId);
    List<Bookmark> findByUser(User user);
}
