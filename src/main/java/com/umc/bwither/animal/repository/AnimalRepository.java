package com.umc.bwither.animal.repository;

import com.umc.bwither.animal.entity.Animal;
import com.umc.bwither.animal.entity.enums.Gender;
import com.umc.bwither.breeder.entity.Breeder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimalRepository extends JpaRepository<Animal, Long> {

  List<Animal> findByBreeder_BreederId(Long breederId);

  Integer countByBreeder(Breeder breeder);

  Page<Animal> findByBreeder(Breeder breeder, Pageable pageable);
}
