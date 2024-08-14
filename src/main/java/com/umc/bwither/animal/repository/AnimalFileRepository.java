package com.umc.bwither.animal.repository;

import com.umc.bwither.animal.entity.Animal;
import com.umc.bwither.animal.entity.AnimalFile;
import com.umc.bwither.animal.entity.enums.FileType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalFileRepository extends JpaRepository<AnimalFile, Long> {

  List<AnimalFile> findByAnimalAndType(Animal animal, FileType fileType);

  void deleteByAnimalAndType(Animal animal, FileType fileType);
}
