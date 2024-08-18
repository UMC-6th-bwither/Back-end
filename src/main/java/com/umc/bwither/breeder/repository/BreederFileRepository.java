package com.umc.bwither.breeder.repository;

import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.breeder.entity.BreederFile;
import com.umc.bwither.breeder.entity.enums.FileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BreederFileRepository  extends JpaRepository<BreederFile, Long> {

    @Query("SELECT COUNT(bf) FROM BreederFile bf WHERE bf.breeder.breederId = :breederId AND bf.type = com.umc.bwither.breeder.entity.enums.FileType.CERTIFICATE")
    Integer countCertificatesByBreederId(Long breederId);

    List<BreederFile> findByBreederAndType(Breeder breeder, FileType fileType);

    void deleteByBreederAndType(Breeder breeder, FileType fileType);
}

