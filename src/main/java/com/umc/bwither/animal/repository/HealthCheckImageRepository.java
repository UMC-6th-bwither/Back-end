package com.umc.bwither.animal.repository;

import com.umc.bwither.animal.entity.AnimalParents;
import com.umc.bwither.animal.entity.HealthCheckImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthCheckImageRepository extends JpaRepository<HealthCheckImage, Long> {

  List<HealthCheckImage> findByAnimalParents(AnimalParents parent);

  void deleteByAnimalParents(AnimalParents parent);
}
