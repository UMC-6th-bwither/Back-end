package com.umc.bwither.animal.repository;

import com.umc.bwither.animal.entity.Animal;
import com.umc.bwither.animal.entity.WaitList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitListRepository extends JpaRepository<WaitList, Long> {

  Integer countByAnimal(Animal animal);
}
