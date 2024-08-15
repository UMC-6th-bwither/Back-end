package com.umc.bwither.member.repository;

import com.umc.bwither.member.entity.BreederViewed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BreederViewedRepository extends JpaRepository<BreederViewed, Long> {
    List<BreederViewed> findByMember_MemberIdOrderByViewedAtDesc(Long memberId);
}
