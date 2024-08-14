package com.umc.bwither.breeder.repository;

import com.umc.bwither.breeder.entity.Breeding;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BreedingRepository  extends JpaRepository<Breeding, Long> {
}
