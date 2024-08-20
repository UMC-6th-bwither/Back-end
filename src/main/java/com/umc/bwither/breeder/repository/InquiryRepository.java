package com.umc.bwither.breeder.repository;

import com.umc.bwither.breeder.entity.Inquiry;
import com.umc.bwither.breeder.entity.enums.AnimalType;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findByUserUserId(Long userId);
    Optional<Inquiry> findByUserUserIdAndBreederBreederId(Long userId, Long breederId);

    @Query("SELECT i.breeder, COUNT(i) FROM Inquiry i WHERE i.createdAt > :date GROUP BY i.breeder")
    List<Object[]> countByBreederAndCreatedAtAfter(@Param("date") LocalDateTime date);
}
