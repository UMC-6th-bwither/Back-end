package com.umc.bwither.member.repository;

import com.umc.bwither.member.entity.Member;
import com.umc.bwither.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByMemberId(Long memberId);

    Optional<Member> findByUser(User user);

    Optional<Member> findByUser_UserId(Long userId);
}
