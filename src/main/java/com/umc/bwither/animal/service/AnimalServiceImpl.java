package com.umc.bwither.animal.service;

import com.umc.bwither._base.apiPayLoad.code.status.ErrorStatus;
import com.umc.bwither._base.apiPayLoad.exception.handler.TestHandler;
import com.umc.bwither.animal.dto.AnimalRequestDTO.AnimalCreateDTO;
import com.umc.bwither.animal.dto.AnimalResponseDTO.AnimalDetailDTO;
import com.umc.bwither.animal.dto.AnimalResponseDTO.BreederDTO;
import com.umc.bwither.animal.dto.AnimalResponseDTO.FileDTO;
import com.umc.bwither.animal.dto.AnimalResponseDTO.ParentDTO;
import com.umc.bwither.animal.entity.Animal;
import com.umc.bwither.animal.entity.AnimalFile;
import com.umc.bwither.animal.entity.AnimalParents;
import com.umc.bwither.animal.entity.HealthCheckImage;
import com.umc.bwither.animal.entity.enums.FileType;
import com.umc.bwither.animal.entity.enums.ParentType;
import com.umc.bwither.animal.entity.enums.Status;
import com.umc.bwither.animal.repository.AnimalFileRepository;
import com.umc.bwither.animal.repository.AnimalParentsRepository;
import com.umc.bwither.animal.repository.AnimalRepository;
import com.umc.bwither.animal.repository.HealthCheckImageRepository;
import com.umc.bwither.animal.repository.WaitListRepository;
import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.breeder.repository.BreederRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AnimalServiceImpl implements AnimalService {

  private final AnimalRepository animalRepository;
  private final BreederRepository breederRepository;
  private final AnimalFileRepository animalFileRepository;
  private final AnimalParentsRepository animalParentsRepository;
  private final HealthCheckImageRepository healthCheckImageRepository;
  private final WaitListRepository waitListRepository;
  private final S3Uploader s3Uploader;

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

  @Override
  @Transactional
  public Long animalCreate(long memberId, AnimalCreateDTO animalCreateDTO,
      Map<FileType, List<MultipartFile>> animalFiles,
      Map<ParentType, MultipartFile> parentImages,
      Map<ParentType, List<MultipartFile>> parentHealthCheckImages) {

    Breeder breeder = breederRepository.findById(memberId)
        .orElseThrow(() -> new TestHandler(ErrorStatus.BREEDER_NOT_FOUND));

    Animal animal = animalRepository.save(Animal.builder()
        .breeder(breeder)
        .name(animalCreateDTO.getName())
        .type(animalCreateDTO.getType())
        .breed(animalCreateDTO.getBreed())
        .gender(animalCreateDTO.getGender())
        .birthDate(animalCreateDTO.getBirthDate())
        .character(animalCreateDTO.getCharacter())
        .feature(animalCreateDTO.getFeature())
        .feeding(animalCreateDTO.getFeeding())
        .vaccination(animalCreateDTO.getVaccination())
        .virusCheck(animalCreateDTO.getVirusCheck())
        .parasitic(animalCreateDTO.getParasitic())
        .healthCheck(animalCreateDTO.getHealthCheck())
        .status(Status.BEFORE)
        .build());

    if (animalFiles != null) {
      for (Map.Entry<FileType, List<MultipartFile>> entry : animalFiles.entrySet()) {
        FileType fileType = entry.getKey();
        List<MultipartFile> files = entry.getValue();
        if (files != null) {
          for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
              String filePath = s3Uploader.uploadFile("animal-files", file);
              AnimalFile animalFile = AnimalFile.builder()
                  .animal(animal)
                  .type(fileType)
                  .animalFilePath(filePath)
                  .build();
              animalFileRepository.save(animalFile);
            }
          }
        }
      }
    }
    AnimalParents mother = saveParents(animal, ParentType.MOTHER, animalCreateDTO.getMotherName(), animalCreateDTO.getMotherBreed(),
        animalCreateDTO.getMotherBirthDate(), animalCreateDTO.getMotherHereditary(), animalCreateDTO.getMotherCharacter(),
        animalCreateDTO.getMotherHealthCheck(), parentImages.get(ParentType.MOTHER));
    saveParentFiles(mother, ParentType.MOTHER, parentHealthCheckImages);

    AnimalParents father = saveParents(animal, ParentType.FATHER, animalCreateDTO.getFatherName(), animalCreateDTO.getFatherBreed(),
        animalCreateDTO.getFatherBirthDate(), animalCreateDTO.getFatherHereditary(), animalCreateDTO.getFatherCharacter(),
        animalCreateDTO.getFatherHealthCheck(), parentImages.get(ParentType.FATHER));
    saveParentFiles(father, ParentType.FATHER, parentHealthCheckImages);
    return animal.getAnimalId();
  }

  private AnimalParents saveParents(Animal animal, ParentType parentType, String name, String breed, LocalDate birthDate, String hereditary, String character, String healthCheck, MultipartFile image) {
     String imageUrl = s3Uploader.uploadFile("animal-parents", image);

    AnimalParents animalParents = AnimalParents.builder()
        .type(parentType)
        .name(name)
        .breed(breed)
        .birthDate(birthDate)
        .hereditary(hereditary)
        .character(character)
        .healthCheck(healthCheck)
        .animal(animal)
        .imageUrl(imageUrl)
        .build();
    return animalParentsRepository.save(animalParents);
  }

  private void saveParentFiles(AnimalParents animalParents, ParentType parentType, Map<ParentType, List<MultipartFile>> parentHealthCheckImages) {

    if (parentHealthCheckImages != null && parentHealthCheckImages.get(parentType) != null) {
      List<MultipartFile> healthCheckImages = parentHealthCheckImages.get(parentType);
      for (MultipartFile healthCheckImage : healthCheckImages) {
        if (healthCheckImage != null && !healthCheckImage.isEmpty()) {
          String healthCheckImagePath = s3Uploader.uploadFile("parents-health-check-images", healthCheckImage);
          HealthCheckImage healthCheckImageEntity = HealthCheckImage.builder()
              .animal(animalParents.getAnimal())
              .animalParents(animalParents)
              .filePath(healthCheckImagePath)
              .build();
          healthCheckImageRepository.save(healthCheckImageEntity);
        }
      }
    }
  }

}




//
//  @Override
//  public Long createAnimal(long memberId, AnimalCreateDTO animalCreateDTO) {
//    Breeder breeder = breederRepository.findById(memberId).orElseThrow(() -> new TestHandler(ErrorStatus.BREEDER_NOT_FOUND));
//    // Animal 엔티티 생성 및 저장
//    Animal animal = animalRepository.save(Animal.builder()
//        .breeder(breeder)
//        .name(animalCreateDTO.getName())
//        .type(animalCreateDTO.getType())
//        .breed(animalCreateDTO.getBreed())
//        .gender(animalCreateDTO.getGender())
//        .birthDate(animalCreateDTO.getBirthDate())
//        .character(animalCreateDTO.getCharacter())
//        .feature(animalCreateDTO.getFeature())
//        .feeding(animalCreateDTO.getFeeding())
//        .vaccination(animalCreateDTO.getVaccination())
//        .virusCheck(animalCreateDTO.getVirusCheck())
//        .parasitic(animalCreateDTO.getParasitic())
//        .healthCheck(animalCreateDTO.getHealthCheck())
//        .status(Status.BEFORE)
//        .build());
//
//    // AnimalFile 엔티티 생성 및 저장
//    for (AnimalRequestDTO.FileDTO fileDTO : animalCreateDTO.getFiles()) {
//      String filePath = s3Uploader.uploadFile("animal-files", fileDTO.getAnimalFile());
//      AnimalFile animalFile = AnimalFile.builder()
//          .animal(animal)
//          .type(fileDTO.getType())
//          .animalFilePath(filePath)
//          .build();
//      animalFileRepository.save(animalFile);
//    }
//
//    // AnimalParents 엔티티 생성 및 저장
//    for (AnimalRequestDTO.ParentDTO parentDTO : animalCreateDTO.getAnimalParents()) {
//      String parentImagePath = s3Uploader.uploadFile("animal-parents", parentDTO.getImage());
//      AnimalParents animalParents = AnimalParents.builder()
//          .animal(animal)
//          .type(parentDTO.getType())
//          .name(parentDTO.getName())
//          .breed(parentDTO.getBreed())
//          .birthDate(parentDTO.getBirthDate())
//          .hereditary(parentDTO.getHereditary())
//          .character(parentDTO.getCharacter())
//          .healthCheck(parentDTO.getHealthCheck())
//          .imageUrl(parentImagePath)
//          .build();
//      animalParentsRepository.save(animalParents);
//
//      // HealthCheckImage 엔티티 생성 및 저장
//      for (MultipartFile healthCheckImage : parentDTO.getHealthCheckImage()) {
//        String healthCheckImagePath = s3Uploader.uploadFile("animal-parents-healthcheck", healthCheckImage);
//        HealthCheckImage healthCheckImageEntity = HealthCheckImage.builder()
//            .animal(animal)
//            .animalParents(animalParents)
//            .filePath(healthCheckImagePath)
//            .build();
//        healthCheckImageRepository.save(healthCheckImageEntity);
//      }
//    }
//    return animal.getAnimalId();
//  }

