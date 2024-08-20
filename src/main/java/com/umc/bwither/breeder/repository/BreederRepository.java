package com.umc.bwither.breeder.repository;

import com.umc.bwither.breeder.entity.enums.AnimalType;
import com.umc.bwither.breeder.entity.Breeder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import java.util.List;

@Repository
public interface BreederRepository extends JpaRepository<Breeder, Long> {
    List<Breeder> findByBreederId(Long breederId);

    @Query("SELECT b.trustLevel FROM Breeder b WHERE b.breederId = :breederId")
    Integer findTrustLevelByBreederId(Long breederId);

    Optional<Breeder> findByUser_UserId(Long userId);

    List<Breeder> findByAnimal(AnimalType animalType);
}
