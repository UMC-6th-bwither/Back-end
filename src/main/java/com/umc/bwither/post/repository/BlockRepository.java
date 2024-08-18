package com.umc.bwither.post.repository;

import com.umc.bwither.post.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends JpaRepository<Block,Long> {
}
