package com.umc.bwither.breeder.repository;

import com.umc.bwither.breeder.entity.Breeding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BreedingRepository  extends JpaRepository<Breeding, Long> {
    @Query("SELECT SUM(YEAR(COALESCE(b.leaveDate, CURRENT_DATE)) - YEAR(b.joinDate)) " +
            "FROM Breeding b WHERE b.breeder.breederId = :breederId")
    Integer findTotalCareerYearsByBreederId(@Param("breederId") Long breederId);
}
