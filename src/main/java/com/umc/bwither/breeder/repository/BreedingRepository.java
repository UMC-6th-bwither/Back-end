package com.umc.bwither.breeder.repository;

import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.breeder.entity.Breeding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BreedingRepository  extends JpaRepository<Breeding, Long> {
    void deleteByBreeder(Breeder breeder);

    List<Breeding> findByBreeder(Breeder breeder);
}
