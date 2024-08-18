package com.umc.bwither.animal.dto;

import com.umc.bwither.animal.entity.enums.AnimalType;
import com.umc.bwither.animal.entity.enums.FileType;
import com.umc.bwither.animal.entity.enums.Gender;
import com.umc.bwither.animal.entity.enums.ParentType;
import com.umc.bwither.animal.entity.enums.Status;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
  public static class AnimalPreViewListDTO {
    List<AnimalPreViewDTO> animalList;
    Integer listSize;
    Integer totalPage;
    Long totalElements;
    Boolean isFirst;
    Boolean isLast;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class AnimalPreViewDTO {
    Long animalId;
    Status status;
    String location;
    String name;
    String breed;
    LocalDate birthDate;
    Gender gender;
    String breederName;
    Integer waitList;
    AnimalType type;
    String imageUrl;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
  }

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
    String vaccinationStatus;
    String virusStatus;
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
    Double breederRating;
    Integer reviewCount;
    Integer experienceYears;
    Integer trustLevel;
    //TODO 브리더 태그
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class BookmarkAnimalDTO {
    Long animalId;
    Status status;
    String imageUrl;
    String location;
    String name;
    String breed;
    LocalDate birthDate;
    Gender gender;
    String breederName;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class BookmarkAnimalPreViewListDTO {
    List<BookmarkAnimalDTO> animalList;
    Integer listSize;
    Integer totalPage;
    Long totalElements;
    Boolean isFirst;
    Boolean isLast;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class BreederAnimalDTO {
    Long animalId;
    Status status;
    String imageUrl;
    String location;
    String name;
    String breed;
    LocalDate birthDate;
    Gender gender;
    String breederName;
    Integer waitList;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class BreederAnimalPreViewListDTO {
    List<BreederAnimalDTO> animalList;
    Integer listSize;
    Integer totalPage;
    Long totalElements;
    Boolean isFirst;
    Boolean isLast;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class MissingFilesDTO {
    private Long animalId;
    private String fileType;
  }


}
