package com.umc.bwither.breeder.service;

import com.umc.bwither.breeder.dto.BreederRequestDTO;
import com.umc.bwither.breeder.dto.BreederResponseDTO;
import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.breeder.entity.BreederFile;
import com.umc.bwither.breeder.entity.Breeding;
import com.umc.bwither.breeder.repository.BreederFileRepository;
import com.umc.bwither.breeder.repository.BreederRepository;
import com.umc.bwither.breeder.repository.BreedingRepository;
import com.umc.bwither.user.entity.User;
import com.umc.bwither.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BreederServiceImpl implements BreederService {

    private final BreederRepository breederRepository;
    private final BreedingRepository breedingRepository;
    private final BreederFileRepository breederFileRepository;

    @Override
    public void saveBreeder(final Breeder breeder) {
        breederRepository.save(breeder);
    }

    @Override
    public void saveBreeding(final Breeding breeding) { breedingRepository.save(breeding); }

    @Override
    public void saveBreederFile(final BreederFile breederFile) { breederFileRepository.save(breederFile); }

    @Override
    public BreederResponseDTO.TrustLevelResponseDTO getTrustLevel(Long breederId) {
        Integer trustLevel = breederRepository.findTrustLevelByBreederId(breederId);

        return BreederResponseDTO.TrustLevelResponseDTO.builder()
                .trustLevel(trustLevel)
                .build();
    }
}
