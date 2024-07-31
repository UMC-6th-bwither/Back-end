package com.umc.bwither.animal.dto;

import com.umc.bwither.animal.entity.enums.AnimalType;
import com.umc.bwither.animal.entity.enums.FileType;
import com.umc.bwither.animal.entity.enums.Gender;
import com.umc.bwither.animal.entity.enums.ParentType;
import com.umc.bwither.animal.entity.enums.Status;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AnimalResponseDTO {

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class AnimalDetailDTO {
    Long animalId;
    Integer waitList;
    Status status;
    String name;
    AnimalType type;
    String breed;
    Gender gender;
    LocalDate birthDate;
    String character;
    String feature;
    String feeding;
    String vaccination;
    String virusCheck;
    String parasitic;
    String healthCheck;
    List<FileDTO> files;
    List<ParentDTO> animalParents;
    BreederDTO breeder;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class FileDTO {
    Long fileId;
    FileType type;
    String animalFilePath;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ParentDTO {
    Long animalParentsId;
    ParentType type;
    String name;
    String breed;
    LocalDate birthDate;
    String hereditary;
    String character;
    String healthCheck;
    String imageUrl;
    List<String> healthCheckImages;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class BreederDTO {
    Long breederId;
    String name;
    String location;
    String description;
    Integer totalAnimals;
//TODO    Double breederRating;
//TODO    Integer reviewCount;
//TODO    Integer experienceYears;
    Integer trustLevel;
    //TODO 브리더 태그
  }
}
