package com.umc.bwither.animal.repository;

import com.umc.bwither.animal.entity.Animal;
import com.umc.bwither.animal.entity.AnimalParents;
import com.umc.bwither.animal.entity.enums.ParentType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalParentsRepository extends JpaRepository<AnimalParents, Long> {

  Optional<AnimalParents> findByAnimalAndType(Animal animal, ParentType parentType);
}
