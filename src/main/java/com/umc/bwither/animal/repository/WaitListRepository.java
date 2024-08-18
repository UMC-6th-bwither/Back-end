package com.umc.bwither.animal.repository;

import com.umc.bwither.animal.entity.Animal;
import com.umc.bwither.animal.entity.WaitList;
import com.umc.bwither.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WaitListRepository extends JpaRepository<WaitList, Long> {

  Integer countByAnimal(Animal animal);

  Optional<WaitList> findByAnimalAndMember(Animal animal, Member member);

  @Query("SELECT COUNT(DISTINCT wl.animal) FROM WaitList wl WHERE wl.animal.breeder.breederId = :breederId")
  Integer countAnimalsByBreederId(Long breederId);

  @Query("SELECT COUNT(wl.member) FROM WaitList wl WHERE wl.animal.breeder.breederId = :breederId")
  Integer countMembersByBreederId(Long breederId);
}
