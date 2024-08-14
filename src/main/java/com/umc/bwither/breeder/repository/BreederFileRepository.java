package com.umc.bwither.breeder.repository;

import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.breeder.entity.BreederFile;
import com.umc.bwither.breeder.entity.enums.FileType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BreederFileRepository  extends JpaRepository<BreederFile, Long> {

    List<BreederFile> findByBreederAndType(Breeder breeder, FileType fileType);

    void deleteByBreederAndType(Breeder breeder, FileType fileType);
}

