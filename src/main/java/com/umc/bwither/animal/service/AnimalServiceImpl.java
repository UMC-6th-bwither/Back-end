package com.umc.bwither.animal.service;

import com.umc.bwither._base.apiPayLoad.code.status.ErrorStatus;
import com.umc.bwither._base.apiPayLoad.exception.handler.TestHandler;
import com.umc.bwither.animal.dto.AnimalRequestDTO.AnimalCreateDTO;
import com.umc.bwither.animal.dto.AnimalResponseDTO;
import com.umc.bwither.animal.dto.AnimalResponseDTO.AnimalDetailDTO;
import com.umc.bwither.animal.dto.AnimalResponseDTO.AnimalPreViewDTO;
import com.umc.bwither.animal.dto.AnimalResponseDTO.AnimalPreViewListDTO;
import com.umc.bwither.animal.dto.AnimalResponseDTO.BookmarkAnimalDTO;
import com.umc.bwither.animal.dto.AnimalResponseDTO.BookmarkAnimalPreViewListDTO;
import com.umc.bwither.animal.dto.AnimalResponseDTO.BreederAnimalDTO;
import com.umc.bwither.animal.dto.AnimalResponseDTO.BreederAnimalPreViewListDTO;
import com.umc.bwither.animal.dto.AnimalResponseDTO.BreederDTO;
import com.umc.bwither.animal.dto.AnimalResponseDTO.FileDTO;
import com.umc.bwither.animal.dto.AnimalResponseDTO.ParentDTO;
import com.umc.bwither.animal.entity.Animal;
import com.umc.bwither.animal.entity.AnimalFile;
import com.umc.bwither.animal.entity.AnimalMember;
import com.umc.bwither.animal.entity.AnimalParents;
import com.umc.bwither.animal.entity.HealthCheckImage;
import com.umc.bwither.animal.entity.WaitList;
import com.umc.bwither.animal.entity.enums.AnimalType;
import com.umc.bwither.animal.entity.enums.FileType;
import com.umc.bwither.animal.entity.enums.Gender;
import com.umc.bwither.animal.entity.enums.ParentType;
import com.umc.bwither.animal.entity.enums.Status;
import com.umc.bwither.animal.repository.AnimalFileRepository;
import com.umc.bwither.animal.repository.AnimalMemberRepository;
import com.umc.bwither.animal.repository.AnimalParentsRepository;
import com.umc.bwither.animal.repository.AnimalRepository;
import com.umc.bwither.animal.repository.HealthCheckImageRepository;
import com.umc.bwither.animal.repository.WaitListRepository;
import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.breeder.repository.BreederRepository;
import com.umc.bwither.member.entity.Member;
import com.umc.bwither.member.repository.MemberRepository;
import com.umc.bwither.user.entity.User;
import jakarta.persistence.criteria.Join;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
  private final MemberRepository memberRepository;
  private final AnimalMemberRepository animalMemberRepository;
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
            animal.getBreeder().getBreederId(),
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
  public Long animalCreate(long breederId, AnimalCreateDTO animalCreateDTO,
                           Map<FileType, List<MultipartFile>> animalFiles,
                           Map<ParentType, MultipartFile> parentImages,
                           Map<ParentType, List<MultipartFile>> parentHealthCheckImages) {

    Breeder breeder = breederRepository.findById(breederId)
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

  @Override
  @Transactional
  public void animalUpdate(Long animalId, long breederId, AnimalCreateDTO animalCreateDTO,
                           Map<FileType, List<MultipartFile>> animalFiles, Map<ParentType, MultipartFile> parentImages,
                           Map<ParentType, List<MultipartFile>> parentHealthCheckImages) {
    Animal animal = animalRepository.findById(animalId).orElseThrow(() -> new TestHandler(ErrorStatus.ANIMAL_NOT_FOUND));
    animal.update(animalCreateDTO);
    animalRepository.save(animal);

    if (animalFiles != null) {
      for (Map.Entry<FileType, List<MultipartFile>> entry : animalFiles.entrySet()) {
        FileType fileType = entry.getKey();
        List<MultipartFile> files = entry.getValue();
        if (files != null) {
          List<AnimalFile> oldFiles = animalFileRepository.findByAnimalAndType(animal, fileType);
          for (AnimalFile oldFile : oldFiles) {
            s3Uploader.deleteFile(oldFile.getAnimalFilePath());
          }
          animalFileRepository.deleteByAnimalAndType(animal, fileType);

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

    updateParents(animal, ParentType.MOTHER, animalCreateDTO.getMotherName(), animalCreateDTO.getMotherBreed(),
            animalCreateDTO.getMotherBirthDate(), animalCreateDTO.getMotherHereditary(), animalCreateDTO.getMotherCharacter(),
            animalCreateDTO.getMotherHealthCheck(), parentImages.get(ParentType.MOTHER), parentHealthCheckImages);

    updateParents(animal, ParentType.FATHER, animalCreateDTO.getFatherName(), animalCreateDTO.getFatherBreed(),
            animalCreateDTO.getFatherBirthDate(), animalCreateDTO.getFatherHereditary(), animalCreateDTO.getFatherCharacter(),
            animalCreateDTO.getFatherHealthCheck(), parentImages.get(ParentType.FATHER), parentHealthCheckImages);
  }

  @Override
  public boolean isAnimalAuthor(Long animalId, long breederId) {
    Animal animal = animalRepository.findById(animalId)
            .orElseThrow(() -> new TestHandler(ErrorStatus.ANIMAL_NOT_FOUND));
    return animal.getBreeder().getBreederId() == breederId;
  }

  @Override
  public void bookmarkAnimal(long memberId, Long animalId) {
    Animal animal = animalRepository.findById(animalId)
            .orElseThrow(() -> new TestHandler(ErrorStatus.ANIMAL_NOT_FOUND));
    Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new TestHandler(ErrorStatus.MEMBER_NOT_FOUND));
    animalMemberRepository.findByAnimalAndMember(animal, member)
            .ifPresent(mb -> { throw new TestHandler(ErrorStatus.ANIMAL_ALREADY_BOOKMARK); });

    AnimalMember animalMember = AnimalMember.builder()
            .animal(animal)
            .member(member)
            .build();
    animalMemberRepository.save(animalMember);
  }

  @Override
  public void unbookmarkAnimal(long memberId, Long animalId) {
    Animal animal = animalRepository.findById(animalId)
            .orElseThrow(() -> new TestHandler(ErrorStatus.ANIMAL_NOT_FOUND));
    Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new TestHandler(ErrorStatus.MEMBER_NOT_FOUND));
    AnimalMember animalMember = animalMemberRepository.findByAnimalAndMember(animal, member)
            .orElseThrow(() -> new TestHandler(ErrorStatus.ANIMAL_NOT_BOOKMARK));

    animalMemberRepository.delete(animalMember);
  }

  @Override
  public BookmarkAnimalPreViewListDTO getBookmarkedAnimals(long memberId, AnimalType animalType,
                                                           Gender gender, String breed, Status status, Integer page) {
    Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new TestHandler(ErrorStatus.MEMBER_NOT_FOUND));
    Pageable pageable = PageRequest.of(page, 6, Sort.by(Sort.Direction.DESC, "id"));
    Page<AnimalMember> animalMembers = animalMemberRepository.findByMember(member, pageable);
    List<Long> animalIds = animalMembers.stream()
            .map(animalMember -> animalMember.getAnimal().getAnimalId())
            .collect(Collectors.toList());

    List<Animal> animals = animalRepository.findAllById(animalIds);

    Map<Long, Animal> animalMap = animals.stream()
            .collect(Collectors.toMap(Animal::getAnimalId, Function.identity()));

    List<Animal> sortedAnimals = animalMembers.stream()
            .map(animalMember -> animalMap.get(animalMember.getAnimal().getAnimalId()))
            .collect(Collectors.toList());

    if (animalType != null) {
      sortedAnimals = sortedAnimals.stream()
              .filter(animal -> animal.getType() == animalType)
              .collect(Collectors.toList());
    }
    if (gender != null) {
      sortedAnimals = sortedAnimals.stream()
              .filter(animal -> animal.getGender() == gender)
              .collect(Collectors.toList());
    }
    if (breed != null && !breed.isEmpty()) {
      sortedAnimals = sortedAnimals.stream()
              .filter(animal -> animal.getBreed().equalsIgnoreCase(breed))
              .collect(Collectors.toList());
    }
    if (status != null) {
      sortedAnimals = sortedAnimals.stream()
              .filter(animal -> animal.getStatus() == status)
              .collect(Collectors.toList());
    }

    // DTO로 변환
    List<AnimalResponseDTO.BookmarkAnimalDTO> animalDTOs = sortedAnimals.stream()
            .map(animal -> BookmarkAnimalDTO.builder()
                    .animalId(animal.getAnimalId())
                    .status(animal.getStatus())
                    .imageUrl(animal.getAnimalFiles().isEmpty() ? null : animal.getAnimalFiles().get(0).getAnimalFilePath())
                    .location(animal.getBreeder().getUser().getAddress())
                    .name(animal.getName())
                    .breed(animal.getBreed())
                    .birthDate(animal.getBirthDate())
                    .gender(animal.getGender())
                    .breederName(animal.getBreeder().getTradeName())
                    .build())
            .collect(Collectors.toList());

    // 최종 결과 반환
    return BookmarkAnimalPreViewListDTO.builder()
            .animalList(animalDTOs)
            .listSize(animalDTOs.size())
            .totalPage(animalMembers.getTotalPages())
            .totalElements(animalMembers.getTotalElements())
            .isFirst(animalMembers.isFirst())
            .isLast(animalMembers.isLast())
            .build();
  }

  @Override
  public BreederAnimalPreViewListDTO getBreederAnimals(long breederId, Gender gender, String breed,
      Integer page) {
    Breeder breeder = breederRepository.findById(breederId)
        .orElseThrow(() -> new TestHandler(ErrorStatus.BREEDER_NOT_FOUND));
    Pageable pageable = PageRequest.of(page, 6, Sort.by(Sort.Direction.DESC, "animalId"));
    Page<Animal> animals = animalRepository.findByBreeder(breeder, pageable);
    List<Animal> animalList = new ArrayList<>(animals.getContent());
    if (gender != null) {
      animalList = animalList.stream()
          .filter(animal -> animal.getGender() == gender)
          .collect(Collectors.toList());
    }
    if (breed != null && !breed.isEmpty()) {
      animalList = animalList.stream()
          .filter(animal -> animal.getBreed().equalsIgnoreCase(breed))
          .collect(Collectors.toList());
    }
    List<BreederAnimalDTO> animalDTOs = animalList.stream()
        .map(animal -> {
          int waitListCount = waitListRepository.countByAnimal(animal); // 해당 동물의 대기자 수 계산
          return BreederAnimalDTO.builder()
              .animalId(animal.getAnimalId())
              .name(animal.getName())
              .breed(animal.getBreed())
              .birthDate(animal.getBirthDate())
              .gender(animal.getGender())
              .status(animal.getStatus())
              .imageUrl(animal.getAnimalFiles().isEmpty() ? null : animal.getAnimalFiles().get(0).getAnimalFilePath())
              .location(animal.getBreeder().getUser().getAddress())
              .breederName(breeder.getTradeName())
              .waitList(waitListCount)
              .build();
        })
        .collect(Collectors.toList());

    return BreederAnimalPreViewListDTO.builder()
        .animalList(animalDTOs)
        .listSize(animalDTOs.size())
        .totalPage(animals.getTotalPages())
        .totalElements(animals.getTotalElements())
        .isFirst(animals.isFirst())
        .isLast(animals.isLast())
        .build();
  }

  private void updateParents(Animal animal, ParentType parentType, String name, String breed, LocalDate birthDate, String hereditary, String character, String healthCheck, MultipartFile image, Map<ParentType, List<MultipartFile>> parentHealthCheckImages) {
    AnimalParents parent = animalParentsRepository.findByAnimalAndType(animal, parentType)
            .orElseGet(() -> AnimalParents.builder()
                    .animal(animal)
                    .type(parentType)
                    .build());

    String oldImageUrl = parent.getImageUrl();
    if (oldImageUrl != null) {
      s3Uploader.deleteFile(oldImageUrl);
    }

    String imageUrl = null;
    if (image != null && !image.isEmpty()) {
      imageUrl = s3Uploader.uploadFile("animal-parents", image);
    }

    parent.update(name, breed, birthDate, hereditary, character, healthCheck, imageUrl);

    animalParentsRepository.save(parent);

    // health check images
    if (parentHealthCheckImages != null && parentHealthCheckImages.get(parentType) != null) {
      List<HealthCheckImage> existingHealthCheckImages = healthCheckImageRepository.findByAnimalParents(parent);
      for (HealthCheckImage existingHealthCheckImage : existingHealthCheckImages) {
        s3Uploader.deleteFile(existingHealthCheckImage.getFilePath());
      }
      healthCheckImageRepository.deleteByAnimalParents(parent);
      List<MultipartFile> healthCheckImages = parentHealthCheckImages.get(parentType);
      for (MultipartFile healthCheckImage : healthCheckImages) {
        if (healthCheckImage != null && !healthCheckImage.isEmpty()) {
          String healthCheckImagePath = s3Uploader.uploadFile("parents-health-check-images", healthCheckImage);
          HealthCheckImage healthCheckImageEntity = HealthCheckImage.builder()
                  .animal(animal)
                  .animalParents(parent)
                  .filePath(healthCheckImagePath)
                  .build();
          healthCheckImageRepository.save(healthCheckImageEntity);
        }
      }
    }
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

  public List<AnimalResponseDTO.MissingFilesDTO> getAnimalsWithMissingFiles(Long breederId) {
    // Breeder가 관리하는 모든 동물 조회
    List<Animal> animals = animalRepository.findByBreeder_BreederId(breederId);
    List<AnimalResponseDTO.MissingFilesDTO> missingFilesList = new ArrayList<>();

    for (Animal animal : animals) {
      // 업로드된 파일들을 조회
      List<AnimalFile> uploadedFiles = animalFileRepository.findByAnimal(animal);

      // 모든 파일 타입을 확인하여 업로드되지 않은 파일을 찾음
      for (FileType fileType : FileType.values()) {
        boolean isFileUploaded = uploadedFiles.stream()
                .anyMatch(file -> file.getType().equals(fileType));

        if (!isFileUploaded) {
          missingFilesList.add(new AnimalResponseDTO.MissingFilesDTO(animal.getAnimalId(), fileType.name()));
        }
      }
    }
    return missingFilesList;
  }

  @Override
  public AnimalPreViewListDTO getAnimalList(String region, AnimalType animalType, Gender gender,
      String breed, Status status, String sortField, Integer page) {

    Pageable pageable = PageRequest.of(page, 6, Sort.by(Sort.Direction.DESC, sortField));

    Page<Animal> animals = animalRepository.findAll(pageable);
    List<Animal> animalList = new ArrayList<>(animals.getContent());

    if (gender != null) {
      animalList = animalList.stream()
          .filter(animal -> animal.getGender() == gender)
          .collect(Collectors.toList());
    }

    if (breed != null && !breed.isEmpty()) {
      animalList = animalList.stream()
          .filter(animal -> animal.getBreed().equalsIgnoreCase(breed))
          .collect(Collectors.toList());
    }

    if (status != null) {
      animalList = animalList.stream()
          .filter(animal -> animal.getStatus() == status)
          .collect(Collectors.toList());
    }

    if (region != null && !region.isEmpty()) {
      animalList = animalList.stream()
          .filter(animal -> animal.getBreeder().getUser().getAddress().contains(region))
          .collect(Collectors.toList());
    }

    List<AnimalPreViewDTO> animalDTOs = animalList.stream()
        .map(animal -> {
            int waitListCount = waitListRepository.countByAnimal(animal);
            return AnimalPreViewDTO.builder()
            .animalId(animal.getAnimalId())
            .status(animal.getStatus())
            .location(animal.getBreeder().getUser().getAddress())
            .name(animal.getName())
            .breed(animal.getBreed())
            .birthDate(animal.getBirthDate())
            .gender(animal.getGender())
            .breederName(animal.getBreeder().getTradeName())
            .waitList(waitListCount)
            .type(animal.getType())
            .createdAt(animal.getCreatedAt())
            .updatedAt(animal.getUpdatedAt())
            .imageUrl(animal.getAnimalFiles().isEmpty() ? null : animal.getAnimalFiles().get(0).getAnimalFilePath())
            .build();
        })
        .collect(Collectors.toList());

    return AnimalPreViewListDTO.builder()
        .animalList(animalDTOs)
        .listSize(animalDTOs.size())
        .totalPage(animals.getTotalPages())
        .totalElements(animals.getTotalElements())
        .isFirst(animals.isFirst())
        .isLast(animals.isLast())
        .build();
  }

  @Override
  public void waitAnimal(long memberId, Long animalId) {
    Animal animal = animalRepository.findById(animalId)
        .orElseThrow(() -> new TestHandler(ErrorStatus.ANIMAL_NOT_FOUND));
    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new TestHandler(ErrorStatus.MEMBER_NOT_FOUND));
    waitListRepository.findByAnimalAndMember(animal, member)
        .ifPresent(mb -> { throw new TestHandler(ErrorStatus.ANIMAL_ALREADY_WAIT); });

    WaitList waitList = WaitList.builder()
        .animal(animal)
        .member(member)
        .build();
    waitListRepository.save(waitList);
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


