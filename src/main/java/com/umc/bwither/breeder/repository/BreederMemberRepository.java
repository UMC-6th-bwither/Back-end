package com.umc.bwither.breeder.repository;

import com.umc.bwither.animal.entity.Animal;
import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.breeder.entity.BreederMember;
import com.umc.bwither.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BreederMemberRepository extends JpaRepository<BreederMember, Long> {

    Optional<BreederMember> findByBreederAndMember(Breeder breeder, Member member);

    Page<BreederMember> findByMember(Member member, Pageable pageable);

    Boolean existsByMemberAndBreeder(Member member, Breeder breeder);

    Integer countByBreeder(Breeder breeder);
}
