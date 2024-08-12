package com.umc.bwither.animal.repository;

import com.umc.bwither.animal.entity.Animal;
import com.umc.bwither.animal.entity.AnimalMember;
import com.umc.bwither.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalMemberRepository extends JpaRepository<AnimalMember, Long> {

  Optional<AnimalMember> findByAnimalAndMember(Animal animal, Member member);
}
