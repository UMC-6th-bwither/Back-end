package com.umc.bwither.breeder.service;

import com.umc.bwither.breeder.dto.BreederResponseDTO;
import com.umc.bwither.breeder.dto.BreederResponseDTO.BreederPreViewListDTO;
import com.umc.bwither.breeder.dto.BreederResponseDTO.BreederDetailDTO;
import com.umc.bwither.breeder.dto.BreederResponseDTO.TrustLevelResponseDTO;
import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.breeder.entity.BreederFile;
import com.umc.bwither.breeder.entity.Breeding;
import com.umc.bwither.breeder.entity.enums.AnimalType;
import com.umc.bwither.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface BreederService {
    void saveBreeder(Breeder breeder);

    void saveBreeding(Breeding breeding);

    void saveBreederFile(BreederFile breederFile);

    BreederDetailDTO getBreederDetail(Long breederId);

    BreederPreViewListDTO getBreederList(String region, AnimalType animalType, String species, String sortField, Integer page);

    void bookmarkBreeder(long memberId, Long breederId);

    void unbookmarkBreeder(long memberId, Long breederId);

    BreederResponseDTO.BookmarkBreederPreViewListDTO getBookmarkedBreeders(long memberId, AnimalType type, String species, Integer page);

    TrustLevelResponseDTO getTrustLevel(Long breederId);
}