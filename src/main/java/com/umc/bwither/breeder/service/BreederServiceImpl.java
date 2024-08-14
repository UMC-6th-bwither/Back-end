package com.umc.bwither.breeder.service;

import com.umc.bwither._base.apiPayLoad.code.status.ErrorStatus;
import com.umc.bwither._base.apiPayLoad.exception.handler.TestHandler;
import com.umc.bwither.animal.entity.Animal;
import com.umc.bwither.animal.repository.AnimalRepository;
import com.umc.bwither.breeder.dto.BreederFileDTO;
import com.umc.bwither.breeder.dto.BreederResponseDTO.BreedingAnimalDTO;
import com.umc.bwither.breeder.dto.BreederResponseDTO.TrustLevelResponseDTO;
import com.umc.bwither.breeder.dto.BreederResponseDTO.BreederDetailDTO;
import com.umc.bwither.breeder.dto.BreedingCareerDTO;
import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.breeder.entity.BreederFile;
import com.umc.bwither.breeder.entity.Breeding;
import com.umc.bwither.breeder.repository.BreederFileRepository;
import com.umc.bwither.breeder.repository.BreederRepository;
import com.umc.bwither.breeder.repository.BreedingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BreederServiceImpl implements BreederService {

    private final BreederRepository breederRepository;
    private final BreedingRepository breedingRepository;
    private final BreederFileRepository breederFileRepository;
    private final AnimalRepository animalRepository;

    @Override
    public void saveBreeder(final Breeder breeder) {
        breederRepository.save(breeder);
    }

    @Override
    public void saveBreeding(final Breeding breeding) { breedingRepository.save(breeding); }

    @Override
    public void saveBreederFile(final BreederFile breederFile) { breederFileRepository.save(breederFile); }

    @Override
    public BreederDetailDTO getBreederDetail(Long breederId) {
        Breeder breeder = breederRepository.findById(breederId).
                orElseThrow(() -> new TestHandler(ErrorStatus.BREEDER_NOT_FOUND));
        Integer totalAnimals = animalRepository.countByBreeder(breeder);
        Integer careerYear = breedingRepository.findTotalCareerYearsByBreederId(breederId);

        List<BreederFileDTO> files = breeder.getBreederFiles().stream()
                .map(file -> new BreederFileDTO(file.getBreederFileId(), file.getType(), file.getBreederFilePath()))
                .collect(Collectors.toList());

        List<BreedingCareerDTO> breedingCareers = breeder.getBreedingCareer().stream()
                .map(career -> new BreedingCareerDTO(
                        career.getTradeName(),
                        career.getJoinDate().toString(),
                        career.getLeaveDate() != null ? career.getLeaveDate().toString() : null,
                        career.getCurrentlyEmployed(),
                        career.getDescription()
                ))
                .collect(Collectors.toList());

        List<Animal> animals = animalRepository.findByBreeder_BreederId(breederId);

        List<BreedingAnimalDTO> breedingAnimals = animals.stream()
                .map(animal -> new BreedingAnimalDTO(
                        animal.getAnimalId(),
                        animal.getAge(),
                        animal.getGender().name()
                ))
                .collect(Collectors.toList());

        // Todo ReviewDTO

        BreederDetailDTO breederDetailDTO = BreederDetailDTO.builder()
                .breederId(breeder.getBreederId())
                .profileUrl(breeder.getUser().getProfileImage())
                .tradeName(breeder.getTradeName())
                .species(breeder.getSpecies())
                .address(breeder.getUser().getAddress())
                .description(breeder.getDescription())
                .totalAnimals(totalAnimals)
                //.breederRating(breederRating)
                //.reviewCount(reviewCount)
                .careerYear(careerYear)
                .trustLevel(breeder.getTrustLevel())
                .tradePhone(breeder.getTradePhone())
                .snsAddress(breeder.getSnsAddress())
                .detailDescription(breeder.getDescriptionDetail())
                .schoolName(breeder.getSchoolName())
                .departmentName(breeder.getDepartmentName())
                .enrollmentDate(breeder.getEnrollmentDate())
                .graduationDate(breeder.getGraduationDate())
                .questionGuarantee(breeder.getQuestionGuarantee())
                .questionPedigree(breeder.getQuestionPedigree())
                .questionBaby(breeder.getQuestionBaby())
                .questionPeriod(breeder.getQuestionPeriod())
                .questionSupport(breeder.getQuestionSupport())
                .files(files)
                .breedingCareers(breedingCareers)
                .breedingAnimals(breedingAnimals)
                // .reviews(reviews) // ReviewDTO 리스트
                .build();

        return breederDetailDTO;
    }


    @Override
    public TrustLevelResponseDTO getTrustLevel(Long breederId) {
        Integer trustLevel = breederRepository.findTrustLevelByBreederId(breederId);

        return TrustLevelResponseDTO.builder()
                .trustLevel(trustLevel)
                .build();
    }
}
