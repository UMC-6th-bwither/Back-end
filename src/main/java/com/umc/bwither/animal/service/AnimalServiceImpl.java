package com.umc.bwither.animal.service;

import com.umc.bwither._base.apiPayLoad.code.status.ErrorStatus;
import com.umc.bwither._base.apiPayLoad.exception.handler.TestHandler;
import com.umc.bwither.animal.dto.AnimalResponseDTO.AnimalDetailDTO;
import com.umc.bwither.animal.dto.AnimalResponseDTO.BreederDTO;
import com.umc.bwither.animal.dto.AnimalResponseDTO.FileDTO;
import com.umc.bwither.animal.dto.AnimalResponseDTO.ParentDTO;
import com.umc.bwither.animal.entity.Animal;
import com.umc.bwither.animal.entity.HealthCheckImage;
import com.umc.bwither.animal.repository.AnimalRepository;
import com.umc.bwither.animal.repository.WaitListRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnimalServiceImpl implements AnimalService {

  private final AnimalRepository animalRepository;
  private final WaitListRepository waitListRepository;

  @Override
  public AnimalDetailDTO getAnimalDetail(Long animalId) {
    Animal animal = animalRepository.findById(animalId)
        .orElseThrow(() -> new TestHandler(ErrorStatus.ANIMAL_NOT_FOUND));
    Integer waitList = waitListRepository.countByAnimal(animal);
    Integer totalAnimals = animalRepository.countByBreeder(animal.getBreeder());

    List<FileDTO> files = animal.getAnimalFiles().stream()
        .map(file -> new FileDTO(file.getAnimalFileId(), file.getType(), file.getAnimalFilePath()))
        .collect(Collectors.toList());

    List<ParentDTO> animalParents = animal.getAnimalParents().stream()
        .map(parent -> new ParentDTO(
            parent.getAnimalParentsId(),
            parent.getType(),
            parent.getName(),
            parent.getBreed(),
            parent.getBirthDate(),
            parent.getHereditary(),
            parent.getCharacter(),
            parent.getHealthCheck(),
            parent.getImageUrl(),
            parent.getHealthCheckImages().stream()
                .map(HealthCheckImage::getFilePath)
                .collect(Collectors.toList())
        ))
        .collect(Collectors.toList());

    BreederDTO breeder = new BreederDTO(
        animal.getBreeder().getUserId(),
        animal.getBreeder().getTradeName(),
        animal.getBreeder().getUser().getAddress(),
        animal.getBreeder().getDescription(),
        totalAnimals,
//        animal.getBreeder().getRating(), //TODO
//        animal.getBreeder().getReviewCount(), //TODO
//        animal.getBreeder().getExperienceYears(), //TODO
        animal.getBreeder().getTrustLevel()
    );

    AnimalDetailDTO animalDetailDTO = AnimalDetailDTO.builder()
        .animalId(animalId)
        .waitList(waitList)
        .status(animal.getStatus())
        .name(animal.getName())
        .type(animal.getType())
        .breed(animal.getBreed())
        .gender(animal.getGender())
        .birthDate(animal.getBirthDate())
        .character(animal.getCharacter())
        .feature(animal.getFeature())
        .feeding(animal.getFeeding())
        .vaccination(animal.getVaccination())
        .virusCheck(animal.getVirusCheck())
        .parasitic(animal.getParasitic())
        .healthCheck(animal.getHealthCheck())
        .files(files)
        .animalParents(animalParents)
        .breeder(breeder)
        .build();
    return animalDetailDTO;
  }
}
