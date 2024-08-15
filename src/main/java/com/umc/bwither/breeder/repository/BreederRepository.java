package com.umc.bwither.breeder.repository;

import com.umc.bwither.breeder.entity.Breeder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BreederRepository extends JpaRepository<Breeder, Long> {
    @Query("SELECT b.trustLevel FROM Breeder b WHERE b.breederId = :breederId")
    Integer findTrustLevelByBreederId(Long breederId);

    Optional<Breeder> findByUser_UserId(Long userId);
}
