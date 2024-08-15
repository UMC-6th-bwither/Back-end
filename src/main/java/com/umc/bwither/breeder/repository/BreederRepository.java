package com.umc.bwither.breeder.repository;

import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.breeder.entity.enums.AnimalType;
import com.umc.bwither.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BreederRepository extends JpaRepository<Breeder, Long> {
    List<Breeder> findByBreederId(Long breederId);

    @Query("SELECT b.trustLevel FROM Breeder b WHERE b.breederId = :breederId")
    Integer findTrustLevelByBreederId(Long breederId);
}
