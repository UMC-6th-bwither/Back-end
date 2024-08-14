package com.umc.bwither.breeder.service;

import com.umc.bwither.breeder.dto.BreederResponseDTO;
import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface BreederService {
    void saveBreeder(Breeder breeder);

    BreederResponseDTO.TrustLevelResponseDTO getTrustLevel(Long breederId);
}
