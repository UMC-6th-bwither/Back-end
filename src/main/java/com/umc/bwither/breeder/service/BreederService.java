package com.umc.bwither.breeder.service;

import com.umc.bwither.breeder.dto.BreederResponseDTO;
import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.breeder.entity.BreederFile;
import com.umc.bwither.breeder.entity.Breeding;
import com.umc.bwither.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface BreederService {
    void saveBreeder(Breeder breeder);

    void saveBreeding(Breeding breeding);

    void saveBreederFile(BreederFile breederFile);

    BreederResponseDTO.TrustLevelResponseDTO getTrustLevel(Long breederId);
}
