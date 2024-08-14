package com.umc.bwither.animal.repository;

import com.umc.bwither.animal.entity.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
}
